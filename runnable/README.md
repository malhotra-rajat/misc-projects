# Compatibility versions

This directory contains September 2026 runtime adaptations. Historical sources remain in their original archive directories; no original program is replaced by its port.

- `GeekQuest/Legacy`: copies of the final game's C# sources, adapted for MonoGame (obsolete namespaces removed, custom content loader, SoundEffect audio, explicit window dimensions and Escape exit). `ArchiveContentManager.cs` loads the original textures/WAVs and generated font atlases. Original assets and authorship remain attached to the archived project.
- `LegacyCpp/compatibility.h`: modern equivalents for the graphics console, DOS keys, time and stream input/output. `tools/run_legacy_cpp.py` generates C++ copies with modern headers/main, corrected switch scopes, initialized input state, bounded demo password reads and separate restaurant sample records. It also adapts SDL_bgi background recoloring. Gameplay/business logic otherwise remains historical, including unverified edge cases.
- `JavaGui/ArchiveAppletHost.java`: desktop container for unchanged applet classes, including validation after the Start button adds fields.
- `JavaWeb/ArchiveWebHost.java`: localhost-only embedded Tomcat wiring for the unchanged servlet/JSP. The launcher generates a landing page and a labeled replacement for the missing welcome page.

Build products and downloaded dependencies are ignored, not vendored. Launch commands and verification limits are in [RUNNING.md](../RUNNING.md).

## Dependency notices

The launchers download MonoGame 3.8.5.1 (Microsoft Public License), Pillow 11.3.0 (HPND), SDL_bgi 3.0.4 by Guido Gonzato (zlib), native SDL2 via MonoGame.Library.SDL 2.32.10.2 (see its bundled LICENSE.txt), Apache Tomcat 9.0.121 (Apache License 2.0) and Eclipse ECJ 3.26.0 (EPL 2.0). Dependency packages retain their notices in the local cache. SDL_bgi's modified source retains its original notice and labels the recoloring modification. No downloaded binary or generated font atlas is committed. Comic Sans MS is read from the user's installed macOS fonts, not redistributed.
