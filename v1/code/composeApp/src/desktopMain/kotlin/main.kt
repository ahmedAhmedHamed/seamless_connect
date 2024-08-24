
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import gui.wakeUpOnLan.addDevicePrompt

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "code",
    ) {
        addDevicePrompt()
    }
}
