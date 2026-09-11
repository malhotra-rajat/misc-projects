# MusicManager / college music-player snapshot

Preserved from `8th sem/Project/Proj 15 apr 2011/Current/Music_Manager_10_src/Music_Manager_10_src`.

**Original attribution: Hasan Shahriar Masud, copyright 2006**, as recorded in `MusicManager/Properties/AssemblyInfo.cs`. The installer carries the same author information. This is a historical snapshot associated with the college project; the repository owner does not claim original authorship of the whole application. Local modifications have not been distinguished from upstream. Original notices remain; no new license is applied.

The application targets .NET Framework 4.0 / Windows Forms and calls Windows Shell32, `winmm.dll`, `user32.dll`, and `kernel32.dll`. It has not been built or run on this Mac. Changing only the target framework will not make it a native Mac app.

Restoration route: open `MusicManager/MusicManager.csproj` directly on Windows, supply compatible .NET Framework reference assemblies (or retarget a separate restoration copy), resolve `lib/Interop.Shell32.dll`, and test local audio playback. Build the application first; the optional `.vdproj` installer may need separate tooling. Test a scratch music directory because the application manages files and tags. A native Mac port requires replacements for the UI, shell metadata and native playback calls.

Generated binaries/caches, source-control bindings, user settings and backup trees were excluded. The existing Shell32 interop DLL and original installer project remain as dependencies/context. See [RUNNING.md](../../RUNNING.md).
