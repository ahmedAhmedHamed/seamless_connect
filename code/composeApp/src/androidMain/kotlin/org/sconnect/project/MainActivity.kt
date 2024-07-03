package org.sconnect.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import gui.wakeUpOnLan.addDeviceButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
//    var wakeUpOnLanCards by remember { mutableStateOf(mutableListOf<>())}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
//            wakeUpDevice(MAC,IP)
        }
        setContent {
//            WakeUpOnLanCard.wakeUpOnLanCard(ipAddress = "192.168.1.3", MAC = "00-B0-D0-63-C2-26", portNumber = 9)
            addDeviceButton()
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}