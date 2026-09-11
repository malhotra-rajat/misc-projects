#!/usr/bin/env python3
"""Run the archived NUMAD14S-New APK in a local Android emulator (no source rebuild)."""
import argparse
import os
from pathlib import Path
import subprocess
import time
from archive_apps import ROOT, launch_app

PACKAGE = 'edu.neu.madcourse.rajatmalhotra'
APK = ROOT / 'MobileAppDevelopment/NUMAD14S-New/app/build/outputs/apk/debug/app-debug.apk'


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--avd', default='Pixel_9_Pro_XL_API_35')
    parser.add_argument('--serial', help='Use an already running emulator by adb serial')
    args = parser.parse_args()
    sdk = Path(os.environ.get('ANDROID_HOME') or os.environ.get('ANDROID_SDK_ROOT') or Path.home() / 'Library/Android/sdk')
    adb, emulator = sdk / 'platform-tools/adb', sdk / 'emulator/emulator'
    if not adb.exists() or not emulator.exists():
        parser.error('Android SDK emulator and platform-tools are required; set ANDROID_HOME.')
    if not APK.exists():
        parser.error('Archived APK is missing. This launcher does not rebuild the old Gradle project.')
    devices = subprocess.check_output([str(adb), 'devices'], text=True)
    serials = [line.split()[0] for line in devices.splitlines()[1:]
               if line.startswith('emulator-') and line.endswith('\tdevice')]
    if args.serial:
        if args.serial not in serials: parser.error('--serial must name a running emulator.')
        serial = args.serial
    elif len(serials) == 1:
        serial = serials[0]
    elif len(serials) > 1:
        parser.error('Several emulators are running; select one with --serial.')
    else:
        avds = subprocess.check_output([str(emulator), '-list-avds'], text=True).splitlines()
        if args.avd not in avds: parser.error('AVD not found. Available: ' + ', '.join(avds))
        launch_app('NUMAD Android Emulator', 'local.miscprojects.numademulator', emulator,
                   ['-avd', args.avd, '-read-only', '-no-snapshot-save', '-no-boot-anim'])
        deadline = time.monotonic() + 120
        serial = None
        while time.monotonic() < deadline:
            listing = subprocess.check_output([str(adb), 'devices'], text=True)
            ready = [line.split()[0] for line in listing.splitlines()[1:]
                     if line.startswith('emulator-') and line.endswith('\tdevice')]
            if len(ready) == 1:
                serial = ready[0]
                break
            time.sleep(1)
        if serial is None: raise RuntimeError('Emulator did not become available within 120 seconds.')
    command = [str(adb), '-s', serial]
    deadline = time.monotonic() + 120
    while subprocess.check_output(command + ['shell', 'getprop', 'sys.boot_completed'], text=True).strip() != '1':
        if time.monotonic() > deadline: raise RuntimeError('Emulator boot timed out.')
        time.sleep(1)
    subprocess.run(command + ['install', '-r', str(APK)], check=True)
    subprocess.run(command + ['shell', 'am', 'start', '-W', '-n', PACKAGE + '/.MainActivity'], check=True)
    print('Opened archived NUMAD APK on', serial)
    print('This uses the preserved APK, not a newly rebuilt application.')


if __name__ == '__main__':
    main()
