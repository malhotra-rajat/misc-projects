# Miscellaneous coding projects

A personal archive of school, college, and later coursework: early games, programming exercises, web applications, and experiments. Original code is preserved for its history, including its rough edges and collaborators' contributions.

## School and college archive

| Project | What it contains | Current run status |
|---|---|---|
| [Alienated Alien](School/AlienatedAlien/) | Class XI Turbo C++ graphics game | Mac compatibility launcher: graphics and movement checked |
| [Restaurant Billing](HighSchoolProject%20-%20Restaurant%20Billing/) | Class XII project by Rohit and Rajat | Mac compatibility launcher: login, rate list and a saved sample bill verified |
| [The Geek Quest](The%20Geek%20Quest/) | Existing prototype plus recovered full final game | Final game launched on Mac through a separate MonoGame port |
| [Java labs](College/JavaLab/) | Console, AWT/Swing, applet and servlet exercises | Seven console checks pass; four GUI demos launched; servlet/JSP checks pass |
| [School C++](School/CppExercises/) | Deduplicated exercises and historical variants | Legacy compiler dependencies; includes attributed reference code |
| [C programming](College/CProgramming/) | Second-semester exercises | Requires compatibility work |
| [OOP](College/OOP/) and [data structures](College/DataStructures/) | Third-semester exercises | Requires compatibility work |
| [Algorithms and graphics](College/AlgorithmsAndGraphics/) | Fourth-semester lab sources | Requires legacy compiler/graphics support |
| [ChatBug](College/ChatBug/) | Final-print, image, and private-chat variants | ASP.NET Web Forms + SQL Server; databases must be recreated |
| [Gadget Guru](College/GadgetGuru/) | Gadget website, registration and login | ASP.NET Web Forms + SQL Server; database must be recreated |
| [MusicManager](College/MusicManager/) | Music-player project snapshot | Windows Forms / .NET Framework 4.0; third-party attribution preserved |

Run the verified examples from the repository root:

```sh
python3 tools/run_java_lab.py --smoke-test
python3 tools/run_geek_quest.py
python3 tools/run_legacy_cpp.py alien
python3 tools/run_legacy_cpp.py restaurant
python3 tools/run_java_web.py
```

See [RUNNING.md](RUNNING.md) for launch commands, dependencies, tested behavior, and remaining limitations. [Archive notes](archive/README.md) describe provenance, omissions, and the distinction between original work, group work, and reference material. This is an archive, not a claim of sole authorship of every file.

## Other existing coursework

- [Information retrieval](InformationRetrieval/)
- [Mobile app development](MobileAppDevelopment/) — NUMAD archived APK runs via `python3 tools/run_numad.py`; dictionary and Word Game checked on Android 15
- [Programming design paradigms](ProgrammingDesignParadigms/)
- [Web development](WebDevelopment/)

These pre-existing projects were not given a full runtime audit as part of the School/College import.
