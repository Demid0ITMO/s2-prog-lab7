package clientCommands

import builders.printToClientPacket
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToDouble

/***
 * count_by_distance distance : вывести количество элементов, значение поля distance которых равно заданному
 * count_less_than_distance distance : вывести количество элементов, значение поля distance которых меньше заданного
 * @author Demid0
 * @since 1.0
 */
class CountDistance(
    commandName: String,
    val compare: (a: Double, b: Double) -> Boolean
) : ClientCommand<Double>(commandName, CommandType.SINGLE_ARG, Visibility.LOGGED_USER, argToDouble, {
        _, distance ->
    printToClientPacket(
        try {
            val counter = collectionManager.collection.filter { compare(it.getDistance(), distance) }.count()
            "$counter element(s)"
        } catch (e: java.lang.Exception) {
            "Wrong distance format"
        }
    )
})

val countByDistance = CountDistance("count_by_distance") { a, b -> a == b}
val countLessThanDistance = CountDistance("count_less_than_distance") { a, b -> a < b}