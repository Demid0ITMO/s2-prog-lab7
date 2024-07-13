package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import java.util.NoSuchElementException
/***
 * remove_first : удалить первый элемент из коллекции
 * @author Demid0
 * @since 1.0
 */

val removeFirst = ClientCommand("remove_first", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        user_id, _ ->
    printToClientPacket (
        try {
            val element = collectionManager.collection.first()
            if (dbHandler.removeElement(element.getId(), user_id)) {
                collectionManager.collection.remove(element)
                "Done!"
            }
            else "Don't removed. Probably it isn't your element"
        }
        catch (e: NoSuchElementException) { "Collection is empty" }
    )
}
