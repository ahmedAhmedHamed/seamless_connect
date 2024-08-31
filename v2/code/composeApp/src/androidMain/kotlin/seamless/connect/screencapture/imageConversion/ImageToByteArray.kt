package seamless.connect.screencapture.imageConversion

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.media.Image
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

fun imageToByteArray(image: Image): ByteArray {
    val buffer: ByteBuffer = image.planes[0].buffer
    val width = image.width
    val height = image.height
    val pixelStride = image.planes[0].pixelStride
    val rowStride = image.planes[0].rowStride
    val rowPadding = rowStride - pixelStride * width
    val bitmap = Bitmap.createBitmap(
        width + rowPadding / pixelStride,
        height,
        Bitmap.Config.ARGB_8888
    )
    bitmap.copyPixelsFromBuffer(buffer)

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}
