package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility

/***
 * clear : очистить коллекцию
 * @author Demid0
 * @since 1.0
 */
val clear = ClientCommand("clear", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        user_id, _ ->
    if (dbHandler.clearCollection(user_id)) { collectionManager.collection.removeIf { it.getOwner() == user_id } }
    printToClientPacket("Removed all your Routes")
}