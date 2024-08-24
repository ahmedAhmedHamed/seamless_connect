package gui.wakeUpOnLan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WakeUpOnLanCard(deviceName: String, ipAddress: String, MAC: String, portNumber: Int = 9,
                    onclick: () -> Unit = {}) {
    Surface (modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary) {
        Column(modifier = Modifier.clickable {
            onclick()
        }) {
            Text(text = deviceName)
            Text(text = ipAddress)
            Text(text = MAC)
            Text(text = portNumber.toString())
        }
    }
}

