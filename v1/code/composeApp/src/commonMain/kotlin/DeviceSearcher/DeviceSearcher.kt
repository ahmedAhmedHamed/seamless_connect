package DeviceSearcher
import java.net.InetAddress
import java.net.NetworkInterface

fun searchForDevices(): Array<String> {
    val localAddresses = getLocalIPAddress()
    val reachableAddresses = mutableListOf<String>()
    for (localAddress in localAddresses) {
        val subnet = localAddress.substringBeforeLast('.')

        for (i in 0..20) {
            val host = "$subnet.$i"
            if (host != localAddress && isReachable(host)) {
                reachableAddresses.add(host)
            }
        }
    }

    return reachableAddresses.toTypedArray()
}

fun getLocalIPAddress(): MutableList<String> {
    val myInterfaces = mutableListOf<String>()
    val interfaces = NetworkInterface.getNetworkInterfaces()
    for (networkInterface in interfaces) {
        if (networkInterface.isLoopback || !networkInterface.isUp) {
            continue
        }
        val addresses = networkInterface.inetAddresses
        for (inetAddress in addresses) {
            if (inetAddress.isSiteLocalAddress) {
                myInterfaces.add(inetAddress.hostAddress)
            }
        }
    }
    return myInterfaces
}

fun isReachable(host: String): Boolean {
    return try {
        InetAddress.getByName(host).isReachable(100)
    } catch (e: Exception) {
        false
    }
}
