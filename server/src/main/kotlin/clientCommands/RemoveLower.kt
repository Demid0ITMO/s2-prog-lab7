package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Route
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToRoute

/***
 * remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный
 * @author Demid0
 * @since 1.0
 */

val removeLower = ClientCommand("remove_lower", CommandType.OBJECT_ARG, Visibility.LOGGED_USER, argToRoute) {
        user_id, route ->
    val temp: ArrayList<Route> = arrayListOf()
    collectionManager.collection.forEach { if (it.getDistance() < route.getDistance()) temp.add(it) }
    temp.forEach { if(dbHandler.removeElement(it.getId(), user_id)) collectionManager.collection.remove(it) }
    printToClientPacket("Done!")
}
