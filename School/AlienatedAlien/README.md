# Alienated Alien

Class XI-B graphics game, credited to Rajat Malhotra in `11GAME.CPP`. Use directional keys to guide the alien to food before the timer expires.

Preserved unchanged from `School/11GAME.CPP`. Uses Turbo C++/Borland graphics, `conio.h`, and DOS APIs, including a hardcoded `c:\tcc` graphics-driver path. The original file remains unchanged.

A separate Mac compatibility launcher now builds with SDL_bgi:

```sh
python3 tools/run_legacy_cpp.py alien
```

The original introduction, game graphics, arrow-key movement and countdown were observed on an Apple Silicon Mac. This is a native compatibility version, with different timing/font rendering from Turbo C++. See [RUNNING.md](../../RUNNING.md) for dependencies and controls.
