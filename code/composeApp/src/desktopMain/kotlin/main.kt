import DeviceConnector.connectToDevice
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "code",
    ) {
        connectToDevice("192.168.1.30")
        App()
    }
}