"""Package local demo executables so macOS can identify and focus their windows."""
from pathlib import Path
import plistlib
import shlex
import subprocess

ROOT = Path(__file__).resolve().parents[1]


def launch_app(name, bundle_id, executable, arguments=(), environment=None, cwd=None):
    build = ROOT / '.archive-build'
    contents = build / (name + '.app') / 'Contents'
    (contents / 'MacOS').mkdir(parents=True, exist_ok=True)
    (contents / 'Info.plist').write_bytes(plistlib.dumps({
        'CFBundleName': name, 'CFBundleDisplayName': name,
        'CFBundleIdentifier': bundle_id, 'CFBundleVersion': '1',
        'CFBundlePackageType': 'APPL', 'CFBundleExecutable': 'launch',
        'NSHighResolutionCapable': True,
    }))
    script = ['#!/bin/sh', 'set -eu']
    if cwd:
        script.append('cd ' + shlex.quote(str(cwd)))
    for key, value in (environment or {}).items():
        if not key.replace('_', '').isalnum():
            raise ValueError('Invalid environment key')
        script.append('export ' + key + '=' + shlex.quote(str(value)))
    script.append('exec ' + shlex.join([str(executable), *map(str, arguments)])
                  + ' >> ' + shlex.quote(str(build / (name + '.log'))) + ' 2>&1')
    launcher = contents / 'MacOS/launch'
    launcher.write_text('\n'.join(script) + '\n')
    launcher.chmod(0o755)
    subprocess.run(['open', str(contents.parent)], check=True)
    print('Opened', contents.parent)
    return contents.parent
