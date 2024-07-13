package utils

import kotlinx.serialization.Serializable
import commandArgumentsAndTheirsComponents.CommandArgument

@Serializable
open class Packet(val commandName: String, val arguments: ArrayList<CommandArgument>) {
    var token: String = "unlogged_user"
    var user_id: Long = 0
    constructor() : this("command", arrayListOf())
    fun wrapIntoArray(): ArrayList<Packet> = arrayListOf(this)
}