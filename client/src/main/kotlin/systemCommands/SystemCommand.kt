package systemCommands

import utils.parseCommandAndAskArguments
import commandArgumentsAndTheirsComponents.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.*
import java.io.*

class SystemCommand<R>(
    val commandName: String,
    private val type: (ArrayList<CommandArgument>) -> R,
    private val execution: SystemCommand<R>.(R) -> Unit
): KoinComponent {
    internal val scriptStack: ArrayDeque<String> by inject()
    internal val readerStack: HashMap<String, BufferedReader> by inject()
    internal val parseCommandAndAskArguments : parseCommandAndAskArguments by inject()
    internal val writerManager : WriterManager by inject()
    internal val readerManager : ReaderManager by inject()
    internal val condition : Condition by inject()
    fun execute(arguments: ArrayList<CommandArgument>) {
        execution.invoke(this, type.invoke(arguments))
    }
}

