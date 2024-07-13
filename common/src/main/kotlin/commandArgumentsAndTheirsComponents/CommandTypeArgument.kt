package commandArgumentsAndTheirsComponents

import kotlinx.serialization.Serializable

@Serializable
class CommandTypeArgument(val it: CommandType): CommandArgument()