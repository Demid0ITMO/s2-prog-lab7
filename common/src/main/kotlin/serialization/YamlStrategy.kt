package serialization

import commandArgumentsAndTheirsComponents.Route
import com.charleskorn.kaml.Yaml
import utils.Packet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

/***
 * Обертка для yaml сериализации
 * @author Demid0
 * @since 1.0
 */
class YamlStrategy : Strategy {
    override fun decode(str: String, collection: MutableCollection<Route>): MutableCollection<Route> = Yaml.default.decodeFromString(str)
    override fun decode(str: String, packet: ArrayList<Packet>): ArrayList<Packet> = Yaml.default.decodeFromString(str)
    override fun encode(collection: MutableCollection<Route>) = Yaml.default.encodeToString(collection)
    override fun encode(packet: ArrayList<Packet>): String = Yaml.default.encodeToString(packet)
    override fun toString() = "Yaml"
}