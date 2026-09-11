# Archive provenance

Imported from the personal `Cheenu Disk/School` and `Cheenu Disk/College` folders in September 2026. Source-relative locations and SHA-256 checksums are recorded in [provenance.json](provenance.json). Exact-byte duplicate exercises within a category are stored once, with every original location retained in `source_copies`. Meaningful variants are kept separately. Line endings and source contents are retained except for the explicitly documented Geek Quest project-file cleanup.

The import includes 339 original source/asset files (approximately 55 MB), plus new documentation and the Java runner. This is a selected project archive rather than a dump of the entire 4 GB personal folder.

## Attribution

- Alienated Alien credits Rajat Malhotra, Class XI-B, in its source.
- Restaurant Billing credits Rohit and Rajat, Class XII-B, in its existing source.
- The Geek Quest is preserved as a college project; original source and asset credits remain. Asset origins have not all been independently established.
- ChatBug includes a private-chat variant originally filed under `FINAL ROHIT` and `ChatBug - nitin and rohit`; preserve these collaborator credits. The other variants' directory labels do not prove sole authorship.
- Gadget Guru is preserved from a training-project folder; authorship is not independently established.
- MusicManager's `Properties/AssemblyInfo.cs` credits **Hasan Shahriar Masud, copyright 2006**. The installer also identifies that author. This snapshot is preserved as material associated with the college project, not presented as wholly original work by the repository owner. The extent of local modifications has not been established.
- `School/CppExercises/School/a/SNAKE.CPP` explicitly credits **Himanshu Mishra**. It is attributed reference material, not the repository owner's original game.
- Other exercises may include classroom or reference material. Existing notices remain in place. No blanket license or new ownership claim is applied to this mixed archive.

## Exclusions and adjustments

Personal results, saved email, unrelated documents, class lists, books, downloaded tutorial bundles, installation media, and redundant project backup trees were not imported. Neither were SQL database data/log files, signing keys, IDE user settings, nor generated `bin/` / `obj/` trees.

The final Geek Quest project file was adjusted only to disable old ClickOnce signing, remove the omitted temporary-key reference and certificate thumbprint, and replace a personal absolute publish path with `publish/`. Gameplay source and assets are unchanged.

ChatBug's saved Google `privacy.html` page and its downloaded page resources were excluded. Links to that historical page may need replacement in a restored demo. The private-chat variant's own ASPX privacy page remains.

MusicManager's `lib/Interop.Shell32.dll` is retained as an existing COM interop build dependency, not treated as disposable application build output. Its old installer project is preserved for context; build the C# application project directly when restoring it.

The pre-existing repository includes tracked dependencies and other artifacts. Those were left in place: recent commits deliberately updated the vendored web dependencies. Ignore rules prevent newly imported local output/private files from entering this archive; they do not retroactively remove existing tracked files. The prior audit also flagged old API-key fields in pre-existing mobile/web code; this import does not rotate those keys or rewrite history.

## Validation

The Java runner's seven console sample checks pass on the installed Java 23.0.2. The other apps are archived with explicit environment requirements, not asserted to have been launched. Original databases were not opened or published.
