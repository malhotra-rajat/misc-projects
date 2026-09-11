#!/usr/bin/env python3
"""Run the original LoginServlet and PrimeCheck JSP locally with embedded Tomcat."""
import argparse
import hashlib
import os
from pathlib import Path
import shutil
import subprocess
import urllib.request
from archive_apps import ROOT

VERSION='9.0.121'
DEPS=[('org/apache/tomcat/embed','tomcat-embed-core',VERSION),
      ('org/apache/tomcat/embed','tomcat-embed-jasper',VERSION),
      ('org/apache/tomcat/embed','tomcat-embed-el',VERSION),
      ('org/apache/tomcat','tomcat-annotations-api',VERSION),
      ('org/eclipse/jdt','ecj','3.26.0')]

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--port',type=int,default=8091)
    args=parser.parse_args()
    build=ROOT/'.archive-build/java-web';lib=build/'lib';lib.mkdir(parents=True,exist_ok=True)
    jars=[]
    for group,name,version in DEPS:
        filename=f'{name}-{version}.jar';path=lib/filename
        url=f'https://repo.maven.apache.org/maven2/{group}/{name}/{version}/{filename}'
        if not path.exists():
            data=urllib.request.urlopen(url).read()
            expected=urllib.request.urlopen(url+'.sha1').read().decode().split()[0]
            if hashlib.sha1(data).hexdigest()!=expected:raise RuntimeError('Maven checksum mismatch: '+filename)
            path.write_bytes(data)
        jars.append(str(path))
    classes=build/'classes';classes.mkdir(exist_ok=True)
    classpath=os.pathsep.join(jars)
    subprocess.run(['javac','-cp',classpath,'-d',str(classes),str(ROOT/'College/JavaLab/LoginServlet.java'),str(ROOT/'runnable/JavaWeb/ArchiveWebHost.java')],check=True)
    web=build/'web';web.mkdir(exist_ok=True)
    shutil.copy2(ROOT/'College/JavaLab/PrimeCheck.jsp',web/'PrimeCheck.jsp')
    (web/'index.html').write_text('<!doctype html><title>Archived Java web labs</title><h1>Java web labs</h1><p><a href="/login">Original login servlet</a> (demo: admin / pass)</p><p><a href="/PrimeCheck.jsp">Original prime-number JSP</a></p>')
    (web/'Welcome.html').write_text('<!doctype html><title>Welcome</title><h1>Login successful</h1><p>This welcome page is a restoration placeholder; the original servlet redirects here.</p><a href="/">Back to labs</a>')
    command=['java','-cp',str(classes)+os.pathsep+classpath,'ArchiveWebHost',str(build/'tomcat'),str(web),str(args.port)]
    os.execvp(command[0],command)

if __name__=='__main__':main()
