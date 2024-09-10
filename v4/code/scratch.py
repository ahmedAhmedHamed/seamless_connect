import cv2
import subprocess
import numpy as np

def stream_phone_screen():
    try:
        # Start the adb process (this produces the screen recording output)
        adb_command = ["adb", "exec-out", "screenrecord", "--output-format=h264", "-"]
        adb_process = subprocess.Popen(adb_command, stdout=subprocess.PIPE)

        # Start the ffplay process (this consumes the adb output)
        ffplay_command = ["ffplay", "-framerate", "60", "-probesize", "32", "-sync", "video", "-"]
        ffplay_process = subprocess.Popen(ffplay_command, stdin=adb_process.stdout)

        # This ensures that ffplay consumes adb's stdout in real-time
        adb_process.stdout.close()

        # Wait for the adb process and ffplay to finish
        adb_process.wait()
        ffplay_process.wait()

    except Exception as e:
        print(f"Error: {e}")

def stream_phone_screen_with_ffmpeg_and_opencv():
    try:
        # FFmpeg command to decode adb screenrecord output (H.264) into raw frames
        ffmpeg_command = [
            'ffmpeg',
            '-framerate', '60',
            '-probesize', '32',
             'video',
            '-i', 'pipe:0',  # Read from stdin (pipe)
            '-f', 'rawvideo',
            '-pix_fmt', 'bgr24',  # Convert to BGR for OpenCV
            '-vf', 'scale=720:1280',  # Set resolution
            'pipe:1'  # Output to stdout (pipe)
        ]

        # Start adb process to stream screen using adb exec-out
        adb_command = ["adb", "exec-out", "screenrecord", "--output-format=h264", "-"]
        adb_process = subprocess.Popen(adb_command, stdout=subprocess.PIPE)

        # Start FFmpeg process to decode the H.264 stream
        ffmpeg_process = subprocess.Popen(ffmpeg_command, stdin=adb_process.stdout, stdout=subprocess.PIPE, bufsize=10**8)

        # Frame resolution (change according to your phone screen's resolution)
        width = 720
        height = 1280
        frame_size = width * height * 3  # 3 bytes per pixel for BGR

        while True:
            # Read raw frame data from FFmpeg
            frame_bytes = ffmpeg_process.stdout.read(frame_size)

            # If frame_bytes is empty, end the loop (end of stream)
            if not frame_bytes:
                break

            # Convert raw bytes into a numpy array
            frame = np.frombuffer(frame_bytes, np.uint8).reshape((height, width, 3))

            # Display the frame using OpenCV
            cv2.imshow('Phone Screen', frame)

            # Press 'q' to quit the window
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        # Cleanup
        adb_process.stdout.close()
        adb_process.terminate()
        ffmpeg_process.terminate()
        cv2.destroyAllWindows()

    except Exception as e:
        print(f"Error: {e}")

def stream_phone_screen_with_opencv():
    try:
        # Start adb process to stream screen using adb exec-out
        adb_command = ["adb", "exec-out", "screenrecord", "--output-format=h264", "-"]
        adb_process = subprocess.Popen(adb_command, stdout=subprocess.PIPE)

        # OpenCV VideoCapture object to read from adb stream
        cap = cv2.VideoCapture(adb_process.stdout)

        if not cap.isOpened():
            print("Error: Could not open video stream.")
            return

        while True:
            # Capture frame-by-frame from the stream
            ret, frame = cap.read()
            if not ret:
                print("Error: Can't receive frame (stream end?). Exiting ...")
                break

            # Display the resulting frame
            cv2.imshow('Phone Screen', frame)

            # Press 'q' to quit the window
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        # When done, release everything
        cap.release()
        cv2.destroyAllWindows()
        adb_process.stdout.close()
        adb_process.terminate()

    except Exception as e:
        print(f"Error: {e}")



def pull_down_top_bar():
    adb_command = ['adb','shell', 'input', 'swipe', '500', '0', '500', '1000']
    adb_process = subprocess.Popen(adb_command)


if __name__ == "__main__":
    stream_phone_screen()
