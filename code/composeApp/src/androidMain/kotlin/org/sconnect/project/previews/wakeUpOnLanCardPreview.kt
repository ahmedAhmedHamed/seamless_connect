package org.sconnect.project.previews
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import gui.wakeUpOnLan.WakeUpOnLanCard

@Preview
@Composable
fun WakeUpOnLanCardPreview() {
    WakeUpOnLanCard(ipAddress = "192.168.1.3", MAC = "00-B0-D0-63-C2-26", portNumber = 9).show()
}

