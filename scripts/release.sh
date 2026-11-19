#!/usr/bin/env bash
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"

if [ -n "$(git status --porcelain)" ]; then
    echo "error: working tree is not clean"
    exit 1
fi

BRANCH=$(git rev-parse --abbrev-ref HEAD)
CURRENT=$(grep '^mod_version=' gradle.properties | cut -d= -f2 | xargs)
MC=$(grep '^minecraft_version=' gradle.properties | cut -d= -f2 | xargs)
CORE=${CURRENT%%+*}
SUFFIX=${CURRENT#*+}

if [ "$SUFFIX" != "$MC" ]; then
    echo "error: mod_version suffix ($SUFFIX) does not match minecraft_version ($MC)"
    exit 1
fi

if [ "$BRANCH" != "main" ]; then
    case "$MC" in
        "$BRANCH"|"$BRANCH".*) ;;
        *)
            echo "error: branch $BRANCH does not match minecraft_version $MC"
            exit 1
            ;;
    esac
fi

BUMP=${1:-patch}
if [[ "$BUMP" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    NEW_CORE=$BUMP
else
    IFS=. read -r MAJOR MINOR PATCH <<< "$CORE"
    case "$BUMP" in
        major)
            MAJOR=$((MAJOR + 1))
            MINOR=0
            PATCH=0
            ;;
        minor)
            MINOR=$((MINOR + 1))
            PATCH=0
            ;;
        patch)
            PATCH=$((PATCH + 1))
            ;;
        *)
            echo "usage: $0 [patch|minor|major|X.Y.Z]"
            exit 1
            ;;
    esac
    NEW_CORE="$MAJOR.$MINOR.$PATCH"
fi

NEW="$NEW_CORE+$SUFFIX"

if git rev-parse "v$NEW" >/dev/null 2>&1; then
    echo "error: tag v$NEW already exists"
    exit 1
fi

sed -i.bak "s/^mod_version=.*/mod_version=$NEW/" gradle.properties
rm -f gradle.properties.bak

git add gradle.properties
git commit -m "version bump to $NEW_CORE"
git tag -a "v$NEW" -m "v$NEW"
git push origin "$BRANCH"
git push origin "v$NEW"

echo "Released $NEW"
