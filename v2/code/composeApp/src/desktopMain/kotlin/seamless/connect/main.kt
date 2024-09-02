package seamless.connect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import connections.socket.createServerSocketConnection
import java.io.DataInputStream

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import java.io.ByteArrayInputStream
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

fun byteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap {
    val inputStream = ByteArrayInputStream(byteArray)
    return loadImageBitmap(inputStream)
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "code",
    ) {
        val coroutineScope = rememberCoroutineScope()
        var imageState by
        remember { mutableStateOf(loadImage("C:\\Users\\Stan\\Pictures\\potat2023.png")) }
        Image(bitmap = imageState, contentDescription = null)

        LaunchedEffect(Unit) {
            coroutineScope.launch(Dispatchers.IO) {
                val clientConnection = createServerSocketConnection()
                if (clientConnection !== null) {
                    val inputStream = DataInputStream(clientConnection.getInputStream())
                    while (true) {
                        try {
                            val length = inputStream.readInt()
                            if (length > 0) {
                                val imageByteArray = ByteArray(length)
                                inputStream.readFully(imageByteArray)
                                imageState = byteArrayToImageBitmap(imageByteArray)
                            }
                        } catch (e: Exception){
                            print("")
                        }
                    }
                }
            }
        }

    }
}

fun loadImage(filePath: String): ImageBitmap {
    return File(filePath).inputStream().buffered().use {
        loadImageBitmap(it)
    }
}
