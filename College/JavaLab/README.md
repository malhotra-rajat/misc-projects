# Java lab

Original fifth-semester sources remain unchanged. Tested on Java 23.0.2 on an Apple Silicon Mac.

From the repository root:

```sh
python3 tools/run_java_lab.py --smoke-test
python3 tools/run_java_lab.py selection
python3 tools/run_java_gui.py helloworldswing
python3 tools/run_java_gui.py frameandpanel
python3 tools/run_java_gui.py movingball
python3 tools/run_java_gui.py manageuserapplet
python3 tools/run_java_web.py
```

Seven console sample checks pass: `pascal`, `selection`, `calc`, `charcheck`, `multithread`, `excp`, and `filecopy`. Pascal and sorting use fixed-size arrays; use at most 10 rows/elements. Character readers expect newline-terminated input. `filecopy` resolves paths against your current working directory.

The Swing shapes, AWT window, moving-ball applet, and number-comparison applet were launched and visually checked. Applets use a separate desktop host because browsers no longer host them. Start reveals the comparison fields; automated entry/result checking was not completed. The host requires a JDK that still contains Applet (tested with JDK 23).

The embedded Tomcat launcher serves the original servlet and JSP at `http://127.0.0.1:8091/`. Login rejection, successful login redirect (`admin` / `pass` demo credentials), prime/composite inputs, and invalid input passed HTTP checks. The welcome page is an explicitly marked restoration placeholder because the original redirect target was missing.

Known original bugs remain: `charcount` reports one word for `hello world`; the comparison applet uses separate `if` statements and can append the wrong extra result when the first number is greatest. Repeated comparisons append text. These are historical exercises, not a comprehensive test suite.

Generated files stay in ignored `.archive-build/`. See [RUNNING.md](../../RUNNING.md).
