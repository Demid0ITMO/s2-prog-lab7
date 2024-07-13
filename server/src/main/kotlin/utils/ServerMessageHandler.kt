package utils

import builders.packet
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.logging.Logger

class ServerMessageHandler: KoinComponent {
    private val queryPool = ArrayBlockingQueue<Pair<ArrayList<Packet>, SocketAddress>>(10000)
    private val outPool = ArrayBlockingQueue<Pair<ArrayList<Packet>, SocketAddress>>(10000)
    private val logger = Logger.getLogger("Handler logger")
    private var socket = DatagramSocket(1490)
    private val clients: HashMap<String, ClientAssistant> by inject()
    init {
        val unlogged_client = "unlogged_user"
        clients[unlogged_client] = ClientAssistant(-32132)
    }

    fun executeQuery() {
        while (true) {
            if (queryPool.isNotEmpty()) {
                Thread().start().run {
                    val query = queryPool.first()
                    queryPool.remove(query)
                    val query_from = query.first.first().token
                    if (clients[query_from] == null) clients[query_from] = ClientAssistant(query.first.first().user_id)
                    else if (query.first.first().commandName == "command") clients.remove(query_from)
                    else {
                        val out = clients[query_from]!!.executeQuery(query.first)
                        outPool.add(out to query.second)
                    }
                }
            }
        }
    }

    fun receiveMessage() {
        while (true) {
            Executors.newCachedThreadPool().run {
                val byteArray = ByteArray(65535)
                val datagramPacket = DatagramPacket(byteArray, byteArray.size)
                socket.receive(datagramPacket)
                val address = datagramPacket.socketAddress
                val query = unpackMessage(datagramPacket)
                val query_from = query.first().token
                logger.info("Message from client $query_from")
                queryPool.add(query to address)
            }
        }
    }

    fun sendMessage() {
        while (true) {
            if (outPool.isNotEmpty()) {
                ForkJoinPool.commonPool().run {
                    val out = outPool.first()
                    outPool.remove(out)
                    logger.info("Sending answer to ${out.first.first().token} ${out.first.first().commandName}")
                    val datagramPacket = packMessage(out.first, out.second)
                    socket.send(datagramPacket)
                }
            }
        }
    }
    private fun unpackMessage(datagramPacket: DatagramPacket) : ArrayList<Packet> {
        return deserializeMessage(String(datagramPacket.data, 0, datagramPacket.length))
    }

    private fun packMessage(packets: ArrayList<Packet>, address: SocketAddress): DatagramPacket {
        var byteArray = ByteArray(65535)
        val datagramPacket = DatagramPacket(byteArray, byteArray.size)
        byteArray = serializeMessage(packets).toByteArray()
        datagramPacket.setData(byteArray, 0, byteArray.size)
        datagramPacket.socketAddress = address
        return datagramPacket
    }

    fun setConnection(port: Int) {
        val balancerAddress = InetSocketAddress("localhost", 1489)
        val channel = DatagramChannel.open()
        channel.send(ByteBuffer.wrap("new_server $port".toByteArray()), balancerAddress)
        channel.close()
        socket.close()
        socket = DatagramSocket(port)
    }

    companion object {

        private val serializator = Serializator()
        private fun deserializeMessage(message: String) : ArrayList<Packet> {
            return serializator.deserialize(message, ArrayList<Packet>())
        }
        private fun serializeMessage(packets: ArrayList<Packet>) : String {
            return serializator.serialize(packets)
        }
        fun updateUser(token: String, user_id: Long) {
            val balancerAddress = InetSocketAddress("localhost", 1489)
            val channel = DatagramChannel.open()
            channel.send(ByteBuffer.wrap("update_user ${serializeMessage(packet{ this.token = token; this.user_id = user_id }.wrapIntoArray())}".toByteArray()), balancerAddress)
            channel.close()
        }
    }
}