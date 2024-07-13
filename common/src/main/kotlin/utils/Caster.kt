package utils

import commandArgumentsAndTheirsComponents.*


val argToString = {
    arguments: ArrayList<CommandArgument> ->
    (arguments[0] as MyString).it
}
val argToCommand = {
    arguments: ArrayList<CommandArgument> ->
    argToString(arguments) to (arguments[1] as CommandTypeArgument).it
}
val argToVisibility = {
    arguments: ArrayList<CommandArgument> ->
    (arguments[0] as VisibilityArgument).it
}

val argToRoute = {
    arguments: ArrayList<CommandArgument> ->
    (arguments[0] as Route)
}

val argToVisibilityAndString = {
    arguments: ArrayList<CommandArgument> ->
    argToVisibility(arguments) to argToString(arrayListOf(arguments[1]))
}

val argToDouble = {
    arguments: ArrayList<CommandArgument> ->
    argToString(arguments).toDouble()
}

val argToTwoStrings = {
    arguments: ArrayList<CommandArgument> ->
    argToString(arguments) to argToString(arrayListOf(arguments[1]))
}

val argToLong = {
    arguments: ArrayList<CommandArgument> ->
    argToString(arguments).toLong()
}

val argToLongAndRoute = {
    arguments: ArrayList<CommandArgument> ->
    argToLong(arguments) to argToRoute(arrayListOf(arguments[1]))
}