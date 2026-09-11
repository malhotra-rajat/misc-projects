#!/usr/bin/env python3
"""Build/run the Mac compatibility version using the original Geek Quest assets."""
import argparse
import json
import math
import os
from pathlib import Path
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
CONTENT = ROOT / 'The Geek Quest/final/CreatingA2DSprite/Content'
BUILD = ROOT / '.archive-build'
PROJECT = ROOT / 'runnable/GeekQuest/GeekQuest.csproj'


def fonts():
    from PIL import Image, ImageDraw, ImageFont
    target = BUILD / 'geek-fonts'
    target.mkdir(parents=True, exist_ok=True)
    for spec in CONTENT.glob('*.spritefont'):
        asset = ET.parse(spec).getroot().find('Asset')
        family = asset.findtext('FontName')
        style = asset.findtext('Style')
        suffix = ' Bold' if 'Bold' in style else ''
        font_file = Path('/System/Library/Fonts/Supplemental') / (family + suffix + '.ttf')
        if not font_file.exists():
            raise RuntimeError(f'Original font not found: {font_file}')
        size = round(float(asset.findtext('Size')) * 96 / 72)
        font = ImageFont.truetype(str(font_file), size)
        ascent, descent = font.getmetrics()
        line_height = ascent + descent
        cell = max(line_height + 4, math.ceil(font.getlength('W')) + 8)
        atlas = Image.new('RGBA', (cell * 16, cell * 6))
        draw = ImageDraw.Draw(atlas)
        glyphs = []
        for i, code in enumerate(range(32, 127)):
            x, y = i % 16 * cell, i // 16 * cell
            draw.text((x, y), chr(code), font=font, fill=(255, 255, 255, 255), anchor='lt')
            glyphs.append(dict(code=code, x=x, y=y, width=cell, height=cell, advance=font.getlength(chr(code))))
        atlas.save(target / (spec.stem + '.png'))
        (target / (spec.stem + '.json')).write_text(json.dumps(dict(glyphs=glyphs, lineHeight=line_height, spacing=float(asset.findtext('Spacing')))))
    return target


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--build-only', action='store_true')
    args = parser.parse_args()
    try:
        import PIL
    except ImportError:
        python = BUILD / 'python/bin/python3'
        if not python.exists():
            subprocess.run([sys.executable, '-m', 'venv', str(BUILD / 'python')], check=True)
        subprocess.run([str(python), '-m', 'pip', 'install', 'Pillow==11.3.0'], check=True)
        os.execv(str(python), [str(python), __file__, *sys.argv[1:]])
    font_dir = fonts()
    subprocess.run(['dotnet', 'build', str(PROJECT), '--nologo', '-v', 'minimal'], check=True)
    if args.build_only:
        return
    executable = ROOT / 'runnable/GeekQuest/bin/Debug/net9.0/osx-arm64/GeekQuest'
    from archive_apps import launch_app
    launch_app("Geek Quest", "local.miscprojects.geekquest", executable,
               environment={"GEEK_QUEST_CONTENT": str(CONTENT), "GEEK_QUEST_FONTS": str(font_dir)})


if __name__ == '__main__':
    main()
