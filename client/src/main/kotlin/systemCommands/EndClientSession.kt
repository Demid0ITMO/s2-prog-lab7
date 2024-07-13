package systemCommands

import kotlin.system.exitProcess

val endClientSession = SystemCommand("exit", {} ) {
    exitProcess(0)
}