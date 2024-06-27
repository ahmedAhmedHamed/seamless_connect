package ConnectionReceiver
import java.io.DataInputStream
import java.io.IOException
import java.net.BindException
import java.net.ServerSocket
import java.net.SocketException

fun connectionReciever() {
    val portNum = 8121
    try {
        //Create instance of "ServerSocket" class with portNumber
        val socketClassification = ServerSocket(portNum)

        // Block the execution of the program until a client connects to the server on the port that firstSockett is listening to.
        val socket = socketClassification.accept()

        // Testing if the sockets are successfully created using DataInputStream
        val dataInputStream = DataInputStream(socket.getInputStream())
        val str = dataInputStream.readUTF()
        println("Message = " + str)

        socketClassification.close()
        socket.close()
    }catch (e: IOException){
    }catch (e: SocketException){
        println("SocketException occurred: ${e.message}")
    }catch (e: BindException){
        println("BindException occurred: ${e.message}")
    }catch (e: SecurityException){
        println("SecurityException occurred: ${e.message}")
    }
}
