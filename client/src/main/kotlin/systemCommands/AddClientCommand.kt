package systemCommands

import exceptions.SystemCommandInvocationException
import utils.argToCommand

val addClientCommand = SystemCommand("add_client_command", argToCommand) {
        (singleArg, commandType) ->
    try {
        parseCommandAndAskArguments.addCommand(singleArg, commandType)
    } catch (_: Exception) {
        throw SystemCommandInvocationException()
    }
}
