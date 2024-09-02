package seamless.connect.screencapture

import android.media.Image
import java.io.OutputStream
import java.net.Socket
import seamless.connect.screencapture.imageConversion.imageToByteArray
import java.nio.ByteBuffer

fun sendImageToServer(image: Image, outputStream: OutputStream) {
    val imageByteArray: ByteArray = imageToByteArray(image)
    val byteArray = ByteBuffer.allocate(4).putInt(imageByteArray.size).array()
    image.close()
    outputStream.write(byteArray)
    outputStream.write(imageByteArray)
    outputStream.flush()
}
