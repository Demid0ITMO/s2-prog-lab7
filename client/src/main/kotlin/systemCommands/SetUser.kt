package systemCommands

import utils.argToVisibilityAndString


val setUser = SystemCommand("set_user", argToVisibilityAndString) {
        (visibility, token) ->
    condition.token = token
    condition.set(visibility)
}