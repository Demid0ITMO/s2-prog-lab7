package exceptions


open class SystemCommandInvocationException(message: String): Exception(message) {
    constructor() : this("")
}

class RecursionException: SystemCommandInvocationException("No way! Recursion")
