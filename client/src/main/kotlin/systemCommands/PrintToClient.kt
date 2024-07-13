package systemCommands

import exceptions.SystemCommandInvocationException
import utils.argToString

val printToClient = SystemCommand("print_to_client", argToString) {
        singleArg ->
    try {
        val writer = writerManager.get()
        writer.print(singleArg)
        writer.flush()
    } catch (_: Exception) {
        throw SystemCommandInvocationException()
    }
}
