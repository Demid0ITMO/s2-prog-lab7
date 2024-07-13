package utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.Logger

class ClientAssistant(private val user_id: Long): KoinComponent {
    private val clientCommandInvoker: ClientCommandInvoker by inject()
    private val logger = Logger.getLogger("Handler logger")

    fun executeQuery(packets: ArrayList<Packet>): ArrayList<Packet> {
        logger.info("Executing query to user (user_id=$user_id)")
        return clientCommandInvoker.invoke(packets, user_id)
    }
}
