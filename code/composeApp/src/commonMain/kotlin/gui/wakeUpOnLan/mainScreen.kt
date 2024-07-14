package gui.wakeUpOnLan

import WakeUpDevice.Device
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun wakeUpOnLanScreen() {
    val devices = remember { mutableStateListOf<Device>() }
    var showAddDevicePrompt by remember { mutableStateOf(false) }

    addDeviceButton {showAddDevicePrompt = true}
    if (showAddDevicePrompt) {
        addDevicePrompt {deviceName, ipAddress, MACAddress ->
            devices.add(Device(deviceName, MACAddress, ipAddress))
            showAddDevicePrompt = false
        }
    }
    LazyColumn {
        itemsIndexed(devices) {idx, device ->
            WakeUpOnLanCard(device.deviceName, device.ipAddress, device.MAC, device.portNumber)
            if (idx < devices.size)
                Spacer(modifier = Modifier.height(5.dp))
        }
    }
}