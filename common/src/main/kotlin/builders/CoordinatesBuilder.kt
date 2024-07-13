package builders

import commandArgumentsAndTheirsComponents.Coordinates

class CoordinatesBuilder {
    var x: Float? = null
    var y: Int? = null

    fun build() = Coordinates(x, y)
}
