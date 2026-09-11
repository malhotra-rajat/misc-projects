#!/usr/bin/env python3
"""Compile/run an original Java lab, or run seven reproducible console checks."""
import argparse
from pathlib import Path
import shutil
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[1]
SOURCES = ROOT / "College" / "JavaLab"
BUILD = ROOT / ".archive-build" / "java-lab"


def compile_lab(name):
    destination = BUILD / name
    destination.mkdir(parents=True, exist_ok=True)
    subprocess.run(["javac", "-d", str(destination), str(SOURCES / (name + ".java"))], check=True)
    return destination


def smoke_test():
    cases = [
        ("pascal", "5\n", ("1 4 6 4 1",)),
        ("selection", "5\n33 1 12 10 6\n", ("1 6 10 12 33",)),
        ("calc", "", ("rectangle is:50", "triangle is:6.0")),
        ("charcheck", "Ab3!\n", ("No Of Digits:1", "No Of Uppercase Characters:1", "No Of Lowercase Characters:1", "No Of Other Characters:1")),
        ("multithread", "", ("(JAVA)", "(PROGRAMMING)", "Main Now Ends")),
        ("excp", "", ("chk: Finally",)),
        ("filecopy", "", ("File copied.",)),
    ]
    failures = 0
    for name, sample, expected in cases:
        try:
            destination = compile_lab(name)
            arguments = []
            if name == "filecopy":
                (destination / "smoke-input.txt").write_text("Old project archive smoke test.\n")
                (destination / "smoke-output.txt").unlink(missing_ok=True)
                arguments = ["smoke-input.txt", "smoke-output.txt"]
            result = subprocess.run(["java", "-cp", str(destination), name, *arguments],
                                    input=sample, text=True, capture_output=True,
                                    cwd=destination, timeout=15)
            passed = result.returncode == 0 and all(part in result.stdout for part in expected)
            if name == "filecopy":
                copied = destination / "smoke-output.txt"
                passed = passed and copied.exists() and copied.read_bytes() == (destination / "smoke-input.txt").read_bytes()
            if not passed:
                print(result.stdout, result.stderr, file=sys.stderr)
        except (subprocess.SubprocessError, OSError) as error:
            print(error, file=sys.stderr)
            passed = False
        print(f"{'PASS' if passed else 'FAIL'} {name}")
        failures += not passed
    print(f"{len(cases) - failures}/{len(cases)} sample checks passed.")
    return 1 if failures else 0


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--smoke-test", action="store_true", help="compile and check seven console labs")
    parser.add_argument("program", nargs="?", choices=sorted(p.stem for p in SOURCES.glob("*.java")))
    parser.add_argument("arguments", nargs=argparse.REMAINDER, help="arguments passed to the Java program")
    options = parser.parse_args()
    if not shutil.which("javac") or not shutil.which("java"):
        parser.error("Install a JDK and put java and javac on PATH.")
    if options.smoke_test:
        if options.program:
            parser.error("Choose either --smoke-test or one program.")
        return smoke_test()
    if not options.program:
        parser.print_help()
        return 0
    destination = compile_lab(options.program)
    return subprocess.call(["java", "-cp", str(destination), options.program, *options.arguments])


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except subprocess.CalledProcessError as error:
        raise SystemExit(error.returncode)
