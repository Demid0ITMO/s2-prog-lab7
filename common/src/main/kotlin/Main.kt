import builders.packet
import builders.route
import commandArgumentsAndTheirsComponents.CommandType

fun main(args: Array<String>) {
    val route = route {
        name = "Asdfg"
        coordinates = coordinates {
            x = null
            y = null
        }
        from = null
        to = location {
                x = 1
                y = 2.0f
                z = 3
                name = "sdfl"
            }
        distance = 5.0
    }
    println(route.toString())
    val packet = packet {
        commandName = "add"
        arguments = argsArray {
            string("Asdlfasldfla")
            string {
                it = "aslfdalsd"
            }
            commandType {
                it = CommandType.MIXED_ARG
            }
            string("Asdlfasldfla")

        }
    }
    println(packet.commandName)
    println(packet.arguments.size)
}
