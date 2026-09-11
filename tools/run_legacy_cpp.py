#!/usr/bin/env python3
"""Run the original Alienated Alien / Restaurant Billing through SDL_bgi on macOS."""
import argparse
import hashlib
from pathlib import Path
import re
import shlex
import subprocess
import tarfile
import urllib.request
import zipfile
import io
from archive_apps import launch_app, ROOT

BUILD = ROOT / '.archive-build/legacy-cpp'
VENDOR = ROOT / '.archive-build/sdl2-bgi'
URL = 'https://downloads.sourceforge.net/project/sdl-bgi/SDL2_bgi-3.0.4.tar.gz'
SHA256 = '272ddbc0ffc1a9f1311cd669b75a8e301a29a848c89522b9552fa7f4a92a3474'
SOURCES = {'alien': 'School/AlienatedAlien/11GAME.CPP',
           'restaurant': 'HighSchoolProject - Restaurant Billing/Source Code/12PRO.CPP'}
NAMES = {'alien': 'Alienated Alien', 'restaurant': 'Restaurant Billing'}


def dependency():
    VENDOR.mkdir(parents=True, exist_ok=True)
    source = VENDOR / 'SDL2_bgi-3.0.4/src'
    if not source.exists():
        archive = VENDOR / 'SDL2_bgi-3.0.4.tar.gz'
        urllib.request.urlretrieve(URL, archive)
        if hashlib.sha256(archive.read_bytes()).hexdigest() != SHA256:
            raise RuntimeError('SDL_bgi download checksum mismatch')
        with tarfile.open(archive) as package:
            package.extractall(VENDOR, filter='data')
    return source


def transformed(name):
    text = (ROOT / SOURCES[name]).read_text()
    text = re.sub(r'#include[^\n]*\n', '', text)
    text = '#include "compatibility.h"\n' + text
    text = text.replace('void main()', 'int main()')
    text = re.sub(r'initgraph\(&driver,&mode,"[^\n]*"\);',
                  'initwindow(640,480);\n    SDL_SetWindowTitle(bgi_window,"'+NAMES[name]+'");', text)
    if name == 'alien':
        text = text.replace('char keypress;', 'char keypress=0;')
        text = re.sub(r'(case (?:left_key|right_key|up_key|down_key):)', r'\1 {', text)
        for marker in ['case right_key:', 'case up_key:', 'case down_key:']:
            text = text.replace(marker, '}\n'+marker)
        marker = '\t\t       }'
        if text.count(marker) != 1:
            raise RuntimeError('Original switch layout changed; review compatibility transform')
        text = text.replace(marker, '\t\t       }\n'+marker, 1)
    else:
        text = re.sub(r'closegraph\s*\(\s*\);\s*set_graph\(\);', 'cleardevice();', text)
        # New test records use the current compiler's layout; historical binary files stay untouched.
        text = text.replace('    public:', '''    public:
    item(const char* name="", int code=0, float price=0) : item_code(code),item_price(price) {
        std::memset(item_name,0,sizeof(item_name));std::strncpy(item_name,name,sizeof(item_name)-1);
    }
''', 1)
        seed = '''
    // Fresh local demo data only. Never open the archived DAT files.
    if (!ifstream("ownerp.dat").good()) { ofstream("ownerp.dat") << "owner"; }
    if (!ifstream("empp.dat").good()) { ofstream("empp.dat") << "staff"; }
    if (!ifstream("rlist.dat").good()) {
        ofstream file("rlist.dat",ios::binary);
        item entries[]={item("Tea",1,20),item("Coffee",2,40),item("Sandwich",3,60)};
        for (auto &entry:entries) file.write((char*)&entry,sizeof(entry));
    }
    if (!ifstream("sales.dat").good()) { ofstream file("sales.dat",ios::binary); }
'''
        text = text.replace('int main()\n{', 'int main()\n{'+seed, 1)
        # Read passwords within the original 10-byte buffers and handle missing files safely.
        begin = text.index('    int z=0;', text.index('void get_pass_frm_file()       //'))
        end = text.index('\n}', begin)
        text = text[:begin] + '''    ifstream owner("ownerp.dat"), employee("empp.dat");
    owner.getline(pass_o,sizeof(pass_o));employee.getline(pass_e,sizeof(pass_e));
''' + text[end:]
    return text


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('program', choices=SOURCES)
    parser.add_argument('--build-only', action='store_true')
    args = parser.parse_args()
    BUILD.mkdir(parents=True, exist_ok=True)
    vendor = dependency()
    prefix = Path(subprocess.check_output(['brew','--prefix','sdl2'],text=True).strip())
    flags = ['-I'+str(prefix/'include'), '-I'+str(vendor)]
    library = BUILD / 'SDL2_bgi.o'
    # SDL_bgi 3.0.4 clears the viewport in setbkcolor; the archived programs
    # depend on Borland palette-style recoloring without erasing foregrounds.
    original = (vendor/'SDL2_bgi.c').read_text()
    start = original.index('void setbkcolor (int col)')
    end = original.index('} // setbkcolor ()', start)
    section = original[start:end]
    section = section.replace('  check_initgraph ();', '  check_initgraph ();\n  Uint32 archive_old_bg = bgi_argb_palette[bgi_bg_color];', 1)
    section = section.replace('  clearviewport ();', '''  // Archive compatibility modification: preserve existing foreground pixels.
  for (int x = vp.left; x <= vp.right; ++x)
    for (int y = vp.top; y <= vp.bottom; ++y)
      if (PIXEL(x, y) == archive_old_bg)
        PIXEL(x, y) = bgi_argb_palette[bgi_bg_color];
  update ();''', 1)
    adapted = original[:start] + section + original[end:]
    adapted_file = BUILD / 'SDL2_bgi.c'
    changed = not adapted_file.exists() or adapted_file.read_text() != adapted
    if changed: adapted_file.write_text(adapted)
    if changed or not library.exists():
        subprocess.run(['clang','-O2','-c',str(adapted_file),*flags,'-o',str(library)],check=True)
    # Native SDL2 avoids keyboard-event issues seen with Homebrew's SDL2-on-SDL3 compatibility library.
    native = VENDOR / 'libSDL2-2.0.0.dylib'
    if not native.exists():
        url = 'https://api.nuget.org/v3-flatcontainer/monogame.library.sdl/2.32.10.2/monogame.library.sdl.2.32.10.2.nupkg'
        package = urllib.request.urlopen(url).read()
        expected = 'a335a392f4d7316b68c93e4276127d82a1ffe90a18c8e765a4f84cbb61eb96e4'
        if hashlib.sha256(package).hexdigest() != expected:
            raise RuntimeError('SDL2 package checksum mismatch')
        with zipfile.ZipFile(io.BytesIO(package)) as bundle:
            native.write_bytes(bundle.read('runtimes/osx/native/libSDL2-2.0.0.dylib'))
            (VENDOR/'SDL2-LICENSE.txt').write_bytes(bundle.read('LICENSE.txt'))
    source = BUILD / (args.program+'.cpp')
    source.write_text(transformed(args.program))
    executable = BUILD / args.program
    subprocess.run(['clang++','-std=c++17','-Wno-deprecated-declarations','-Wno-writable-strings',
                    *flags,'-I'+str(ROOT/'runnable/LegacyCpp'),str(source),str(library),
                    str(native),'-Wl,-rpath,'+str(native.parent),'-o',str(executable)],check=True)
    if args.build_only:
        return
    data = BUILD / (args.program+'-data')
    data.mkdir(exist_ok=True)
    launch_app(NAMES[args.program], 'local.miscprojects.'+args.program, executable, cwd=data)
    if args.program == 'restaurant':
        print('Fresh demo logins: owner / staff. Data is isolated at',data)


if __name__ == '__main__':
    main()
