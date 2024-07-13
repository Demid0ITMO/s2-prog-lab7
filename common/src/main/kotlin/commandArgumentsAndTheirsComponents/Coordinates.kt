package commandArgumentsAndTheirsComponents

import kotlinx.serialization.Serializable

/***
 * Класс, экземляр которого содержится в экземпляре коллекции в качестве поля
 * @author Demid0
 * @since 1.0
 * @param x Координата x
 * @param y Координата y
 */
@Serializable
class Coordinates(
    private var x: Float?,
    private var y: Int?
) {
    override fun toString(): String {
        return "x: $x, y: $y"
    }
    fun getX() = x
    fun getY() = y
}
