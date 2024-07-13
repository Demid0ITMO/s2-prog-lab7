package clientCommands

import builders.packet
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToVisibility

val checkout = ClientCommand("checkout", CommandType.VISIBILITY_ARG, Visibility.ALL_USERS, argToVisibility) {
        _, currentVisibilityLevel ->
    val ans = packet { commandName = "clear_client_commands" }.wrapIntoArray()
    val existingCommands = clientCommandInvoker.getCommands()
    existingCommands.filter { it.value.visibility == Visibility.ALL_USERS || it.value.visibility == currentVisibilityLevel }.forEach { command ->
        ans.add(
            packet {
                commandName = "add_client_command"
                argsArray {
                    string (command.key)
                    commandType(command.value.commandType)
                }
            }
        )
    }
    ans
}