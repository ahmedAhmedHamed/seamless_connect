package seamless.connect.screencapture

import android.media.Image
import java.io.OutputStream
import java.net.Socket
import seamless.connect.screencapture.imageConversion.imageToByteArray

fun sendImageToServer(image: Image, outputStream: OutputStream) {
    val imageByteArray: ByteArray = imageToByteArray(image)
    outputStream.write(imageByteArray.size)
    outputStream.write(imageByteArray)
    outputStream.flush()
}
