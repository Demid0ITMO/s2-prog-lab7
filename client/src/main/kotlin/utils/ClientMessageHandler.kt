package utils

import builders.printToClientPacket
import exceptions.SystemCommandInvocationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.util.concurrent.LinkedBlockingQueue

class ClientMessageHandler: KoinComponent {
    private val serializator: Serializator by inject()
    private val app: App by inject()
    private val writerManager: WriterManager by inject()
    private val readerManager: ReaderManager by inject()
    private val systemCommandInvoker: SystemCommandInvoker by inject()
    private val channel = DatagramChannel.open()
    private val balancerAddress = InetSocketAddress("localhost", 1488)
    private val condition: Condition by inject()
    private val threadPool = LinkedBlockingQueue<Thread>()

    fun run() {
        val packet = getPacket()
        val ans = sendAndRecieve(packet)
        if (!ans.first) {
            systemCommandInvoker.invoke(printToClientPacket("Server is dead"))
            val thread = Thread {
                var tryAgain = sendAndRecieve(packet)
                while (!tryAgain.first) tryAgain = sendAndRecieve(packet)
                if (threadPool.size == 1) systemCommandInvoker.invoke(printToClientPacket("\nServer is alive\n> ", false))
                threadPool.remove()
            }
            threadPool.add(thread)
            thread.start()
        } else {
            invoke(ans.second)
        }
    }

    fun invoke(ansBuffer: ByteBuffer) {
        val ans = unpackMessage(ansBuffer)
        try {
            systemCommandInvoker.invoke(ans)
        } catch (e: SystemCommandInvocationException) {
            app.setDefaultCondition(e)
        }
    }

    fun sendAndRecieve(packet: Packet): Pair<Boolean, ByteBuffer> {
        packet.token = condition.token
        val byteBuffer = packMessage(packet.wrapIntoArray())
        val ansBuffer = ByteBuffer.wrap(ByteArray(65535))
        sendMessage(byteBuffer, balancerAddress)
        return receiveMessage(ansBuffer) to ansBuffer
    }

    fun getPacket(): Packet {
        var packet = app.run(readerManager, writerManager)
        while (packet == null) packet = app.run(readerManager, writerManager)
        return packet
    }

    fun packMessage(packet: ArrayList<Packet>) : ByteBuffer {
        return ByteBuffer.wrap(serializator.serialize(packet).toByteArray())
    }

    fun unpackMessage(byteBuffer: ByteBuffer): ArrayList<Packet> {
        return serializator.deserialize(String(byteBuffer.array(), 0, byteBuffer.position()), ArrayList<Packet>())
    }

    fun sendMessage(byteBuffer: ByteBuffer, address: InetSocketAddress) {
        channel.send(byteBuffer, address)
    }

    fun receiveMessage(byteBuffer: ByteBuffer): Boolean {
        val selector = Selector.open()
        channel.configureBlocking(false)
        channel.register(selector, SelectionKey.OP_READ)
        selector.select(10000)
        val selectedKeys = selector.selectedKeys()
        return if (selectedKeys.isEmpty()) {
            false
        } else {
            channel.receive(byteBuffer)
            true
        }
    }

}
