package connections.socket
import java.net.Socket

fun connectToServer(serverIp: String, port: Int) : Boolean {
    println("trying to connect...")
    return try {
        val socket = Socket(serverIp, port)
        val outputStream = socket.getOutputStream()
        outputStream.write(5)
        socket.close()
        true
    } catch (e: Exception) {
        println("connectToDevice has thrown an exception. $e")

        false
    }
}
