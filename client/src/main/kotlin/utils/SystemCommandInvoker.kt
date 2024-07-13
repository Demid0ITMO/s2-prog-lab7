package utils

import systemCommands.*

class SystemCommandInvoker {
    private val commands = HashMap<String, SystemCommand<*>>()

    init {
        addCommand(printToClient)
        addCommand(addClientCommand)
        addCommand(clearClientCommands)
        addCommand(endClientSession)
        addCommand(readFromFile)
        addCommand(setUser)
    }

    fun invoke(listOfPacket: ArrayList<Packet>) {
        listOfPacket.forEach { commands[it.commandName]!!.execute(it.arguments) }
    }
    fun addCommand(systemCommand: SystemCommand<*>) {
        commands.put(systemCommand.commandName, systemCommand)
    }
}
