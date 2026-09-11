# The Geek Quest

Two historical stages are kept together:

- `The Geek Quest/`: the pre-existing prototype, targeting Windows x86, .NET Framework 3.5 and XNA 3.1, with two source textures.
- `final/`: recovered full game from `7th sem/Minor Project/Final Proj Files/Project Final`, targeting Windows x86, .NET Framework 3.5 and XNA 3.0. Includes enemy/fireball/bridge logic, levels, menus, and original graphics/audio.

The final game has now been launched on an Apple Silicon Mac through a separate MonoGame compatibility version. Its source/content manifest inputs were checked against the recovered files. The old temporary signing key is excluded, signing disabled, and the publish path made relative. All gameplay code is unchanged.

Run `python3 tools/run_geek_quest.py` from the repository root. The port lives in `runnable/GeekQuest/` and loads original assets; its help and game-over/score screens were observed, but every level and audio behavior have not been fully tested. See [run instructions](../RUNNING.md). Original assets and credits are retained as archival material; their origins have not all been independently established.
