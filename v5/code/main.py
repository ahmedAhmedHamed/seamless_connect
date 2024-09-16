import subprocess
import sys
import tkinter as tk
from threading import Thread
from components import ScrollableButtonBox


class MainScreen:

    def __init__(self):
        self.device_selection_box: ScrollableButtonBox or None = None
        self.scrcpy_result = None
        self.root = tk.Tk()
        self.root.title("Seamless Connect")
        self.build_phone_controls()
        self.build_devices_list()

    def build_devices_list(self):
        self.device_selection_box = ScrollableButtonBox(self.root)

    def build_phone_controls(self):
        self.launch_bridge_mirroring_btn = tk.Button(self.root, text="launch bridge mirroring", command=self.launch_scrcpy,
                                                     font=("Arial", 12))
        self.launch_bridge_mirroring_btn.pack(pady=10)

        self.otg_button_check = tk.BooleanVar()
        self.otg_mode_check = tk.Checkbutton(self.root, text="OTG mode", variable=self.otg_button_check)
        self.otg_mode_check.pack(pady=10)

    def get_scrcpy_command(self):
        command = ["dependencies/scrcpy-win64-v2.6.1/scrcpy.exe"]
        if self.otg_button_check.get():
            command.append("--otg")
        if self.device_selection_box.get_selected_device_info()[0]:
            device_serial, _ = self.device_selection_box.get_selected_device_info()
            command.append(f'--serial={device_serial}')
        return command

    def non_blocking_scrcpy(self):
        scrcpy_process = subprocess.Popen(self.get_scrcpy_command(), stdout=sys.stdout)
        self.scrcpy_result = scrcpy_process.wait()
        self.launch_bridge_mirroring_btn.config(state=tk.NORMAL)

    def launch_scrcpy(self):
        self.launch_bridge_mirroring_btn.config(state=tk.DISABLED)
        bridge_mirroring_thread = Thread(target=self.non_blocking_scrcpy)
        bridge_mirroring_thread.start()

    def launch(self):
        self.root.mainloop()


if __name__ == "__main__":
    MainScreen().launch()
