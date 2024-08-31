package seamless.connect.screencapture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

fun saveImageToFile(image: Image, file: File) {
    try {
        // Extract buffer from the image
        val buffer: ByteBuffer = image.planes[0].buffer
        val width = image.width
        val height = image.height
        val pixelStride = image.planes[0].pixelStride
        val rowStride = image.planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width

        // Create a Bitmap with the proper width and height
        val bitmap = Bitmap.createBitmap(
            width + rowPadding / pixelStride,
            height,
            Bitmap.Config.ARGB_8888
        )

        // Copy pixels from the ByteBuffer to the Bitmap
        bitmap.copyPixelsFromBuffer(buffer)

        // Save the Bitmap to a file
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace() // Log or handle the error as needed
    } finally {
        image.close() // Always close the Image after processing
    }
}

