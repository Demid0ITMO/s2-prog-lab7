import org.koin.core.context.startKoin
import utils.*
import kotlin.concurrent.thread

fun main(args: Array<String>)  {
    startKoin {
        modules(dbHandlerModule, serverKoinModule)
    }
    var incorrectPort = true
    val serverMessageHandler = ServerMessageHandler()
    var port: Int
    while (incorrectPort) {
        print("Номер порта: ")
        try {
            port = readln().toInt()
            serverMessageHandler.setConnection(port)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Некорректный номер порта")
            continue
        }
        incorrectPort = false
    }
    thread { serverMessageHandler.receiveMessage() }
    thread { serverMessageHandler.executeQuery() }
    thread { serverMessageHandler.sendMessage() }
}
