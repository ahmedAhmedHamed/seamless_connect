package seamless.connect

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import java.io.InputStream

fun streamPhoneScreenWithFFplay() {
    try {
        // First, start the adb exec-out command
        val adbCommand = arrayOf(
            "adb", "exec-out", "screenrecord", "--output-format=h264", "-"
        )
        val adbProcess = ProcessBuilder(*adbCommand).start()

        // Get the output stream from adb, which is the screen stream
        val adbInputStream: InputStream = adbProcess.inputStream

        // Then, start the ffplay command to play the stream
        val ffplayCommand = arrayOf(
            "ffplay", "-framerate", "60", "-probesize", "32", "-sync", "video", "-"
        )
        val ffplayProcess = ProcessBuilder(*ffplayCommand)
            .redirectErrorStream(true)
            .start()

        // Write the adb output to ffplay's input
        val ffplayOutputStream = ffplayProcess.outputStream

        // Pipe the adb stream to ffplay's input stream
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (adbInputStream.read(buffer).also { bytesRead = it } != -1) {
            ffplayOutputStream.write(buffer, 0, bytesRead)
            ffplayOutputStream.flush()
        }

        // Close streams when done
        adbInputStream.close()
        ffplayOutputStream.close()

        // Wait for both processes to finish
        adbProcess.waitFor()
        ffplayProcess.waitFor()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "code",
//    ) {
//        App()
//    }
    streamPhoneScreenWithFFplay()
}