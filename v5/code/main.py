import subprocess
import shlex
import sys
import tkinter as tk
from threading import Thread
from tkinter import DISABLED


class ScrollableButtonBox:
    def __init__(self, parent, device_info):
        # Create a static frame for headers (outside the canvas)
        header_frame = tk.Frame(parent)
        header_frame.pack(fill=tk.X, pady=5)

        serial_label = tk.Label(header_frame, text="Device", font=('Arial', 12, 'bold'))
        serial_label.pack(side=tk.LEFT, padx=10, expand=False)

        status_label = tk.Label(header_frame, text="Status", font=('Arial', 12, 'bold'))
        status_label.pack(side=tk.LEFT, padx=10, expand=False)

        clear_button = tk.Button(header_frame, text="Refresh", font=('Arial', 12, 'bold'), command=self.refresh)
        clear_button.pack(side=tk.RIGHT, padx=10, expand=False)

        reset_button = tk.Button(header_frame, text="clear Selection", font=('Arial', 12, 'bold'), command=self.deselect_button)
        reset_button.pack(side=tk.RIGHT, padx=10, expand=False)

        # Create a canvas for the scrollable area
        self.canvas = tk.Canvas(parent, height=150)  # Limit the height to display up to 5 rows
        self.canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        # Create a vertical scrollbar
        self.scrollbar = tk.Scrollbar(parent, orient=tk.VERTICAL, command=self.canvas.yview)
        self.scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

        # Configure canvas to use scrollbar
        self.canvas.configure(yscrollcommand=self.scrollbar.set)

        # Create an internal frame to hold the buttons inside the canvas
        self.frame = tk.Frame(self.canvas)

        # Create a window in the canvas to hold the frame
        self.canvas.create_window((0, 0), window=self.frame, anchor='nw')

        # Add buttons to the frame with device information
        self.buttons = []
        self.selected_button = None  # To track the selected button
        if device_info ==  []:
            self.refresh()
        else:
            self.add_device_info_buttons(device_info)

        # Update scroll region and configure scrolling
        self.frame.bind("<Configure>", self.on_frame_configure)

    # Update scrollable area
    def on_frame_configure(self, event):
        self.canvas.configure(scrollregion=self.canvas.bbox("all"))

    def add_device_info_buttons(self, device_info):
        for serial, status in device_info:
            button_frame = tk.Frame(self.frame)
            button_frame.pack(pady=5, fill=tk.X)

            # Create two labels: one for serial number and one for status
            serial_label = tk.Label(button_frame, text=serial)
            serial_label.pack(side=tk.LEFT, padx=10, expand=True)

            status_label = tk.Label(button_frame, text=status)
            status_label.pack(side=tk.LEFT, padx=10, expand=True)

            # Create an invisible button for click detection
            button = tk.Button(button_frame, text="Select", command=lambda s=serial, b=button_frame: self.on_button_click(s, b))
            button.pack(side=tk.RIGHT, padx=10)
            self.buttons.append(button_frame)

    def refresh(self):
        self.clear_buttons()
        process = subprocess.Popen(['dependencies/scrcpy-win64-v2.6.1/adb', 'devices'],
                                   stdout=subprocess.PIPE,
                                   stderr=subprocess.PIPE,
                                   text=True)
        stdout, stderr = process.communicate()
        device_list = []
        for line in stdout.splitlines():
            if '\t' in line:  # Each valid device line has a tab between the device ID and its status
                device_id, status = line.split('\t')
                device_list.append((device_id, status))
        self.add_device_info_buttons(device_list)

    def clear_buttons(self):
        for widget in self.frame.winfo_children():
            widget.destroy()  # Destroy each widget inside the frame
        self.buttons.clear()  # Clear the buttons list
        self.selected_button = None  # Reset the selected button

    # Function when a button is clicked
    def on_button_click(self, serial, button_frame):
        print(f"Device '{serial}' clicked!")

        # If a button is already selected, revert its appearance
        if self.selected_button:
            self.selected_button.config(bg='SystemButtonFace')  # Default background color

        # Set the new selected button's appearance
        button_frame.config(bg='lightblue')  # Change background to indicate selection
        self.selected_button = button_frame  # Update selected button

    # Function to deselect any button
    def deselect_button(self):
        if self.selected_button:
            self.selected_button.config(bg='SystemButtonFace')  # Reset color
            self.selected_button = None

    # Method to get the serial number and status from the selected button
    def get_selected_device_info(self):
        if self.selected_button:
            # Access children of the selected button's frame
            children = self.selected_button.winfo_children()

            # Assuming the order of children: serial_label, status_label, and button
            serial_number = children[0].cget("text")  # First child is the serial number label
            status = children[1].cget("text")         # Second child is the status label
            return serial_number, status
        return None, None  # If no button is selected


class MainScreen:

    def __init__(self):
        self.scrcpy_result = None
        self.root = tk.Tk()
        self.root.title("Seamless Connect")
        self.build_phone_controls()
        self.build_devices_list()

    def build_devices_list(self):

        self.device_selection = ScrollableButtonBox(self.root, [])

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
        if self.otg_button_check:
            command.append("--otg")
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
