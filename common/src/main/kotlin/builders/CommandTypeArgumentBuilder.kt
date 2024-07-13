package builders

import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.CommandTypeArgument

class CommandTypeArgumentBuilder {
    var it: CommandType? = null
    fun build() = CommandTypeArgument(it!!)
}