package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToLong
import java.lang.Exception
/***
 * remove_by_id id : удалить элемент из коллекции по его id
 * @author Demid0
 * @since 1.0
 */

val removeById = ClientCommand("remove_by_id", CommandType.SINGLE_ARG, Visibility.LOGGED_USER, argToLong) {
        user_id, id ->
    printToClientPacket (
        try {
            if (dbHandler.removeElement(id, user_id)) {
                collectionManager.collection.removeIf { it.getId() == id }
                "Done!"
            } else "Can't find element or it is not your element"
        } catch (e: Exception) {
            "Wrong id format."
        }
    )
}

