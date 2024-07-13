package clientCommands

import utils.*
import commandArgumentsAndTheirsComponents.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/***
 * Абстрактный класс команды
 * @author Demid0
 * @since 1.0
 */
open class ClientCommand<R>(
    val commandName: String,
    val commandType: CommandType,
    val visibility: Visibility,
    private val type: (ArrayList<CommandArgument>) -> R,
    private val execution: ClientCommand<R>.(Long, R) -> ArrayList<Packet>): KoinComponent {

    val collectionManager: CollectionManager by inject()
    internal val serializator: Serializator by inject()
    internal val clientCommandInvoker: ClientCommandInvoker by inject()
    internal val clients: HashMap<String, ClientAssistant> by inject()
    internal val dbHandler: DBHandler by inject()
    internal val tokenizer: Tokenizer by inject()
    internal val tokens: HashMap<Long, String> by inject()

    fun execute(arguments: ArrayList<CommandArgument>, user_id: Long): ArrayList<Packet> {
        return execution.invoke(this, user_id, type.invoke(arguments))
    }
}
