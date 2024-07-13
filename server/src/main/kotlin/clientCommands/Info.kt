package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility

/***
 * info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 * @author Demid0
 * @since 1.0
 */

val info = ClientCommand("info", CommandType.NO_ARG, Visibility.LOGGED_USER, {}) {
        _, _ ->
    printToClientPacket(
        "Information about collection:" +
                "\n\tType: ${collectionManager.collection.javaClass.simpleName}" +
                "\n\tSize: ${collectionManager.collection.size}" +
                "\nInfo about system:" +
                "\n\tSerialization strategy: ${serializator.getChosenStrategy().toString()}"
    )
}
