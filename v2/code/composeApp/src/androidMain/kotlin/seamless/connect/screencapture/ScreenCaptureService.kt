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
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import connections.socket.createClientSocketConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import java.net.Socket

/**
 * TODO:-
 * notes: computer has the socket server
 *      but is the one who consumes information
 * - create a socket connection with computer(server)
 * - every time an image is generated:
 *  -- send:
 *   --- image size
 *   --- image bytes
 * - variables maintained:
 *  -- socket
 *  -- input stream
 * client side:
 * - create socket connection with phone(client)
 * - every time an int N is received:
 *  -- read the next N bytes and put them in a bytearray
 *  -- show that image on the screen
 */

class ScreenCaptureService : Service() {

    private var mediaProjection: MediaProjection? = null
    private var test = 0
    private val serviceJob = Job() // TODO free
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var socket: Socket
    private lateinit var outputStream: OutputStream // TODO free


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var tempSocket: Socket?

        serviceScope.launch {
            tempSocket = createClientSocketConnection("192.168.1.5") // TODO free
            socket = tempSocket!!
            outputStream = socket.getOutputStream()
        }
        val projectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_CANCELED)
        val data = intent?.getParcelableExtra<Intent>("data")
        val displayMetrics = resources.displayMetrics

        val imageReader: ImageReader = setupImageReader(displayMetrics)
        setOnImageAvailableListener(imageReader)

        if (resultCode == Activity.RESULT_OK && data != null) {
            activateNotification()
            mediaProjection = projectionManager.getMediaProjection(resultCode, data) as MediaProjection
            mediaProjection?.registerCallback(getMediaProjectionCallback(), null)
            // TODO if media projection is null, fail
            var virtualDisplay = mediaProjection!!.createVirtualDisplay(
                "ScreenCapture",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.surface,
                getVirtualDisplayCallback(imageReader), null)
            // TODO if virtualDisplay is not working, fail
        }


        return START_NOT_STICKY
    }

    private fun setOnImageAvailableListener(imageReader: ImageReader) {
        // this might be better to not be on the main thread
        imageReader.setOnImageAvailableListener({ reader ->
            val image: Image? = reader.acquireLatestImage()
            image?.let {
                test += 1
                if (test % 30 == 0) {
                    if (::outputStream.isInitialized) {
                        println("sending image over socket")
                        serviceScope.launch {
                            sendImageToServer(it, outputStream)
                        }
                    }
                } else {
                it.close()
                }
            }
        }, null)
    }

    private fun getVirtualDisplayCallback(imageReader: ImageReader): VirtualDisplay.Callback {
        return object : VirtualDisplay.Callback() {
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
        }
    }

    private fun getMediaProjectionCallback(): MediaProjection.Callback {
        return object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                println("MediaProjection stopped")
//                    virtualDisplay?.release() // TODO properly release virtualDisplay
            }
        }
    }

    private fun setupImageReader(displayMetrics: DisplayMetrics): ImageReader {
        val imageReader: ImageReader = ImageReader.newInstance(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            PixelFormat.RGBA_8888,
            60
        )
        return imageReader
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
