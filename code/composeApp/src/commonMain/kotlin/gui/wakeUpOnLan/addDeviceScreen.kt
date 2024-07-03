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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun addDevicePrompt() {
    var showDialog by remember { mutableStateOf(false) }
    var text1 by remember { mutableStateOf(TextFieldValue("")) }
    var text2 by remember { mutableStateOf(TextFieldValue("")) }
    var text3 by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("Add Device") },
        text = {
            Column {
                TextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("Device Name") }
                )

                TextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = { Text("IPAddress") }
                )

                MacAddressInput(
                    value = text3,
                    onValueChange = { text3 = it },
                    label = { Text("MAC address") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Handle button click
                    // Here you can perform actions with text1, text2, text3
                    // For example, print them:
                    println("Text 1: ${text1.text}")
                    println("Text 2: ${text2.text}")
                    println("Text 3: ${text3.text}")
                    showDialog = false
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
            val newValue = formatMacAddress(AnnotatedString(it.text))
            onValueChange(TextFieldValue(text = newValue.toString()))
        },
        label = label,
        visualTransformation = MacAddressVisualTransformation()
    )
}

fun formatMacAddress(input: AnnotatedString): AnnotatedString {
    val input2 = input.toString()
    val cleanInput = input2.replace(":", "").replace("-", "").toUpperCase()
    val formatted = buildString {
        for (i in cleanInput.indices) {
            if (i > 0 && i % 2 == 0) append(':')
            append(cleanInput[i])
        }
    }
    return AnnotatedString(formatted.take(17)) // Ensure MAC address does not exceed 17 characters
}

class MacAddressVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = formatMacAddress(text)
        return TransformedText(
            text = transformed,
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset
                }
            }
        )
    }
}
