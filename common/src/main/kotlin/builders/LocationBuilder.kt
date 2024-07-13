package builders

import commandArgumentsAndTheirsComponents.Location

class LocationBuilder {
    var x: Int? = null
    var y: Float? = null
    var z: Long? = null
    var name: String? = null

    fun build() = Location(x, y!!, z!!, name!!)
}