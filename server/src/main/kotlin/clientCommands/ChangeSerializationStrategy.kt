package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToString

/***
 * change_serialization_strategy strategy : поменять тип сериализации
 * @author Demid0
 * @since 1.0
 */

val changeSerializationStrategy = ClientCommand("change_serialization_strategy", CommandType.SINGLE_ARG,Visibility.LOGGED_USER, argToString) {
    _, argument ->
    printToClientPacket(
        try {
            var newType = argument
            newType += "strategy"
            serializator.changeStrategy(serializator.getStrategy(newType)!!)
            "Changed"
        } catch (e: NullPointerException) {
            var out = "You can use this strategies:\n"
            for (strategy in serializator.getStrategies()) {
                out += strategy.value.toString() + "\n"
            }
            "Unknown serialization strategy\n${out.dropLast(1)}"
        }
    )
}