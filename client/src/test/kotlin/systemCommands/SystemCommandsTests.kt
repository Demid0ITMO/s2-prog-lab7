package systemCommands

import commandArgumentsAndTheirsComponents.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import utils.clientKoinModule
import java.io.File
import kotlin.test.assertEquals

class SystemCommandsTests {

    @Test
    fun addClientCommandTest() {
        val command = addClientCommand
        val newCommandName = "test_command"
        val x = command.parseCommandAndAskArguments.getCommands().size + 1
        command.execute(arrayListOf(MyString(newCommandName), CommandTypeArgument(CommandType.NO_ARG)))
        assertEquals(command.parseCommandAndAskArguments.getCommands().size, x)
        assertEquals(command.parseCommandAndAskArguments.getCommands()[newCommandName], CommandType.NO_ARG)
    }

    @Test
    fun clearClientCommandTest() {
        val command = clearClientCommands
        command.execute(ArrayList())
        assertEquals(command.parseCommandAndAskArguments.getCommands().size, 0)
    }

    @Test
    fun readFromFile() {
        val command = readFromFile
        val fileName = "/home/demid/Desktop/Itmo/programming/Lab7/testScript"
        val file = File(fileName)
        command.execute(arrayListOf(MyString(fileName)))
        assert(command.scriptStack.contains(file.absolutePath))
        assert(command.readerStack.contains(fileName))
    }

    @Test
    fun setUser() {
        val command = setUser
        val token = "ASDlvkdmfbaav-[sdgvlwekrbwev3145"
        val visibility = Visibility.ALL_USERS
        command.execute(arrayListOf(VisibilityArgument(visibility), MyString(token)))
        assertEquals(command.condition.token, token)
        assertEquals(command.condition.get(), visibility)
    }


    @BeforeEach
    fun beforeAll() {
        startKoin { modules(clientKoinModule) }
    }

    @AfterEach
    fun afterAll() {
        stopKoin()
    }
}