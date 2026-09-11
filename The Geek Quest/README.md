# The Geek Quest

Two historical stages are kept together:

- `The Geek Quest/`: the pre-existing prototype, targeting Windows x86, .NET Framework 3.5 and XNA 3.1, with two source textures.
- `final/`: recovered full game from `7th sem/Minor Project/Final Proj Files/Project Final`, targeting Windows x86, .NET Framework 3.5 and XNA 3.0. Includes enemy/fireball/bridge logic, levels, menus, and original graphics/audio.

The final game has not been launched on macOS. Its source/content manifest inputs were checked against the recovered files. The old temporary signing key is excluded, signing disabled, and the publish path made relative. All gameplay code is unchanged.

For a Mac version, create a separate MonoGame DesktopGL project that shares or copies the game source, then migrate the XNA 3 APIs/content pipeline and audio. Start with the small prototype before restoring all final-game assets. See [the runtime plan](../RUNNING.md). Original assets and credits are retained as archival material; their origins have not all been independently established.
