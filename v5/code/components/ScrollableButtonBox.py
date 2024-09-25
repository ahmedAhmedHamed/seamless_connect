import tkinter as tk
import subprocess
from engine import adb_get_devices


class ScrollableButtonBox:
    def __init__(self, parent, device_info=None):
        # Create a static frame for headers (outside the canvas)
        if device_info is None:
            device_info = []
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
        self.device_list_canvas = tk.Canvas(parent, height=150)  # Limit the height to display up to 5 rows
        self.device_list_canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        # Create a vertical scrollbar
        self.scrollbar = tk.Scrollbar(parent, orient=tk.VERTICAL, command=self.device_list_canvas.yview)
        self.scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

        # Configure canvas to use scrollbar
        self.device_list_canvas.configure(yscrollcommand=self.scrollbar.set)

        # Create an internal frame to hold the buttons inside the canvas
        self.frame = tk.Frame(self.device_list_canvas)

        # Create a window in the canvas to hold the frame
        self.device_list_canvas.create_window((0, 0), window=self.frame, anchor='nw')

        # Add buttons to the frame with device information
        self.devices = []
        self.selected_device = None  # To track the selected button
        if device_info ==  []:
            self.refresh()
        else:
            self.add_device_info_buttons(device_info)

        # Update scroll region and configure scrolling
        self.frame.bind("<Configure>", self.on_frame_configure)

    # Update scrollable area
    def on_frame_configure(self, event):
        self.device_list_canvas.configure(scrollregion=self.device_list_canvas.bbox("all"))

    def add_device_info_buttons(self, device_info):
        for serial, status in device_info:
            button_frame = tk.Frame(self.frame)
            button_frame.pack(pady=5, fill=tk.X)

            # Create two labels: one for serial number and one for status
            serial_label = tk.Label(button_frame, text=serial)
            serial_label.pack(side=tk.LEFT, padx=10, expand=True)

            status_label = tk.Label(button_frame, text=status)
            status_label.pack(side=tk.LEFT, padx=10, expand=True)

            select_device_btn = tk.Button(button_frame, text="Select",
                                          command=lambda s=serial,
                                          b=button_frame: self.on_button_click(s, b))
            select_device_btn.pack(side=tk.RIGHT, padx=10)
            self.devices.append(button_frame)

    def refresh(self):
        self.clear_buttons()
        device_list = adb_get_devices()
        self.add_device_info_buttons(device_list)

    def clear_buttons(self):
        for widget in self.frame.winfo_children():
            widget.destroy()
        self.devices.clear()
        self.selected_device = None

    # Function when a button is clicked
    def on_button_click(self, serial, button_frame):
        print(f"Device '{serial}' clicked!")

        # If a button is already selected, revert its appearance
        if self.selected_device:
            self.selected_device.config(bg='SystemButtonFace')  # Default background color

        # Set the new selected button's appearance
        button_frame.config(bg='lightblue')  # Change background to indicate selection
        self.selected_device = button_frame  # Update selected button

    def deselect_button(self):
        if self.selected_device:
            self.selected_device.config(bg='SystemButtonFace')  # Reset color
            self.selected_device = None

    # Method to get the serial number and status from the selected button
    def get_selected_device_info(self):
        if self.selected_device:
            # Access children of the selected button's frame
            children = self.selected_device.winfo_children()

            # Assuming the order of children: serial_label, status_label, and button
            serial_number = children[0].cget("text")  # First child is the serial number label
            status = children[1].cget("text")         # Second child is the status label
            return serial_number, status
        return None, None  # If no button is selected
