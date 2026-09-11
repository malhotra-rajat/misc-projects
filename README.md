# Miscellaneous coding projects

A personal archive of school, college, and later coursework: early games, programming exercises, web applications, and experiments. Original code is preserved for its history, including its rough edges and collaborators' contributions.

## School and college archive

| Project | What it contains | Current run status |
|---|---|---|
| [Alienated Alien](School/AlienatedAlien/) | Class XI Turbo C++ graphics game | Requires DOS/Turbo C++ setup or a port |
| [Restaurant Billing](HighSchoolProject%20-%20Restaurant%20Billing/) | Class XII project by Rohit and Rajat | Legacy Turbo C++; unchanged source does not compile on macOS |
| [The Geek Quest](The%20Geek%20Quest/) | Existing prototype plus recovered full final game | Windows XNA 3.1 prototype / XNA 3.0 final; not run on macOS |
| [Java labs](College/JavaLab/) | Console, AWT/Swing, applet and servlet exercises | Seven console sample checks pass on Java 23 |
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
python3 tools/run_java_lab.py selection
```

See [RUNNING.md](RUNNING.md) for tested behavior and a restoration plan. [Archive notes](archive/README.md) describe provenance, omissions, and the distinction between original work, group work, and reference material. This is an archive, not a claim of sole authorship of every file.

## Other existing coursework

- [Information retrieval](InformationRetrieval/)
- [Mobile app development](MobileAppDevelopment/)
- [Programming design paradigms](ProgrammingDesignParadigms/)
- [Web development](WebDevelopment/)

These pre-existing projects were not given a full runtime audit as part of the School/College import.
