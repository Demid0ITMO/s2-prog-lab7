package clientCommands

import builders.printToClientPacket
import utils.*
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility

/***
 * help : вывести справку по доступным командам
 * @author Demid0
 * @since 1.0
 */

val help = ClientCommand("help", CommandType.VISIBILITY_ARG, Visibility.ALL_USERS, argToVisibility) {
        _, currentVisibilityLevel ->
    val commands = clientCommandInvoker.getCommands()
    printToClientPacket(
        if (commands.isEmpty()) "No commands"
        else {
            var out = "You can use this commands:\n"
            commands.toSortedMap().filter { it.value.visibility == Visibility.ALL_USERS || it.value.visibility == currentVisibilityLevel }.forEach { out += it.key + "\n" }
            out.dropLast(1)
        }
    )
}
