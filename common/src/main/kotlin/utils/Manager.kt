package utils

import java.io.BufferedReader
import java.io.PrintWriter

/***
 * Класс для обертки Readers и Writers
 * @author Demid0
 * @since 1.0
 */
abstract class Manager<T>(private var obj: T) {
    fun get() = obj
    fun set(obj: T) {
        this.obj = obj
    }
}

/***
 * Класс для BufferedReader
 * @author Demid0
 * @since 1.0
 */
class ReaderManager(obj: BufferedReader): Manager<BufferedReader>(obj)

/***
 * Класс для PrintWriter
 * @author Demid0
 * @since 1.0
 */
class WriterManager(obj: PrintWriter): Manager<PrintWriter>(obj)
