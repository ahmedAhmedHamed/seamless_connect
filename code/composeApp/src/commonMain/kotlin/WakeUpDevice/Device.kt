package WakeUpDevice

data class Device(var deviceName: String,
                  var MAC: String,
                  var ipAddress: String,
                  var portNumber: Int = 9)