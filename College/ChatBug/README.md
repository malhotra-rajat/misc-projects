# ChatBug

Three distinct college-project snapshots:

- `final-print/`: the version filed with the final printed report.
- `with-image/`: the image-enabled snapshot.
- `private-chat/`: the private-chat variant filed under `FINAL ROHIT` and `ChatBug - nitin and rohit`; original collaborator attribution is retained.

ASP.NET Web Forms/C# with LINQ-to-SQL mappings, ASP.NET membership and SQL Express attachment connection strings. Source is preserved unchanged. This is group/project archival material, not a sole-authorship claim.

The original `Database.mdf`, `ASPNETDB.MDF`, and log files were omitted because they can contain account data. Recreate clean schemas and membership storage; the DBML mappings reveal `CommonRoom` and `UserData`, and the private-chat variant has additional mappings. They are schema clues, not a verified complete database backup. Update connection strings to your local test database.

Use a Windows Web Forms development environment to restore a local demo. The saved Google privacy webpage was omitted from `final-print` and `with-image`; replace its link/page when restoring them. Authentication/database behavior has not been tested. See [RUNNING.md](../../RUNNING.md).
