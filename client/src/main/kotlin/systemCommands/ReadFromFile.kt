package systemCommands

import exceptions.RecursionException
import exceptions.SystemCommandInvocationException
import utils.argToString
import java.io.*

val readFromFile = SystemCommand("read_from_file", argToString) {
        argument ->
    try {
        var fileName = argument
        val file = File(fileName)
        val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
        fileName = file.absolutePath
        if (scriptStack.contains(fileName)) throw RecursionException()
        readerManager.set(reader)
        if (!scriptStack.contains(fileName)) {
            scriptStack.addLast(fileName)
            readerStack[fileName] = reader
        }
    } catch (e: SystemCommandInvocationException) {
        throw e
    } catch (_: FileNotFoundException) {
        throw SystemCommandInvocationException("File not found")
    } catch (_: Exception) {
        throw SystemCommandInvocationException()
    }
}
