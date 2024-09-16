import subprocess
import sys
from threading import Thread


def launch_scrcpy(flags=None, callback=lambda *args, **kwargs: None):
	"""
	:param flags: flags to be passed to the scrcpy process
	:param callback: called after process returns, takes one parameter, for the process result
	callback defaults to a lambda that takes any arg and does nothing
	:return:
	"""
	if flags is None:
		flags = []
	command = ["dependencies/scrcpy-win64-v2.6.1/scrcpy.exe"]
	for flag in flags:
		command.append(flag)
	scrcpy_process = subprocess.Popen(command, stdout=sys.stdout)
	scrcpy_result = scrcpy_process.wait()
	callback(scrcpy_result)


def alaunch_scrcpy(flags=None, callback=lambda *args, **kwargs: None):
	bridge_mirroring_thread = Thread(target=launch_scrcpy, args=(flags, callback))
	bridge_mirroring_thread.start()
