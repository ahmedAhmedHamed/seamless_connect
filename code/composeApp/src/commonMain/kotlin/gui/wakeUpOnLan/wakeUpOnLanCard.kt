package gui.wakeUpOnLan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class WakeUpOnLanCard(private val ipAddress: String,
                      private val MAC: String,
                      private val portNumber: Int = 9) {
    @Composable
    fun show() {
        Surface (modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary) {
            Column(modifier = Modifier.clickable {
                println(ipAddress)
                println(MAC)
            }) {
                Text(text = ipAddress)
                Text(text = MAC)
                Text(text = portNumber.toString())
            }
        }
    }
}
