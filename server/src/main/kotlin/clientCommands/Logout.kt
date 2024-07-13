package clientCommands

import builders.packet
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import commandArgumentsAndTheirsComponents.VisibilityArgument
import utils.ServerMessageHandler

val logout = ClientCommand("logout", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        user_id, _ ->
    clients.remove(tokens[user_id])
    ServerMessageHandler.updateUser(tokens[user_id]!!, user_id)
    tokens.remove(user_id)
    val ans = packet {
        commandName = "set_user"
        visibility(Visibility.UNLOGGED_USER)
        string("unlogged_user")
    }.wrapIntoArray()
    ans.addAll(checkout.execute(arrayListOf(VisibilityArgument(Visibility.UNLOGGED_USER)), user_id))
    ans
}
