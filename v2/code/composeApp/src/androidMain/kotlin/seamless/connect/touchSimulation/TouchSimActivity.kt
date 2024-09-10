package seamless.connect.touchSimulation

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import seamless.connect.R

class TouchSimActivity : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("SERVICE CONNECTED!!!")

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            flags = AccessibilityServiceInfo.DEFAULT
            notificationTimeout = 100
            packageNames = arrayOf("com.example.package")
        }

        serviceInfo = info

    }

    override fun onInterrupt() {
    }


    fun simulateTouch(x: Float, y: Float) {
        val gestureBuilder = GestureDescription.Builder()
        val path = Path().apply { moveTo(x, y) }
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        val z = dispatchGesture(gestureBuilder.build(), null, null)
        println(z)
    }
}
