import sys

import pygame
import subprocess
import numpy as np
import cv2

height = 720
width = 1280
# Initialize Pygame
pygame.init()
screen = pygame.display.set_mode((height, width))

# Start the adb command to get the H.264 stream
adb_command = ["adb", "exec-out", "screenrecord", "--output-format=h264", "-"]
adb_process = subprocess.Popen(adb_command, stdout=subprocess.PIPE)

# Start ffmpeg to decode H.264 stream
ffmpeg_command = [
    "ffmpeg",
    "-i", "pipe:0",  # Input from stdin
    "-f", "rawvideo",  # Output format
    "-pix_fmt", "rgb24",  # Pixel format
    "-"
]
ffmpeg_process = subprocess.Popen(ffmpeg_command, stdin=adb_process.stdout, stdout=subprocess.PIPE, stderr=sys.stdout)


# Loop to read and display frames
running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    # Read the header to get the frame size
    frame_size = height * width * 3  # Adjust if needed

    # Read the frame data in chunks
    frame_data = b''
    while len(frame_data) < frame_size:
        chunk = ffmpeg_process.stdout.read(frame_size - len(frame_data))
        if not chunk:
            break
        frame_data += chunk

    if len(frame_data) < frame_size:
        print("Not enough data for a full frame")
        continue

    # Convert to a Pygame surface
    frame = np.frombuffer(frame_data, dtype=np.uint8).reshape((height, width, 3))
    cv2.imwrite('frame.png', frame)
    # running = False
    pygame_surface = pygame.surfarray.make_surface(frame)

    # Render the frame
    screen.blit(pygame_surface, (0, 0))
    pygame.display.flip()

# Clean up
pygame.quit()
ffmpeg_process.terminate()
adb_process.terminate()
