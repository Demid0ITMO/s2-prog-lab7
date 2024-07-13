package utils

import clientCommands.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/***
 * Класс, вызывающий команды
 * @author Demid0
 * @since 1.0
 * @param commands
 * Поддерживаемые типы комманд
 */
class ClientCommandInvoker: KoinComponent {
    private val commands = HashMap<String, ClientCommand<*>>()
    private val tokens: HashMap<Long, String> by inject()

    init {
        addCommand(add)
        addCommand(addIfMax)
        //addCommand(changeCollectionType)
        //addCommand(changeSerializationStrategy)
        //addCommand(checkout)
        addCommand(clear)
        addCommand(countByDistance)
        addCommand(countLessThanDistance)
        addCommand(executeScript)
        addCommand(exit)
        addCommand(help)
        addCommand(info)
        addCommand(login)
        addCommand(logout)
        addCommand(printFieldDescendingDistance)
        addCommand(removeById)
        addCommand(removeFirst)
        addCommand(removeLower)
        addCommand(show)
        addCommand(signUp)
        addCommand(updateId)
    }

    fun invoke(listOfPackets: ArrayList<Packet>, user_id: Long): ArrayList<Packet> {
        val ans = ArrayList<Packet>()
        listOfPackets.forEach { packet ->
            val command = commands[packet.commandName]!!
            val out = command.execute(packet.arguments, user_id)
            out.forEach { it.token = tokens[user_id].toString() }
            ans.addAll(out)
        }
        return ans
    }
    fun addCommand (clientCommand: ClientCommand<*>) {
        commands.put(clientCommand.commandName, clientCommand)
    }
    fun getCommands() = commands

}