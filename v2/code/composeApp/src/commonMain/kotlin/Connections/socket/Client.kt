package connections.socket
import java.net.Socket

fun createClientSocketConnection(serverIp: String, port: Int) : Socket? {
    println("trying to connect...")
    return try {
        val socket = Socket(serverIp, port)
//        val outputStream = socket.getOutputStream()
//        outputStream.write(5)
//        socket.close()
        socket
    } catch (e: Exception) {
        println("connectToDevice has thrown an exception. $e")
        null
    }
}
