package commandArgumentsAndTheirsComponents

import kotlinx.serialization.Serializable

/***
 * Класс, экземляр которого содержится в экземпляре коллекции в качестве поля
 *
 * @author Demid0
 * @since 1.0
 * @param x Координата x
 * @param y Координата y
 * @param z Координата z
 * @param name Название локации
 */
@Serializable
class Location(private var x: Int?,
               private var y: Float,
               private var z: Long,
               private var name: String
) {
    override fun toString(): String {
        return "\n\tname: $name\n\tx: $x, y: $y, z: $z"
    }

    fun getX() = x
    fun getY() = y
    fun getZ() = z
    fun getName() = name
}
