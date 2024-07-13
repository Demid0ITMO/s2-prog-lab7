package serialization

import commandArgumentsAndTheirsComponents.Route
import utils.Packet

/***
 * Обертка для разных типов сериализации
 * @author Demid0
 * @since 1.0
 */
interface Strategy {
    fun decode(str: String, collection: MutableCollection<Route>): MutableCollection<Route>
    fun encode(collection: MutableCollection<Route>): String
    fun decode(str: String, packet: ArrayList<Packet>): ArrayList<Packet>
    fun encode(packet: ArrayList<Packet>): String
    override fun toString(): String
}