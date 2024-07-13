package commandArgumentsAndTheirsComponents

import kotlinx.serialization.Serializable

@Serializable
enum class Visibility {
    LOGGED_USER,
    UNLOGGED_USER,
    ALL_USERS
}