# Java lab

Original fifth-semester lab sources, preserved unchanged.

From the repository root, with Python 3 and a JDK on PATH:

```sh
python3 tools/run_java_lab.py --smoke-test
python3 tools/run_java_lab.py pascal
python3 tools/run_java_lab.py selection
python3 tools/run_java_lab.py frameandpanel
python3 tools/run_java_lab.py helloworldswing
```

The last two commands open GUI programs; their compilation was checked, but their windows were not tested in this import. Build output is isolated under `.archive-build/java-lab/` and ignored by Git. A regular run inherits the caller's working directory and standard input, so `filecopy` paths are relative to your current directory.

Seven console samples pass: `pascal`, `selection`, `calc`, `charcheck`, `multithread`, `excp`, and `filecopy`. These are sample behavior checks, not a comprehensive test suite. Pascal and sorting use fixed-size arrays; use at most 10 rows/elements. Character readers expect a newline-terminated line.

`charcount` is deliberately preserved with its bug: `hello world` reports one word. `movingball` and `manageuserapplet` compile on Java 23 with Applet removal warnings, but need an applet host or conversion to a standalone window. `LoginServlet` requires a compatible `javax.servlet` API/container and deployment configuration; it does not compile with the JDK alone. Do not count compiling an applet as running it.
