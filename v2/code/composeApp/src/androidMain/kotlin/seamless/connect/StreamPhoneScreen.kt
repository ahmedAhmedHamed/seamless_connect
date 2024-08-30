package seamless.connect
//
//import android.app.Activity
//import android.media.projection.MediaProjectionManager
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import android.media.projection.MediaProjection
//
//class test : Activity() {
//
//    lateinit var mediaProjection : MediaProjection
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
//        var mediaProjection : MediaProjection
//
//        val startMediaProjection = registerForActivityResult(
//            StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                mediaProjection = mediaProjectionManager
//                    .getMediaProjection(result.resultCode, result.data!!)
//            }
//        }
//
//        startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
//
//    }
//}
//
//
