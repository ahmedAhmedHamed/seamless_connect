package DeviceConnector
import java.net.Socket

fun connectToDevice(device: String) : Boolean {
    val port = 8121
    println("trying to connect...")

    try {
        val socket = Socket(device, port)
        val outputStream = socket.getOutputStream()
        outputStream.write(5)
        socket.close()
        return true
    } catch (e: Exception) {
        println("connectToDevice has thrown an exception. $e")

        return false
    }

}