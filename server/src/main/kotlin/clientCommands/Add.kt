package clientCommands

import builders.*
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToRoute

/***
 * add {element} : добавить новый элемент в коллекцию
 * @author Demid0
 * @since 1.0
 */

val add = ClientCommand("add", CommandType.OBJECT_ARG, Visibility.LOGGED_USER, argToRoute) {
        user_id, objectArg ->
    val route_id = dbHandler.addElement(objectArg, user_id)
    if (route_id > -1) {
        objectArg.setId(route_id)
        objectArg.setOwner(user_id)
        collectionManager.collection.add(objectArg)
        printToClientPacket("Done")
    } else {
        printToClientPacket("Something went wrong")
    }
}