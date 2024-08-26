package seamless.connect.screencapture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Build

class ScreenCaptureActivity : Activity() {

    private lateinit var mediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up MediaProjectionManager
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        // Start screen capture intent
        val captureIntent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_CODE_CAPTURE_PERM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CAPTURE_PERM && resultCode == RESULT_OK) {
            // Start the foreground service with the result data
            val serviceIntent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra("resultCode", resultCode)
                putExtra("data", data)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                print("starting with startForegroundService(serviceIntent)")
                startForegroundService(serviceIntent)
            } else {
                print("starting with startService(serviceIntent)")
                startService(serviceIntent)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CAPTURE_PERM = 1000
    }
}
