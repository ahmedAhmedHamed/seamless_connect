package seamless.connect.touchSimulation

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import android.provider.Settings
import android.content.Context
import android.graphics.Point
import android.text.TextUtils
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.graphics.Path
import android.util.Log

class TouchSimMain : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accessibilityService: TouchSimActivity = TouchSimActivity()

        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        setContent {
            var textvalue by remember { mutableStateOf("hello") }
            Column {
                val context = LocalContext.current
                Button(onClick = {
//                    accessibilityService.simulateTouch(500f, 500f)
//                    val result = accessibilityService.dispatchGesture(
//                        buildClick(500f, 500f),
//                        object : AccessibilityService.GestureResultCallback() {
//                            override fun onCompleted(gestureDescription: GestureDescription?) {
//                                super.onCompleted(gestureDescription)
//                                Log.d("amongus", "gesture completed")
//                            }
//
//                            override fun onCancelled(gestureDescription: GestureDescription?) {
//                                super.onCancelled(gestureDescription)
//                                Log.d("amongus", "gesture cancelled")
//                            }
//                        },
//                        null
//                    )
                    val result = accessibilityService.performGlobalAction(GLOBAL_ACTION_HOME)
                    Log.d("amongus", result.toString())
                    val x = accessibilityService.serviceInfo
                    println(x)

                }) {
                    Text("request touch permission")
                }
                Button(onClick = {textvalue += "HALLOOO"}) {
                    Text("connect to computer")
                }
            }
        }
    }

//    fun dispatch(x: Float, y: Float): Boolean {
//        val result = accessibilityService?.dispatchGesture(
//            buildClick(x, y),
//            object : GestureResultCallback() {
//                override fun onCompleted(gestureDescription: GestureDescription?) {
//                    super.onCompleted(gestureDescription)
//                    Log.d(TAG, "gesture completed")
//                }
//
//                override fun onCancelled(gestureDescription: GestureDescription?) {
//                    super.onCancelled(gestureDescription)
//                    Log.d(TAG, "gesture cancelled")
//                }
//            },
//            null
//        )
//        return result ?: false
//    }


    private fun buildClick(x: Float, y: Float): GestureDescription {
        val clickPath = Path()
        clickPath.moveTo(x, y)
        val clickStroke = GestureDescription.StrokeDescription(clickPath, 0, 1)
        val clickBuilder = GestureDescription.Builder()
        clickBuilder.addStroke(clickStroke)
        return clickBuilder.build()
    }
//
//    private fun buildSwipe(
//        startX: Float,
//        startY: Float,
//        endX: Float,
//        endY: Float,
//        duration: Long
//    ): GestureDescription {
//        val swipePath = Path()
//        swipePath.moveTo(startX, startY)
//        swipePath.lineTo(endX, endY)
//        val swipeBuilder = GestureDescription.Builder()
//        swipeBuilder.addStroke(StrokeDescription(swipePath, 0, duration))
//        return gestureBuilder.build()
//    }

    fun getMaxScreenCoordinates(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size // size.x is the max x value, size.y is the max y value
    }


    fun isAccessibilityServiceEnabled(mContext: Context): Boolean {
        val TAG = "tests"
        var accessibilityEnabled = 0
        val service: String = mContext.packageName + "/" + TouchSimActivity::class.java
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
            Log.v(TAG, "accessibilityEnabled = $accessibilityEnabled")
        } catch (e: Settings.SettingNotFoundException) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.message)
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            Log.v(TAG, "Accessibility Is Enabled")
            val settingValue: String = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    Log.v(TAG, "AccessibilityService :: $accessibilityService $service")
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        Log.v(TAG, "accessibility is switched on!")
                        return true
                    }
                }
            }
        } else {
            Log.v(TAG, "accessibility is disabled")
        }
        return false
    }

}