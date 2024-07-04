package gui.wakeUpOnLan

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.util.Locale


@Composable
fun addDevicePrompt() {
    var text1 by remember { mutableStateOf(TextFieldValue("")) }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = {  },
        title = { Text("Add Device") },
        text = {
            Column {
                TextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("Device Name.") }
                )

                TextField(
                    value = text2,
                    onValueChange = {
                        text2 = it
                            },
                    label = { Text("IPAddress.") }
                )

                MacAddressInput(
                    value = text3,
                    onValueChange = { text3 = it },
                    label = { Text("MAC address.") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    println("Text 1: ${text1.text}")
                    println("Text 2: ${text2}")
                    println("Text 3: ${text3.text}")
                }
            ) {
                Text("Submit")
            }
        }
    )
}

@Composable
fun MacAddressInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: @Composable () -> Unit
) {
    TextField(
        value = value,
        onValueChange = {
            val newValue = formatMacAddress(it.text)
            val selectionIdx: Int = if (it.text.length < newValue.length)
                it.selection.min + 1
            else
                it.selection.min

            val selection = TextRange(selectionIdx)
            onValueChange(TextFieldValue(text = newValue, selection = selection))
        },
        label = label,
    )
}

fun formatMacAddress(input: String): String {
    val cleanInput = input.replace(":", "").replace("-", "").uppercase(Locale.getDefault())
    val formatted = buildString {
        for (i in cleanInput.indices) {
            if (i > 0 && i % 2 == 0) append(':')
            append(cleanInput[i])
        }
    }
    return formatted.take(17)
}

