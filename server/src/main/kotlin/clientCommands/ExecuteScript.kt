package clientCommands

import builders.packet
import commandArgumentsAndTheirsComponents.CommandType
import commandArgumentsAndTheirsComponents.Visibility
import utils.argToString

/***
 * execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 * @author Demid0
 * @since 1.0
 */

val executeScript = ClientCommand("execute_script", CommandType.SINGLE_ARG, Visibility.LOGGED_USER, argToString) {
        _, file_name ->
    packet {
        commandName = "read_from_file"
        string(file_name)
    }.wrapIntoArray()
}
