import sys
import shlex
import pygame
import subprocess
import numpy as np

height = 720
width = 1280
ffmpeg_process = None
adb_process = None

pygame.init()
screen = pygame.display.set_mode((height, width))

def start_adb_streaming():
    global ffmpeg_process
    global adb_process
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


def handle_events():
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.MOUSEBUTTONDOWN:
            pass
            # process = subprocess.Popen(shlex.split())
        elif event.type == pygame.MOUSEBUTTONUP:
            pass


def get_phone_frame():
    global ffmpeg_process
    frame_data = b''
    while len(frame_data) < frame_size:
        chunk = ffmpeg_process.stdout.read(frame_size - len(frame_data))
        if not chunk:
            break
        frame_data += chunk
    return frame_data


running = True
while running:
    try:
        frame_size = height * width * 3  # Adjust if needed
        start_adb_streaming()
        frame_data = get_phone_frame()
        frame = np.frombuffer(frame_data, dtype=np.uint8).reshape((height, width, 3))
        pygame_surface = pygame.surfarray.make_surface(frame)

        screen.blit(pygame_surface, (0, 0))
        pygame.display.flip()
    except:
        pass

# Clean up
pygame.quit()
ffmpeg_process.terminate()
adb_process.terminate()
