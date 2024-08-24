package gui.wakeUpOnLan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun TextFieldRow(onValueChange: (x:String) -> Unit = {}) {
    val textFields = remember { mutableStateListOf("", "", "", "","", "") }
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(textFields.size) { FocusRequester() } }

    Column {
        Text(
            text = "MAC Address"
        )
        Row (modifier = Modifier.fillMaxWidth()) {

            textFields.forEachIndexed { index, text ->
                BasicTextField(
                    value = text,

                    onValueChange = { newText ->
                        if (newText.length <= 2)
                            textFields[index] = newText.uppercase()
                        if (newText.length >= 2 && index < textFields.size - 1) {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        var newMacAddress = ""
                        textFields.forEachIndexed { index, text ->
                            newMacAddress += text
                            if (index < textFields.size - 1) {
                                newMacAddress += ":"
                            }
                        }
                        onValueChange(newMacAddress)
                    },
                    modifier = Modifier.weight(1f).height(21.dp)
                        .focusRequester(focusRequesters[index])
                )
                if (index < textFields.size - 1) {
                    Text(
                        text = ":",
                    )
                }
            }
        }
    }
}



@Composable
fun addDevicePrompt(onFormSubmission: (deviceName: String, ipAddress: String, MACAddress: String) -> Unit) {
    var deviceName by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }
    var MACAddress by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {  },
        title = { Text("Add Device") },
        text = {
            Column {
                TextField(
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    label = { Text("Device Name.") }
                )

                TextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                    },
                    label = { Text("IPAddress.") }
                )

                TextFieldRow { x -> MACAddress = x }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onFormSubmission(deviceName, ipAddress, MACAddress)
                }
            )
            { Text("Submit") }
        }
    )
}


//@Composable
//fun MacAddressInput(
//    value: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    label: @Composable () -> Unit
//) {
//    TextField(
//        value = value,
//        onValueChange = {
//            val oldSelectionIdx = it.selection.min
//            val newValue = formatMacAddress(it.text, oldSelectionIdx)
//            val selectionIdx: Int = if (it.text.length < newValue.length)
//                oldSelectionIdx + 1
//            else
//                oldSelectionIdx
//
//            val selection = TextRange(selectionIdx)
//            onValueChange(TextFieldValue(text = newValue, selection = selection))
//        },
//        label = label,
//    )
//}
//
//fun formatMacAddress(input: String, selectionIdx: Int): String {
//    val cleanInput = input.replace(":", "").replace("-", "").uppercase(Locale.getDefault())
//    val formatted = buildString {
//        for (i in cleanInput.indices) {
//            if (i > 0 && i % 2 == 0) append(':')
//            append(cleanInput[i])
//        }
//        if (this.count() > 17)  deleteCharAt(selectionIdx - 1)
//        while (this.count() > 17) deleteCharAt(this.count() - 1)
//    }
//
//    return formatted
//}

