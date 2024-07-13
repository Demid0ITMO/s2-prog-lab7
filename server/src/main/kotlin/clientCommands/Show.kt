package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility

/***
 * show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
 * @author Demid0
 * @since 1.0
 */

val show = ClientCommand("show", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        user_id, _ ->
    val collection = collectionManager.collection
    printToClientPacket (
        if (collection.isEmpty()) "Collection is empty :("
        else {
            var out = "Collection:\n"
            collection.sortedBy { it.getName() }.forEach { out += it.toString() + "\n" }
            out.dropLast(1)
        }
    )
}
