package builders

import commandArgumentsAndTheirsComponents.Coordinates
import commandArgumentsAndTheirsComponents.Location
import commandArgumentsAndTheirsComponents.Route
import java.util.*

class RouteBuilder {
    var name: String? = null
    var coordinates: Coordinates? = null
    var from: Location? = null
    var to: Location? = null
    var distance: Double? = null
    var id: Long? = null
    var creationDate: Date = Date()
    var user_id: Long = 0

    fun build(): Route {
        val ans = Route(name!!, coordinates!!, from, to!!, distance!!)
        if (id != null) ans.setId(id!!)
        ans.setCreationDate(creationDate)
        ans.setOwner(user_id)
        return ans
    }
    fun location(init: LocationBuilder.() -> Unit) = builders.location(init)
    fun coordinates(init: CoordinatesBuilder.() -> Unit) = builders.coordinates(init)
    fun setOwner(l: Long) { user_id = l }
}