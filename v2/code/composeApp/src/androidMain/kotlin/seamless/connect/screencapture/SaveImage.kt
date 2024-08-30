//package seamless.connect.screencapture
//
//import android.media.Image
//import android.util.Log
//import java.nio.ByteBuffer
//import java.io.File
//import java.io.FileOutputStream
//
//
//fun imageToByteArray(image: Image): ByteArray? {
//    return try {
//        val buffer: ByteBuffer = image.planes[0].buffer
//        val byteArray = ByteArray(buffer.remaining())
//        buffer.get(byteArray)
//        byteArray
//    } catch (e: Exception) {
//        Log.e("ImageError", "Failed to convert RGB_565 image to byte array", e)
//        null
//    }
//}
//
//fun saveImageToFile(image: Image, file: File) {
//    val byteArray = imageToByteArray(image)
//    FileOutputStream(file).use { outputStream ->
//        outputStream.write(byteArray)
//    }
//}
