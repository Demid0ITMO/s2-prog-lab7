import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.logging.Logger

class BalancerMessageHandler {
    private val queryPool = ArrayBlockingQueue<Pair<ByteArray, SocketAddress>>(10000)
    private val ansPool = ArrayBlockingQueue<Pair<ByteArray, SocketAddress>>(10000)
    private val clientListener = DatagramSocket(1488)
    private val newServerListener = DatagramSocket(1489)
    private val serverPool = ArrayBlockingQueue<Pair<DatagramChannel, Int>>(10000)
    private val logger = Logger.getLogger("Balancer logger")

    fun listenClients() {
        while (true) {
            Executors.newCachedThreadPool().run {
                val byteArray = ByteArray(65535)
                val datagramPacket = DatagramPacket(byteArray, byteArray.size)
                clientListener.receive(datagramPacket)
                val address = datagramPacket.socketAddress
                logger.info("Message from client $address")
                queryPool.add(datagramPacket.data to address)
            }
        }
    }

    fun listenServersQuery() {
        while (true) {
            Executors.newCachedThreadPool().run {
                val byteArray = ByteArray(65535)
                val datagramPacket = DatagramPacket(byteArray, byteArray.size)
                newServerListener.receive(datagramPacket)
                val data = datagramPacket.data.filter { it != 0.toByte() }.toByteArray()
                val query = String(data, 0, data.size).splitToSequence(" ", ignoreCase =  true, limit = 2).toMutableList()
                println(query.size)
                if (query[0] == "new_server") {
                    val port = query[1].toInt()
                    serverPool.add(DatagramChannel.open() to port)
                    logger.info("New server ${datagramPacket.socketAddress}")
                }
                else {
                    val clientToken = query[1]
                    for (server in serverPool) {
                        server.first.send(ByteBuffer.wrap(clientToken.toByteArray()), InetSocketAddress("localhost", server.second))
                    }
                }
            }
        }
    }

    fun sendToServerAndReceiveAnswer() {
        while (true) {
            if (queryPool.isNotEmpty() && serverPool.isNotEmpty()) {
                Executors.newCachedThreadPool().run {
                    val query = queryPool.first()
                    queryPool.remove(query)
                    var server = serverPool.first()
                    var serverAddress = InetSocketAddress( "localhost", server.second)
                    val selector = Selector.open()
                    server.first.configureBlocking(false)
                    server.first.register(selector, SelectionKey.OP_READ)
                    selector.select(1500)
                    var selectedKeys = selector.selectedKeys()
                    val ansBuffer = ByteBuffer.wrap(ByteArray(65536))
                    while (selectedKeys.isEmpty()) {
                        if(serverPool.isNotEmpty()) {
                            server = serverPool.first()
                            serverPool.remove()
                            serverAddress = InetSocketAddress( "localhost", server.second)
                        }
                        server.first.send(ByteBuffer.wrap(query.first.filter { it != 0.toByte() }.toByteArray()), serverAddress)
                        server.first.configureBlocking(false)
                        server.first.register(selector, SelectionKey.OP_READ)
                        selector.select(1500)
                        selectedKeys = selector.selectedKeys()
                    }
                    server.first.receive(ansBuffer)
                    serverPool.add(server)
                    ansPool.add(ansBuffer.array() to query.second)
                }
            }
        }
    }

    fun sendAnswerToClient() {
        while (true) {
            if (ansPool.isNotEmpty()) {
                Executors.newCachedThreadPool().run {
                    val ans = ansPool.first()
                    ansPool.remove(ans)
                    val byteArray = ans.first.filter { it != 0.toByte() }.toByteArray()
                    val address = ans.second
                    val datagramPacket = DatagramPacket(byteArray, byteArray.size, address)
                    clientListener.send(datagramPacket)
                    logger.info("Send answer to $address")
                }
            }
        }
    }
}