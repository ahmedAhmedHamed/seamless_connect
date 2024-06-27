package DeviceConnector
import java.io.PrintWriter
import java.net.Socket

fun connectToDevice(device: String) : Boolean {
    val port = 8121
    println("from connect")

    try {
        val socket = Socket(device, port)
        val outputStream = socket.getOutputStream()
        val streamWriter = PrintWriter(outputStream, true)

        streamWriter.println("hello from the other device")

        streamWriter.close()
        socket.close()

        return true
    } catch (e: Exception) {
        println("doesn't work $e")

        return false
    }

}