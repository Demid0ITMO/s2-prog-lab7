package utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import commandArgumentsAndTheirsComponents.MyString
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import kotlin.collections.ArrayDeque
import kotlin.collections.HashMap

class App: KoinComponent {
    private val parseCommandAndAskArguments: parseCommandAndAskArguments by inject()
    private val scriptStack: ArrayDeque<String> by inject()
    private val readerStack: HashMap<String, BufferedReader> by inject()
    private val writerManager: WriterManager by inject()
    private val readerManager: ReaderManager by inject()
    private val nullWriterManager = WriterManager(PrintWriter(File("/dev/null")))

    fun run(readerManager: ReaderManager, writerManager: WriterManager): Packet? {
        try {
            val reader = readerManager.get()
            val writer = if (scriptStack.isEmpty()) writerManager.get() else nullWriterManager.get()
            writer.print("> ")
            writer.flush()
            val input = reader.readLine().trim().split(" ").toMutableList()
            input.removeAll(setOf("", { input.size }))
            val packet = parseCommandAndAskArguments.parse(input)
            return if (parseCommandAndAskArguments.getCommands()[packet.commandName] == null) {
                writer.print("Command not found.\n")
                writer.flush()
                null
            } else {
                packet.arguments.add(MyString(scriptStack.lastOrNull() ?: ""))
                packet
            }
        } catch (e: NullPointerException) {
            readerStack.remove(scriptStack.removeLast())
            if(scriptStack.isEmpty()) readerManager.set(BufferedReader(InputStreamReader(System.`in`)))
            return null
        }
    }

    fun setDefaultCondition (e: Exception) {
        writerManager.set(PrintWriter(System.out))
        readerManager.set(BufferedReader(InputStreamReader(System.`in`)))
        scriptStack.clear()
        readerStack.clear()
        writerManager.get().println(e.message)
        writerManager.get().flush()
    }
}