package clientCommands

import builders.packet
import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import commandArgumentsAndTheirsComponents.VisibilityArgument
import utils.ClientAssistant
import utils.ServerMessageHandler
import utils.argToTwoStrings
import java.util.*


val signUp = ClientCommand("sign_up", CommandType.TWO_STRINGS_ARG, Visibility.UNLOGGED_USER, argToTwoStrings) {
        user_id, (clientName, password) ->
    val new_user_id = dbHandler.setUser(clientName, password)
    if (new_user_id == (-1).toLong()) return@ClientCommand printToClientPacket("User already exists")
    val token = tokenizer.md5(clientName + password + Date().time.toString())
    clients[token] = ClientAssistant(new_user_id)
    tokens[new_user_id] = token
    ServerMessageHandler.updateUser(token, user_id)
    val ans = packet {
        commandName = "set_user"
        visibility(Visibility.LOGGED_USER)
        string(token)
    }.wrapIntoArray()
    ans.addAll(checkout.execute(arrayListOf(VisibilityArgument(Visibility.LOGGED_USER)), new_user_id))
    return@ClientCommand ans
}
