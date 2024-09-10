import sys
import shlex
import pygame
import subprocess
import numpy as np
import threading


height = 720
width = 1280
frame_ready = True
ffmpeg_process = None
adb_process = None

pygame.init()
screen = pygame.display.set_mode((height, width))
#
# input_device = "/dev/input/event3"
#
#
# def adb_shell(cmd):
#     """Run adb shell command."""
#     subprocess.run(f"adb shell {cmd}", shell=True)

def start_adb_streaming():
    global ffmpeg_process
    global adb_process
    adb_command = ["adb", "exec-out", "screenrecord", "--bit-rate", "1M", "--output-format=h264", "-"]
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
            print("lmb pressed")
        elif event.type == pygame.MOUSEBUTTONUP:
            print("lmb released")


def get_phone_frame():
    global ffmpeg_process
    global frame_ready
    frame_data = b''
    while len(frame_data) < frame_size:
        chunk = ffmpeg_process.stdout.read(frame_size - len(frame_data))
        if not chunk:
            break
        frame_data += chunk
    return frame_data


def render_phone_frame(frame_data):
    global screen
    frame = np.frombuffer(frame_data, dtype=np.uint8).reshape((height, width, 3))
    pygame_surface = pygame.surfarray.make_surface(frame)
    screen.blit(pygame_surface, (0, 0))
    pygame.display.flip()

running = True
while running:
    try:
        frame_size = height * width * 3  # Adjust if needed
        start_adb_streaming()
        frame_data = get_phone_frame()
        render_phone_frame(frame_data)
    except:
        pass

pygame.quit()
ffmpeg_process.terminate()
adb_process.terminate()
