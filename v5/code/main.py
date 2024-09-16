import tkinter as tk
from components import ScrollableButtonBox
from engine import alaunch_scrcpy


class MainScreen:

    def __init__(self):
        self.root = tk.Tk()
        self.root.title("Seamless Connect")
        self.build_phone_controls()
        self.device_selection_box = ScrollableButtonBox(self.root)
        self.otg_button_check = tk.BooleanVar()

    def build_phone_controls(self):
        self.launch_bridge_mirroring_btn = tk.Button(self.root, text="launch bridge mirroring", command=self.launch_scrcpy,
                                                     font=("Arial", 12))
        self.launch_bridge_mirroring_btn.pack(pady=10)

        otg_mode_check = tk.Checkbutton(self.root, text="OTG mode", variable=self.otg_button_check)
        otg_mode_check.pack(pady=10)

    def get_scrcpy_flags(self):
        command = []
        if self.otg_button_check.get():
            command.append("--otg")
        if self.device_selection_box.get_selected_device_info()[0]:
            device_serial, _ = self.device_selection_box.get_selected_device_info()
            command.append(f'--serial={device_serial}')
        return command

    def on_mirroring_end(self, result_code=0):
        self.launch_bridge_mirroring_btn.config(state=tk.NORMAL)

    def launch_scrcpy(self):
        self.launch_bridge_mirroring_btn.config(state=tk.DISABLED)
        alaunch_scrcpy(self.get_scrcpy_flags(), self.on_mirroring_end)

    def launch(self):
        self.root.mainloop()


if __name__ == "__main__":
    MainScreen().launch()
