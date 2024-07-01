package WakeUpDevice

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun wakeUpDevice(macAddress: String, ipAddress: String, port: Int = 9) {
    try {
        val macAddressBytes = macAddress.split(":").map { it.toInt(16).toByte() }.toByteArray()

        val packet = ByteArray(6 + 16 * macAddressBytes.size)

        for (i in 0 until 6) {
            packet[i] = 0xFF.toByte()
        }

        for (i in 6 until packet.size) {
            packet[i] = macAddressBytes[(i - 6) % macAddressBytes.size]
        }


        val address = InetAddress.getByName(ipAddress) // TODO: need to change this name
        val socket = DatagramSocket()
        val datagramPacket = DatagramPacket(packet, packet.size, address, port)
        socket.send(datagramPacket)
        socket.close()

        println("Success packet sent to: $ipAddress")
    } catch (e: Exception) {
        println("Error couldn't wake the device: ${e.message}")
    }
}