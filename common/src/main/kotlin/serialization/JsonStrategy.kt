package serialization

import commandArgumentsAndTheirsComponents.Route
import utils.Packet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/***
 * Обертка для json сериализации
 * @author Demid0
 * @since 1.0
 */
class JsonStrategy: Strategy {
    override fun decode(str: String, collection: MutableCollection<Route>): MutableCollection<Route> = Json.decodeFromString(str)
    override fun decode(str: String, packet: ArrayList<Packet>): ArrayList<Packet> = Json.decodeFromString(str)
    override fun encode(collection: MutableCollection<Route>) = Json.encodeToString(collection)
    override fun encode(packet: ArrayList<Packet>): String = Json.encodeToString(packet)
    override fun toString(): String = "Json"
}