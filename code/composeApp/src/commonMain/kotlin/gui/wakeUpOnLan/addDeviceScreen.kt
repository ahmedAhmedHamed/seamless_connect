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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable
fun TextFieldRow() {
    val textFields = remember { mutableStateListOf("", "", "", "","", "", "", "") } // Initial empty list of 8 text fields
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
                    },
                    modifier = Modifier.weight(1f).height(21.dp)
                        .focusRequester(focusRequesters[index])
//                        .background(TextFieldDefaults.textFieldColors().backgroundColor(true).value),
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

                TextFieldRow()

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    println("Text 1: ${text1.text}")
                    println("Text 2: ${text2}")
                    println("Text 3: ${text3.text}")
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

