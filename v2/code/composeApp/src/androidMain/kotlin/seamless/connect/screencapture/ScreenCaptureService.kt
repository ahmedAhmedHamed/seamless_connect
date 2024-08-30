package seamless.connect.screencapture

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import java.io.File

class ScreenCaptureService : Service() {

    private var mediaProjection: MediaProjection? = null
    private var test = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val projectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_CANCELED)
        val data = intent?.getParcelableExtra<Intent>("data")
        val displayMetrics = resources.displayMetrics

        val imageReader: ImageReader = ImageReader.newInstance(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            PixelFormat.RGBA_8888,
            2
        )

        imageReader.setOnImageAvailableListener({ reader ->
            val image: Image? = reader.acquireLatestImage()
            println("setOnImageAvailableListener called!")
            image?.let {
                // Process the image here
//                processImage(it)
                val file = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/" + "/captured_image.png")
                saveImageToFile(it, file)
                it.close() // Don't forget to close the image to free up resources
            }
        }, null)

        if (resultCode == Activity.RESULT_OK && data != null) {
            activateNotification()
            mediaProjection = projectionManager.getMediaProjection(resultCode, data) as MediaProjection
            // Start capturing the screen here using mediaProjection
            mediaProjection?.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() {
                    super.onStop()
                    // Handle the media projection being stopped
                    println("MediaProjection stopped")
                    // Release resources if needed
//                    virtualDisplay?.release()
                }
            }, null)
            var virtualDisplay = mediaProjection!!.createVirtualDisplay(
                "ScreenCapture",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.surface,
                object : VirtualDisplay.Callback() {
                    override fun onPaused() {
                        super.onPaused()
                        // Handle display pause
                        println("Virtual display paused")
                    }

                    override fun onResumed() {
                        super.onResumed()
                        // Handle display resume
                        println("Virtual display resumed")
                    }

                    override fun onStopped() {
                        super.onStopped()
                        // Handle display stop
                        imageReader.close()
                        println("Virtual display stopped")
                    }
                }, null)
        }


        return START_NOT_STICKY
    }

    private fun processImage(image: Image) {
        test += 1
        if (test > 0)
            return
        val buffer = image.planes[0].buffer
        val pixelStride = image.planes[0].pixelStride
        val rowStride = image.planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width

        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/" + "/captured_image.png")
//        saveBitmap(bitmap, file)

        // Use the bitmap for whatever you need
        // For example, you can save it or display it in an ImageView
    }

    private fun activateNotification() {
        val notificationChannelId = "ScreenCaptureService"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Screen Capture Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Screen Capture")
            .setContentText("Capturing the screen")
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // Ensure this drawable exists
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaProjection?.stop()
    }
}
