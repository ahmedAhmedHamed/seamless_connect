import java.net.ServerSocket
import java.io.IOException
import java.net.BindException
import java.net.SocketException
import java.io.DataInputStream

fun main() {
    val portNum = 8181
    try {
        //Create instance of "ServerSocket" class with portNumber
        val primarySockett = ServerSocket(portNum)

        // Block the execution of the program until a client connects to the server on the port that firstSockett is listening to.
        val finalSockett = primarySockett.accept()

            // Testing if the sockets are successfully created using DataInputStream
        val dis = DataInputStream(finalSockett.getInputStream())
        val str = dis.readUTF()
        println("Message = " + str)

        primarySockett.close()
        finalSockett.close()
    }catch (e: IOException){
        println("IOException occurred: ${e.message}")
    }catch (e: SocketException){
        println("SocketException occurred: ${e.message}")
    }catch (e: BindException){
        println("BindException occurred: ${e.message}")
    }catch (e: SecurityException){
        println("SecurityException occurred: ${e.message}")
    }
}
