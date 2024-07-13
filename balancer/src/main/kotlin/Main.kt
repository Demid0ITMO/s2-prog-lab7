import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val balancerMessageHandler = BalancerMessageHandler()
    thread { balancerMessageHandler.listenClients() }
    thread { balancerMessageHandler.listenServersQuery() }
    thread { balancerMessageHandler.sendToServerAndReceiveAnswer() }
    thread { balancerMessageHandler.sendAnswerToClient() }
}
