package systemCommands

import exceptions.SystemCommandInvocationException

val clearClientCommands = SystemCommand("clear_client_commands", {} ) {
    try {
        parseCommandAndAskArguments.clear()
    } catch (_: Exception) {
        throw SystemCommandInvocationException()
    }
}