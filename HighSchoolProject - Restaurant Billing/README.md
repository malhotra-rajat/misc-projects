# Restaurant Billing

Class XII-B project credited in the source to **Rohit and Rajat**. The existing `Source Code/12PRO.CPP` matches the School archive's final restaurant project after newline normalization, so another duplicate was not imported.

The source uses Turbo C++ headers, BGI graphics, and DOS console functions. It fails the current Mac compiler's syntax check at `fstream.h`. The `Executable` directory contains historical data files, not a ready-to-launch executable in this checkout.

Restore with a compatible DOS/Turbo C++ environment, or port a separate copy to modern C++. Use scratch data when testing: the code stores binary records and password-related state (`ownerp.dat`, `empp.dat`), whose layouts and meaning should not be assumed portable across compilers. Existing historical data files were not altered by the archive import. See [RUNNING.md](../RUNNING.md) for the restoration plan.
