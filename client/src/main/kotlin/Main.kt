import org.koin.core.component.inject
import org.koin.core.context.startKoin
import utils.*

fun main(args: Array<String>) {
    startKoin {
        modules(clientKoinModule)
    }
    val clientUtilFabric = ClientUtilsFabric()
    val clientMessageHandler: ClientMessageHandler by clientUtilFabric.inject()
    while (true) {
        clientMessageHandler.run()
    }
}