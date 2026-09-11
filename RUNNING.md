# Running and restoring the archive

Status checked in September 2026 on an Apple Silicon Mac with Java 23.0.2 and .NET SDK 9.0.200. A compiler being installed does not make the Windows/DOS apps runnable. Only the Java console samples below were executed successfully.

## 1. Java — run now

From the repository root:

```sh
python3 tools/run_java_lab.py --smoke-test
python3 tools/run_java_lab.py selection
python3 tools/run_java_lab.py pascal
```

All seven sample checks pass without changing the original Java source. The launcher compiles each lab separately and keeps generated output in `.archive-build/`. The known word-count bug, untested GUI windows, applet-host requirement and servlet dependency are documented in [JavaLab/README.md](College/JavaLab/README.md).

## 2. Alienated Alien, Restaurant Billing and C/C++ — emulate first

The smallest historical restoration route is a DOS emulator plus a compatible, separately supplied Turbo C++ toolchain and BGI graphics drivers. [DOSBox documents Borland compiler compatibility](https://www.dosbox.com/wiki/Software:Borland_Turbo_C); [DOSBox-X documents mounting local directories](https://dosbox-x.com/wiki/Home). Compiler installation media is not included in this repository.

Work in a scratch copy: these programs can write data files. Match the source's `initgraph` BGI-driver paths, supply clean sample data where needed, and compile inside the emulated DOS environment. Restaurant Billing uses binary record layouts tied to its old compiler, so reusing its binary data in a modern C++ build requires more than changing headers. Its existing password files are historical local state, not recommended demo credentials.

For a native Mac version, start with one console exercise: standardize headers and `main`, replace `clrscr`/`getch`, then test input/output and array bounds. For the games, replace BGI drawing and DOS keyboard/timer calls while keeping gameplay logic. None of those ports is implemented by this import.

## 3. Geek Quest — a separate MonoGame port

The archive contains XNA 3.1 (prototype) and XNA 3.0 (final) projects. [MonoGame's migration guide](https://docs.monogame.net/articles/migration/migrate_xna.html) describes compatibility with XNA 4.0 and migration considerations for older APIs. That means this is a port with API/content changes, not simply a successful `dotnet run` on the original solution.

Suggested sequence:

1. Create a separate DesktopGL project for the existing two-texture prototype. Get movement and drawing working first.
2. Migrate older XNA API calls, then bring in final-game source and replace the old `.contentproj` with a MonoGame content build.
3. Rebuild textures and fonts. Normalize asset-name case; the original Windows tree mixes cases such as `exit.PNG` and `mySounds`.
4. Restore audio. Final `Game1.cs` uses `AudioEngine`, `WaveBank` and `SoundBank`, while the archive includes source WAV/XACT assets. Verify cross-platform audio support or replace those calls with a simpler audio path.
5. Test menus, collisions, level transitions, pause/game-over, and sound on macOS before calling the port playable.

[MonoGame's getting-started tutorial](https://docs.monogame.net/articles/tutorials/building_2d_games/02_getting_started/?tabs=macos) describes macOS setup. The original final project has its omitted private signing key disabled; this does not solve its XNA runtime dependencies.

## 4. ChatBug and Gadget Guru — Windows plus a clean database

These are classic ASP.NET Web Forms applications, not ASP.NET Core. [Microsoft's ASP.NET overview](https://learn.microsoft.com/en-us/aspnet/overview) identifies the Web Forms framework and Windows development environment.

Use a compatible Windows development machine or VM with .NET Framework/Web Forms tooling, IIS Express/IIS, and SQL Server. On this Apple Silicon Mac, verify guest architecture and SQL Server/tooling compatibility before provisioning a VM; Windows ARM is not automatically a substitute for an old x86 development environment.

Start with one ChatBug variant. Reconstruct the custom tables from its `.dbml` and call sites, generate clean ASP.NET membership storage, update connection strings, then check registration, login, public chat and logout. Test private chat separately against its extra schema. Gadget Guru also embeds connection strings in code and needs tables inferred from queries/page bindings. Database recreation is a prerequisite, not completed work. Run restored examples locally while assessing their old authentication and SQL behavior.

## 5. MusicManager — Windows first

The source targets .NET Framework 4.0, uses Windows Forms, Shell32 COM metadata and native Windows multimedia/window APIs. [Microsoft documents Windows Forms as a Windows desktop framework](https://learn.microsoft.com/en-us/dotnet/desktop/winforms/overview/).

Open the application `.csproj` directly on Windows, provide compatible reference assemblies or retarget a restoration copy, resolve Shell32 interop, and test playback on a scratch music folder. Skip the installer until the application works. A modern .NET Windows build might be a manageable follow-up, but does not provide Mac support: a Mac port would replace both WinForms and the native shell/audio calls. Original third-party attribution must stay attached.

## Recommended next work

The Java set is ready for use. For a memorable visual result on this Mac, start with the small Geek Quest MonoGame prototype, then migrate the recovered full game. DOS emulation is the better route for preserving the exact look of Alienated Alien and Restaurant Billing. Restore MusicManager and the Web Forms applications in a compatible Windows environment rather than spending initial effort replacing their entire platform.

The existing Android, web, Racket and information-retrieval coursework is outside this import's runtime validation. No Windows app or game is claimed to be playable yet.
