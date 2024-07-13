package utils

import commandArgumentsAndTheirsComponents.Visibility

class Condition(private var it: Visibility) {
    var token = "unlogged_user"

    fun set(v: Visibility) { it = v }
    fun get() = it
}
