package seamless.connect
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.PixelFormat
//import android.media.ImageReader
//import android.media.MediaCodec
//import android.media.MediaFormat
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.os.Bundle
//import android.hardware.display.DisplayManager
//import android.hardware.display.VirtualDisplay
//import java.io.OutputStream
//import java.net.Socket
//
//class ScreenCaptureActivity : Activity() {
//
//    private lateinit var mediaProjectionManager: MediaProjectionManager
//    private var mediaProjection: MediaProjection? = null
//    private var virtualDisplay: VirtualDisplay? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Set up MediaProjectionManager
//        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
//
//        // Start screen capture intent
//        val captureIntent = mediaProjectionManager.createScreenCaptureIntent()
//        startActivityForResult(captureIntent, REQUEST_CODE_CAPTURE_PERM)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_CODE_CAPTURE_PERM && resultCode == RESULT_OK) {
//            startScreenCapture(resultCode, data)
//        }
//    }
//
//    private fun startScreenCapture(resultCode: Int, data: Intent) {
//        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
//        val displayMetrics = resources.displayMetrics
//        val screenDensity = displayMetrics.densityDpi
//        val width = displayMetrics.widthPixels
//        val height = displayMetrics.heightPixels
//
//        // Set up ImageReader to capture the screen
//        val imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
//        virtualDisplay = mediaProjection?.createVirtualDisplay(
//            "ScreenCapture",
//            width, height, screenDensity,
//            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//            imageReader.surface, null, null
//        )
//
//        // Start a thread to capture and stream the screen
//        Thread {
//            while (true) {
//                encodeAndStreamScreenCapture(imageReader)
//            }
//        }.start()
//    }
//
//    private fun encodeAndStreamScreenCapture(imageReader: ImageReader) {
//        val image = imageReader.acquireLatestImage() ?: return
//        val planes = image.planes
//
//        // Prepare MediaCodec for encoding (you would need to set up the MediaFormat accordingly)
//        val codec = MediaCodec.createEncoderByType("video/avc")
//        val mediaFormat = MediaFormat.createVideoFormat("video/avc", image.width, image.height)
//        codec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
//
//        codec.start()
//
//        // Feed the planes data into the codec (this is highly simplified)
//        // Note: Actual encoding involves proper handling of buffers and format specifics
//        val inputBufferIndex = codec.dequeueInputBuffer(-1)
//        if (inputBufferIndex >= 0) {
//            val inputBuffer = codec.getInputBuffer(inputBufferIndex)
//            inputBuffer?.put(planes[0].buffer)
//            codec.queueInputBuffer(inputBufferIndex, 0, planes[0].buffer.remaining(), 0, 0)
//        }
//
//        val bufferInfo = MediaCodec.BufferInfo()
//        val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0)
//        if (outputBufferIndex >= 0) {
//            val outputBuffer = codec.getOutputBuffer(outputBufferIndex)
//            val encodedData = ByteArray(bufferInfo.size)
//            outputBuffer?.get(encodedData)
//
//            streamEncodedData(encodedData)
//
//            codec.releaseOutputBuffer(outputBufferIndex, false)
//        }
//
//        image.close()
//    }
//
//    private fun streamEncodedData(encodedData: ByteArray) {
//        val socket = Socket("your-computer-ip", PORT)
//        val outputStream: OutputStream = socket.getOutputStream()
//        outputStream.write(encodedData)
//        outputStream.flush()
//        socket.close()
//    }
//
//    companion object {
//        private const val REQUEST_CODE_CAPTURE_PERM = 1000
//        private const val PORT = 12345
//    }
//}
