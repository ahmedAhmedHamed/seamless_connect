package seamless.connect

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import connections.socket.createServerSocketConnection
import java.io.DataInputStream

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import java.io.ByteArrayInputStream
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

//fun displayImage(byteArray: ByteArray) {
//    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    // Display the bitmap (e.g., in an ImageView)
//    // imageView.setImageBitmap(bitmap)
//}

fun loadImageBitmap(filePath: String): ImageBitmap {
    // Step 1: Read the file as a BufferedImage using ImageIO
    val bufferedImage: BufferedImage = ImageIO.read(File(filePath))

    // Step 2: Convert the BufferedImage to an ImageBitmap
    return bufferedImage.toComposeImageBitmap()
}

fun loadImageAsByteArray(filePath: String, format: String = "png"): ByteArray {
    // Step 1: Read the file as a BufferedImage using ImageIO
    val bufferedImage: BufferedImage = ImageIO.read(File(filePath))

    // Step 2: Write the BufferedImage to a ByteArrayOutputStream
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, byteArrayOutputStream)

    // Step 3: Convert the ByteArrayOutputStream to a ByteArray
    return byteArrayOutputStream.toByteArray()
}

@Composable
fun DisplayImage(imageBitmap: ImageBitmap) {
    Image(bitmap = imageBitmap, contentDescription = null)
}

fun byteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap {
    val inputStream = ByteArrayInputStream(byteArray)
    return loadImageBitmap(inputStream)
}

@Composable
fun testComposable(imageState: ImageBitmap?) {
    println("recomposing test composable")
    if (imageState !== null) {
        Box {
            Image(bitmap = imageState, contentDescription = null)
        }
    }
}

@Composable
fun testText(text: String) {
    Text(text)
}



fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "code",
    ) {
        val coroutineScope = rememberCoroutineScope()
        var imageState by
        remember { mutableStateOf(loadImage("C:\\Users\\Stan\\Pictures\\potat2023.png")) }
        var testTextState by remember { mutableStateOf("amonugs") }
        var count by remember { mutableStateOf(0) }
        Image(bitmap = imageState, contentDescription = null)
//        val xx = loadImageAsByteArray("C:\\Users\\Stan\\Pictures\\potat.png")
//        imageState = byteArrayToImageBitmap(xx)
//        Column {
//            Text("Count: $count")
//            Button(onClick = {
//                count++
//                testTextState = "aeuw"
//                val x = loadImageAsByteArray("C:\\Users\\Stan\\Pictures\\potat2023.png")
//                imageState.value = byteArrayToImageBitmap(x)
//            }) {
//                Image(bitmap = imageState.value!!, contentDescription = null)
//            }
//        }
        LaunchedEffect(Unit) {
            coroutineScope.launch(Dispatchers.IO) {
                Thread.sleep(1000);
                imageState = loadImage("C:\\Users\\Stan\\Pictures\\potat.png")
            }
        }
    }
}



//        testComposable(imageState.value)

//        val xxx = loadImageAsByteArray("C:\\Users\\Stan\\Downloads\\60d24e134b12f15b003a2aee_PC 2.png")
//        imageState.value = byteArrayToImageBitmap(xxx)
// Example: Receiving image byte array in the background
//        testState = 2
//            LaunchedEffect(Unit) {
//                coroutineScope.launch(Dispatchers.IO) {
//                    val clientConnection = createServerSocketConnection()
//                    if (clientConnection !== null) {
//                        val inputStream = DataInputStream(clientConnection.getInputStream())
//                        while (true) {
//                            val length = inputStream.readInt()
//                            if (length > 0) {
//                                val imageByteArray = ByteArray(length)
//                                inputStream.readFully(imageByteArray)
//                                withContext(Dispatchers.Main) {
//                                    imageState.value = byteArrayToImageBitmap(imageByteArray)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            while (true)
//        LaunchedEffect(imageState) {
//        }
//        val x = loadImageAsByteArray("C:\\Users\\Stan\\Pictures\\potat2023.png")
//        imageState = byteArrayToImageBitmap(x)


//        Thread.sleep(1000);
//        val xx = loadImageAsByteArray("C:\\Users\\Stan\\Pictures\\potat.png")
//        imageState = byteArrayToImageBitmap(xx)
//        imageState?.let {
//            Image(bitmap = it, contentDescription = null)
//        }
//            imageState.value?.let {
//                println("in imagestate.value let")
//                DisplayImage(imageBitmap = it)
//            }

//    println("hello world!")
//    val clientConnection = createServerSocketConnection()
//    if (clientConnection !== null) {
//        val inputStream = DataInputStream(clientConnection.getInputStream())
//        try {
//            while (true) {
//                // Read the length of the image
////                print("")
//                try {
//
//                    val length = inputStream.readInt()
//                    println()
//                    println(length)
//                    println()
//                    if (length > 0) {
//                        // Read the image data
//                        val imageByteArray = ByteArray(length)
//                        inputStream.readFully(imageByteArray)
//                        // Process the image (e.g., display it)
//                        println(imageByteArray.size)
//                        println("___")
////                    onImageReceived(imageByteArray)
//                    }
//                } catch (e: Exception) {
////                    println("exception thrown while reading stream")
//                }
//
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            clientConnection.close()
////            serverSocket.close()
//        }
//    }

@Composable
fun App() {
    var image by remember { mutableStateOf(loadImage("C:\\Users\\Stan\\Pictures\\potat.png")) }

    Image(bitmap = image, contentDescription = null)

    // Button to change the image
    Button(onClick = {
        image = loadImage("C:\\Users\\Stan\\Pictures\\potat2023.png")
    }) {
        Text("Change Image")
    }
}

fun loadImage(filePath: String): ImageBitmap {
    return File(filePath).inputStream().buffered().use {
        loadImageBitmap(it)
    }
}

fun maooin() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
