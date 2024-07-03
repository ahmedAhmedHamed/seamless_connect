package gui.wakeUpOnLan
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun wakeUpOnLanCard(ipAddress: String, MAC: String, portNumber: String) {
    Column {
        Text(text = ipAddress)
        Text(text = MAC)
        Text(text = portNumber)
    }
}