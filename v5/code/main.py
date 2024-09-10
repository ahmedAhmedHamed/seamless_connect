import subprocess
import shlex
import sys



scrcpy_process = subprocess.Popen("dependencies/scrcpy-win64-v2.6.1/scrcpy.exe", stdout=sys.stdout)

scrcpy_process.wait()