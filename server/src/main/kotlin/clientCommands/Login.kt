package clientCommands

import builders.packet
import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import commandArgumentsAndTheirsComponents.VisibilityArgument
import utils.ClientAssistant
import utils.Packet
import utils.ServerMessageHandler
import utils.argToTwoStrings
import java.util.*
import kotlin.collections.ArrayList


val login = ClientCommand("login", CommandType.TWO_STRINGS_ARG, Visibility.UNLOGGED_USER, argToTwoStrings) {
        _, (clientName, password) ->
    val ans: ArrayList<Packet>
    val connected_user_id = dbHandler.checkUser(clientName, password)
    if (connected_user_id != (-1).toLong()) {
        val token = tokenizer.md5(clientName+password+Date().time.toString())
        clients[token] = ClientAssistant(connected_user_id)
        tokens[connected_user_id] = token
        ServerMessageHandler.updateUser(token, connected_user_id)
        ans = packet {
            commandName = "set_user"
            visibility(Visibility.LOGGED_USER)
            string(token)
        }.wrapIntoArray()
        ans.addAll(checkout.execute(arrayListOf(VisibilityArgument(Visibility.LOGGED_USER)), connected_user_id))
    }
    else {
        ans = printToClientPacket("Wrong username or password")
    }
    ans
}
