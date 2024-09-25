import subprocess


def adb_get_devices():
    adb_devices_process = subprocess.Popen(['dependencies/scrcpy-win64-v2.6.1/adb', 'devices'],
                                           stdout=subprocess.PIPE,
                                           stderr=subprocess.PIPE,
                                           text=True)
    stdout, stderr = adb_devices_process.communicate()
    device_list = []
    for line in stdout.splitlines():
        if '\t' in line:  # Each valid device line has a tab between the device ID and its status
            device_id, status = line.split('\t')
            device_list.append((device_id, status))
    return device_list
