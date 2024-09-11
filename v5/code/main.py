import subprocess
import shlex
import sys
import tkinter as tk
from threading import Thread
from tkinter import DISABLED

class MainScreen:

    def __init__(self):
        self.root = tk.Tk()
        self.root.title("Tkinter Example")
        self.label = tk.Label(self.root, text="Hello, Tkinter!", font=("Arial", 14))
        self.label.pack(pady=20)
        self.button = tk.Button(self.root, text="launch bridge mirroring", command=self.launch_scrcpy, font=("Arial", 12))
        self.button.pack(pady=10)
        self.button2 = tk.Button(self.root, text="update_label", command=self.update_label, font=("Arial", 12))
        self.button2.pack(pady=10)
        self.scrcpy_result = None

    def update_label(self):
        self.label.config(text="Button Clicked!")

    def non_blocking_scrcpy(self):
        scrcpy_process = subprocess.Popen("dependencies/scrcpy-win64-v2.6.1/scrcpy.exe", stdout=sys.stdout)
        self.scrcpy_result = scrcpy_process.wait()
        self.button.config(state=tk.NORMAL)

    def launch_scrcpy(self):
        self.button.config(state=tk.DISABLED)
        task_thread = Thread(target=self.non_blocking_scrcpy)
        task_thread.start()

    def launch(self):
        self.root.mainloop()


if __name__ == "__main__":
    MainScreen().launch()
