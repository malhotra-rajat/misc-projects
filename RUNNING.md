# Running the archive

Checked on an Apple Silicon Mac in September 2026 with Java 23.0.2, .NET SDK 9.0.200, Python 3.13, and Apple clang. The original archive is preserved; compatibility code lives in `runnable/` and `tools/`. These are sample runtime checks, not complete game or application validation.

## Launch on Mac

Run these commands from the repository root. First runs download dependencies and build; desktop launchers create local `.app` bundles. Close an existing demo before relaunching after code changes. Generated dependencies, fonts, logs, apps and demo databases stay under ignored `.archive-build/`; .NET also uses ignored `bin/` and `obj/`.

| Project | Command | Observed result |
|---|---|---|
| Geek Quest, recovered final game | `python3 tools/run_geek_quest.py` | Game window, original help screen and game-over/score screen rendered |
| Alienated Alien | `python3 tools/run_legacy_cpp.py alien` | Introduction, game scene, arrow movement and countdown worked |
| Restaurant Billing | `python3 tools/run_legacy_cpp.py restaurant` | Staff login, rate list and a 2-tea / 40-total bill verified and saved |
| Java console labs | `python3 tools/run_java_lab.py --smoke-test` | Seven sample checks passed |
| Swing shapes | `python3 tools/run_java_gui.py helloworldswing` | Rectangle and ellipse rendered |
| AWT controls | `python3 tools/run_java_gui.py frameandpanel` | Window, text area and buttons rendered |
| Moving-ball applet | `python3 tools/run_java_gui.py movingball` | Red ball on blue background rendered in a desktop host |
| Number-comparison applet | `python3 tools/run_java_gui.py manageuserapplet` | Start displayed three input fields and result button; calculation interaction unverified |
| Servlet and JSP | `python3 tools/run_java_web.py` | Login success/rejection, prime/composite and invalid-input HTTP checks passed |

### Geek Quest

Requires .NET 9 SDK and Python. The launcher creates a private Python environment and installs Pillow 11.3.0 for font atlases. It uses the original Comic Sans MS fonts from `/System/Library/Fonts/Supplemental`; missing fonts are reported explicitly. MonoGame 3.8.5.1 is restored through NuGet. This launcher targets Apple Silicon (`osx-arm64`).

The separate port uses the original final-game images, WAV audio and gameplay sources. Its content loader replaces the old XNA content pipeline, handles Windows-style asset paths, and generates fonts locally. SoundEffect replaces XACT background audio. Audio fidelity, every level, collision edge cases and completion have not been exhaustively checked.

The updated H help screen lists the current controls: A/D move left/right, W or O jumps, I fires, P pauses, R resumes, E returns from paused/end/help screens, and Escape exits. The game has no crouch/down action for S. `--build-only` builds without opening a window.

### Alienated Alien and Restaurant Billing

Requires Python 3.12+, clang and Homebrew SDL2 headers (`brew install sdl2`). The launcher downloads checksum-verified SDL_bgi 3.0.4 and the native SDL2 library packaged by MonoGame.Library.SDL 2.32.10.2. Native SDL2 avoids keyboard problems encountered with the Homebrew SDL2-on-SDL3 compatibility runtime. Both launchers accept `--build-only`.

These are native compatibility builds, not DOS emulation. The launcher generates adapted C++ copies, maps old graphics/text/keyboard calls, and retains the archived originals. SDL_bgi background recoloring is adapted to preserve existing foreground graphics. Fonts, layout and timing may differ from the historical programs.

Alienated Alien: continue past the intro with a key; use arrow keys to move. Escape exits.

Restaurant Billing: use `staff` for the employee password or `owner` for the owner password. Fresh demo records contain code 1 Tea (20), code 2 Coffee (40), and code 3 Sandwich (60). In bill entry, 0 opens the rate list and 123 finishes the bill. Demo records are created only when absent in `.archive-build/legacy-cpp/restaurant-data/`; archived DAT files are never opened by the launcher. The compatibility build uses its own native record layout.

Keyboard automation was intermittent in the initial run. After the color fix, a full sample transaction completed: code 1, quantity 2, total 40, saved to the local sales file. Invalid-input handling and original buffer limits have not been comprehensively repaired. Use these small demo records. Escape is mapped to exit, although input delivery may require interacting directly with the window.

### Java

Requires a JDK with `java`, `javac`, `jar` and `jpackage`. The applet host was tested on JDK 23, which still supplies the deprecated Applet API; a newer JDK that removes it will not run these hosts unchanged. The original lab files are not modified. Known word-count and comparison-app bugs remain; see [Java lab notes](College/JavaLab/README.md).

The web launcher downloads embedded Tomcat 9.0.121 and its pinned dependencies from Maven Central, checking the published checksums. It binds to `127.0.0.1` only. Open [the local Java labs](http://127.0.0.1:8091/) after starting it; Ctrl-C stops the foreground server. `--port 8092` selects another port. The original login uses toy credentials `admin` / `pass`; the missing welcome page is supplied as a labeled placeholder.

## Still needs a Windows environment

**MusicManager was attempted but did not build on this Mac:** MSB3644 reports missing .NET Framework 4.0 reference assemblies. It also uses Windows Forms, Shell32 COM and native Windows multimedia/window APIs. A compatible Windows machine/toolchain is needed to run the original; a Mac version would require replacing its UI and platform integrations. Attribution to Hasan Shahriar Masud remains in the archive.

**ChatBug and Gadget Guru have not been run.** They require classic ASP.NET Web Forms plus SQL Server and clean recreated databases. The archived DBML/query code can guide schema reconstruction, but that work and runtime validation remain outstanding. Private historical databases were not imported.

The other C/C++ exercises and pre-existing web, Racket and information-retrieval coursework have not received runtime restoration in this pass.

## NUMAD Android suite

Run `python3 tools/run_numad.py` with an installed Android SDK and the `Pixel_9_Pro_XL_API_35` AVD, or choose another AVD with `--avd NAME`. Set `ANDROID_HOME` if the SDK is not at `~/Library/Android/sdk`. Use `--serial emulator-5554` to select an already-running emulator. The launcher installs the preserved NUMAD14S-New APK; it does not rebuild either historical Android source tree. It starts new emulator sessions read-only so emulator state is temporary.

Checked on Android 15/API 35, ARM64: main menu opens, dictionary input returns matching words, and Word Game starts an Easy board with a countdown. The final Let's Talk Workouts menu also opens; speech/workout recording and network/multiplayer functionality are not yet fully validated. The older NUMAD14S-RajatMalhotra Eclipse tree is preserved separately and has not been rebuilt.

Restaurant Billing color follow-up: console clearing now preserves the BGI background rather than forcing white. The rate list displays yellow text on red; default console foreground resets to black so subsequent screen headings are not erased when the background changes.

## Implementation and dependencies

See [runnable/README.md](runnable/README.md) for the compatibility boundary and dependency notices. Reference documentation: [MonoGame XNA migration](https://docs.monogame.net/articles/migration/migrate_xna.html), [SDL_bgi](https://sdl-bgi.sourceforge.io/), [Windows Forms](https://learn.microsoft.com/en-us/dotnet/desktop/winforms/overview/), and [ASP.NET](https://learn.microsoft.com/en-us/aspnet/overview).
