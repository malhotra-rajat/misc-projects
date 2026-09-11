# Restaurant Billing

Class XII-B project credited in the source to **Rohit and Rajat**. The existing `Source Code/12PRO.CPP` matches the School archive's final restaurant project after newline normalization, so another duplicate was not imported.

The source uses Turbo C++ headers, BGI graphics, and DOS console functions. It fails the current Mac compiler's syntax check at `fstream.h`. The `Executable` directory contains historical data files, not a ready-to-launch executable in this checkout.

A separate Mac compatibility launcher is now available:

```sh
python3 tools/run_legacy_cpp.py restaurant
```

Staff login, bill-entry and rate-list screens were observed. Automated keyboard input was intermittent, so a full saved bill remains unverified. Fresh `owner` / `staff` demo credentials and sample item records are generated only in ignored `.archive-build/legacy-cpp/restaurant-data/`. Archived DAT files remain untouched; historical record layouts are not assumed portable. See [RUNNING.md](../RUNNING.md) for dependencies and controls.
