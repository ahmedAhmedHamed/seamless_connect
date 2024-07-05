package gui.wakeUpOnLan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import java.util.Locale


@Composable
fun addDevicePrompt() {
    var text1 by remember { mutableStateOf(TextFieldValue("")) }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf(TextFieldValue("")) }

        Column {
//                TextField(
//                    value = text1,
//                    onValueChange = { text1 = it },
//                    label = { Text("Device Name.") }
//                )
//
//                TextField(
//                    value = text2,
//                    onValueChange = {
//                        text2 = it
//                            },
//                    label = { Text("IPAddress.") }
//                )

            TextFieldRow()

//                MacAddressInput(
//                    value = text3,
//                    onValueChange = { text3 = it },
//                    label = { Text("MAC address.") }
//                )
        }
}

@Composable
fun TextFieldRow() {
    val textFields = remember { mutableStateListOf("", "", "", "", "", "", "", "") } // Initial empty list of 8 text fields

    Row (modifier = Modifier.fillMaxWidth()) {
        textFields.forEachIndexed { index, text ->
            TextField(
                value = text,
                onValueChange = { newText ->
                    textFields[index] = newText
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Characters
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
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
            val oldSelectionIdx = it.selection.min
            val newValue = formatMacAddress(it.text, oldSelectionIdx)
            val selectionIdx: Int = if (it.text.length < newValue.length)
                oldSelectionIdx + 1
            else
                oldSelectionIdx

            val selection = TextRange(selectionIdx)
            onValueChange(TextFieldValue(text = newValue, selection = selection))
        },
        label = label,
    )
}

fun formatMacAddress(input: String, selectionIdx: Int): String {
    val cleanInput = input.replace(":", "").replace("-", "").uppercase(Locale.getDefault())
    val formatted = buildString {
        for (i in cleanInput.indices) {
            if (i > 0 && i % 2 == 0) append(':')
            append(cleanInput[i])
        }
        if (this.count() > 17)  deleteCharAt(selectionIdx - 1)
        while (this.count() > 17) deleteCharAt(this.count() - 1)
    }

    return formatted
}

