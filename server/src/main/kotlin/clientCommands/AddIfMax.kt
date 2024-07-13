package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToRoute

/***
 * add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
 * @author Demid0
 * @since 1.0
 */

val addIfMax = ClientCommand("add_if_max", CommandType.OBJECT_ARG, Visibility.LOGGED_USER, argToRoute) {
        user_id, route ->
    for (element in collectionManager.collection) {
        if (element.getDistance() >= route.getDistance()) {
            printToClientPacket("I didn't add it")
        }
    }
    add.execute(arrayListOf(route), user_id)
}