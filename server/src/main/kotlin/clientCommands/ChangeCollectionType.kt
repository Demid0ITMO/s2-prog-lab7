package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToString

/***
 * change_collection_type type : поменять тип коллекции
 * @author Demid0
 * @since 1.0
 */


val changeCollectionType = ClientCommand("change_collection_type", CommandType.SINGLE_ARG, Visibility.LOGGED_USER, argToString) {
        _, newType ->
    printToClientPacket(
        try {
            collectionManager.changeType(newType)
            "Changed"
        } catch (e: Exception) {
            var out = "You can use this types:\n"
            for (type in collectionManager.getSupportedCollectionTypes()) {
                out += type.key +"\n"
            }
            "Unsupported collection type\n${out.dropLast(1)}"
        }
    )
}