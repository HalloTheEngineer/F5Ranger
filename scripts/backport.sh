#!/usr/bin/env bash
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"

if [ $# -lt 2 ]; then
    echo "usage: $0 <commit> <branch> [branch...]"
    exit 1
fi

COMMIT=$1
shift

if [ -n "$(git status --porcelain)" ]; then
    echo "error: working tree is not clean"
    exit 1
fi

ORIGINAL=$(git rev-parse --abbrev-ref HEAD)
restore() {
    git checkout "$ORIGINAL" >/dev/null 2>&1 || true
}
trap restore EXIT

for BRANCH in "$@"; do
    echo "=== backporting $COMMIT to $BRANCH ==="
    git fetch origin "$BRANCH"
    git checkout -B "$BRANCH" "origin/$BRANCH"

    if ! git cherry-pick -x "$COMMIT"; then
        echo "error: conflicts on $BRANCH; resolve manually, then push"
        exit 1
    fi

    git push origin "$BRANCH"
done
