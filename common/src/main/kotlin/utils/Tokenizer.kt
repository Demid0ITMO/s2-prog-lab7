package utils

import java.lang.StringBuilder
import java.security.MessageDigest

class Tokenizer {
    private fun hashing(string: String, type: String) : String {
        val bytes = MessageDigest.getInstance(type).digest(string.toByteArray())
        return with(StringBuilder()) {
            bytes.forEach { append(String.format("%02X", it)) }
            toString().lowercase()
        }
    }
    fun md5(input: String): String = hashing(input, "MD5")
}