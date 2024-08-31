package connections.socket

import java.io.BufferedInputStream
import java.io.IOException
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

fun createServerSocketConnection(portNum: Int = 8121): Socket? {
    return try {
        val serverSocket = ServerSocket(portNum)

        val clientSocket = serverSocket.accept()

        println("Client connected from ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}")

        clientSocket
    } catch (e: IOException) {
        println("IOException occurred: ${e.message}")
        null
    } catch (e: SocketException) {
        println("SocketException occurred: ${e.message}")
        null
    } catch (e: BindException) {
        println("BindException occurred: ${e.message}")
        null
    } catch (e: SecurityException) {
        println("SecurityException occurred: ${e.message}")
        null
    }
}

fun listenToClient() {
    val portNum = 8121
    try {
        //Create instance of "ServerSocket" class with portNumber
        val socketClassification = ServerSocket(portNum)

        // Block the execution of the program until a client connects to the server on the port that firstSockett is listening to.
        val socket = socketClassification.accept()

        // Testing if the sockets are successfully created using DataInputStream
        val dataInputStream = BufferedInputStream(socket.getInputStream())
        val str = dataInputStream.read()

        println("Message = " + str)

        dataInputStream.close()

        socketClassification.close()
        socket.close()
    }catch (e: IOException){
        println("IOException occurred: ${e}")
    }catch (e: SocketException){
        println("SocketException occurred: ${e.message}")
    }catch (e: BindException){
        println("BindException occurred: ${e.message}")
    }catch (e: SecurityException){
        println("SecurityException occurred: ${e.message}")
    }
}
