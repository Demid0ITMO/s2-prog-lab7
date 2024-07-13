package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility

/***
 * print_field_descending_distance : вывести значения поля distance всех элементов в порядке убывания
 * @author Demid0
 * @since 1.0
 */

val printFieldDescendingDistance = ClientCommand("print_field_descending_distance", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        _, _ ->
    val collection = collectionManager.collection.sortedByDescending { it.getDistance() }
    printToClientPacket(
        if (collection.isEmpty()) "Collection is empty."
        else {
            var out = "Collection:\n"
            collection.forEach { out += "${it.getDistance()} " }
            out
        }
    )
}
