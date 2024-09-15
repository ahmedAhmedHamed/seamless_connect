import subprocess
import sys
import tkinter as tk
from threading import Thread
from components import ScrollableButtonBox


class MainScreen:

    def __init__(self):
        self.device_selection: ScrollableButtonBox or None = None
        self.scrcpy_result = None
        self.root = tk.Tk()
        self.root.title("Seamless Connect")
        self.build_phone_controls()
        self.build_devices_list()

    def build_devices_list(self):
        self.device_selection = ScrollableButtonBox(self.root)

    def build_phone_controls(self):
        self.button = tk.Button(self.root, text="launch bridge mirroring", command=self.launch_scrcpy,
                                font=("Arial", 12))
        self.button.pack(pady=10)

        self.otg_button_check = tk.BooleanVar()
        self.button2 = tk.Checkbutton(self.root, text="OTG mode", variable=self.otg_button_check)
        self.button2.pack(pady=10)

    def update_label(self):
        self.label.config(text="Button Clicked!")

    def get_scrcpy_command(self):
        command = ["dependencies/scrcpy-win64-v2.6.1/scrcpy.exe"]
        if self.otg_button_check.get():
            command.append("--otg")
        if self.device_selection.get_selected_device_info()[0]:
            device_serial, _ = self.device_selection.get_selected_device_info()
            command.append(f'--serial={device_serial}')
        return command

    def non_blocking_scrcpy(self):
        scrcpy_process = subprocess.Popen(self.get_scrcpy_command(), stdout=sys.stdout)
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
