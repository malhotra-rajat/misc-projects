#!/usr/bin/env python3
"""Package and open an unchanged Java GUI lab or an applet with a desktop host."""
import argparse
from pathlib import Path
import subprocess
from archive_apps import ROOT

NAMES = {'helloworldswing':'ArchiveSwing','frameandpanel':'ArchiveAwt',
         'movingball':'ArchiveMovingBall','manageuserapplet':'ArchiveUserApplet'}

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('program',choices=NAMES)
    args=parser.parse_args();name=NAMES[args.program]
    build=ROOT/'.archive-build/java-gui'/args.program
    classes=build/'classes';classes.mkdir(parents=True,exist_ok=True)
    inputs=build/'input';inputs.mkdir(exist_ok=True)
    sources=[ROOT/'College/JavaLab'/(args.program+'.java')]
    applet=args.program in {'movingball','manageuserapplet'}
    if applet:sources.append(ROOT/'runnable/JavaGui/ArchiveAppletHost.java')
    subprocess.run(['javac','-d',str(classes),*map(str,sources)],check=True)
    subprocess.run(['jar','--create','--file',str(inputs/'demo.jar'),'-C',str(classes),'.'],check=True)
    destination=build/'package';app=destination/(name+'.app')
    if not app.exists():
        command=['jpackage','--type','app-image','--input',str(inputs),'--main-jar','demo.jar',
                 '--main-class','ArchiveAppletHost' if applet else args.program,'--name',name,
                 '--dest',str(destination),'--mac-package-identifier','local.miscprojects.'+args.program]
        if applet:command+=['--arguments',args.program]
        runtime=ROOT/'.archive-build/ArchiveSwing.app/Contents/runtime/Contents/Home'
        if runtime.exists():command+=['--runtime-image',str(runtime)]
        subprocess.run(command,check=True)
    else:
        # jpackage embeds this JAR in Contents/app; refresh it after compilation.
        import shutil
        shutil.copy2(inputs/'demo.jar',app/'Contents/app/demo.jar')
    subprocess.run(['open',str(app)],check=True)
    print('Opened',app)

if __name__=='__main__':main()
