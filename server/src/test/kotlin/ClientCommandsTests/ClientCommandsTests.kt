package ClientCommandsTests

import builders.route
import clientCommands.*
import commandArgumentsAndTheirsComponents.MyString
import commandArgumentsAndTheirsComponents.Route
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import utils.DBHandler
import utils.serverKoinModule
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.assertEquals

class ClientCommandsTests {
    private val route = route {
        name = "abooba"
        coordinates = coordinates {
            x = null
            y = null
        }
        from = null
        to = location {
            x = 1423
            y = 1234.0f
            z = 345
            name = "gelendzhik"
        }
        distance = 4.0
    }
    private val anotherRoute = route {
        name = "asdfdfbsdfoba"
        coordinates = coordinates {
            x = null
            y = null
        }
        from = location {
            x = 132012
            y = 34.0f
            z = 34264
            name = "chertanovo"
        }
        to = location {
            x = 14
            y = 1234.0f
            z = 6798564
            name = "gek"
        }
        distance = 50.0
    }
    @Test
    fun addCommandTest() {
        val command = add
        val x = command.dbHandler.downloadCurrentCollection().size
        command.execute(arrayListOf(route), 1)
        assert(command.collectionManager.collection.contains(route))
        val collection = command.dbHandler.downloadCurrentCollection()
        assert(collection.size == x + 1)
    }

    @Test
    fun addIfMaxCommandTest() {
        val command = addIfMax
        val x = command.dbHandler.downloadCurrentCollection().size
        command.execute(arrayListOf(route), 1)
        assert(command.collectionManager.collection.size == x)
        command.execute(arrayListOf(anotherRoute), 1)
        assert(command.collectionManager.collection.size == x)
    }

    @Test
    fun changeCollectionTypeCommand() {
        val command = changeCollectionType
        command.execute(arrayListOf(MyString("linkedblockingqueue")), 1)
        assertEquals(command.collectionManager.collection::class, LinkedBlockingQueue<Route>()::class)
    }


    @BeforeEach
    fun beforeEach() {
        val testDBHandler = module {
            single { DBHandler("jdbc:postgresql://localhost:3566/postgres", "postgres", "1234") }
        }
        startKoin {
            modules(testDBHandler, serverKoinModule)
        }
    }

    @AfterEach
    fun afterEach() {
        val command = clear
        command.execute(arrayListOf(), 1)
        stopKoin()
    }
}