# Releasing F5 Ranger

## Branch model

- `main` targets the newest Minecraft version.
- Version branches (`1.16.5` … `26.2`) target their Minecraft line. They are cut from the last commit before the next Minecraft bump and receive fixes via backports.
- Version branches are kept until they are no longer supported; tags and releases remain after a branch is removed.

## Versioning

- `mod_version=MAJOR.MINOR.PATCH+<minecraft_version>` in `gradle.properties`.
- Each Minecraft line has its own SemVer line.
- A release is the annotated tag `v<mod_version>` (for example `v1.1.1+26.3`).

## Making a release

1. Make sure CI is green and the working tree is clean.
2. Run `scripts/release.sh patch` (or `minor`, `major`, `X.Y.Z`).
   - It bumps `mod_version`, commits `version bump to X`, creates `vX`, and pushes the branch and tag.
3. Pushing the tag triggers the release workflow:
   - validates `mod_version`, `minecraft_version`, branch and tag;
   - builds the mod;
   - creates or updates the GitHub release;
   - publishes to Modrinth if that version number does not exist yet.

Pushing a branch does not release anything. Only `v*` tags release.

## CI layout

- `.github/workflows/release.yml` on `main` is the shared reusable workflow.
- `.github/workflows/gradle.yml` on every branch is a small stub that calls it with the branch JDK and `make-latest` settings.
- Update the shared workflow once on `main`; all branches pick it up.

## Modrinth

- Version numbers are never reused.
- The publish step queries the Modrinth API and skips if `version_number` already exists.
- Published versions are not deleted. A broken build is superseded by a patch release.
- To retry a failed publish, use `gh run rerun <run-id> --failed`; the guard makes the step idempotent.

## Backports

- `scripts/backport.sh <commit> <branch> [branch...]` cherry-picks with `-x` and pushes each branch.
- Mapping differences between Minecraft lines can cause conflicts; resolve them on the affected branch and push manually.
- Backport only fixes, not features, unless the feature is explicitly requested for that line.

## Historical tags

The `v1.0.0+1.x` and `v1.0.1+1.x` tags originally pointed at the default branch because the old workflow did not set `target_commitish`. They were repaired to their release commits. New releases set `target_commitish` explicitly.
