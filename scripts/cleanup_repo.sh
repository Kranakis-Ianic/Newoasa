#!/usr/bin/env bash
# cleanup_repo.sh — safe repo cleanup helper
# Moves common build and user-cache folders to a timestamped backup directory.
# Usage:
#   ./scripts/cleanup_repo.sh           # dry-run (default)
#   ./scripts/cleanup_repo.sh --run     # perform move to backup
#   ./scripts/cleanup_repo.sh --delete  # permanently delete after backup (use caution)
#   ./scripts/cleanup_repo.sh --help

set -euo pipefail

DRY_RUN=true
DELETE_AFTER_BACKUP=false

for arg in "$@"; do
  case "$arg" in
    --run) DRY_RUN=false ;;
    --delete) DRY_RUN=false; DELETE_AFTER_BACKUP=true ;;
    --help|-h) echo "Usage: $0 [--run] [--delete]"; exit 0 ;;
    *) echo "Unknown arg: $arg"; echo "Usage: $0 [--run] [--delete]"; exit 1 ;;
  esac
done

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="$(pwd)/repo_cleanup_backup/$TIMESTAMP"

# Targets to move/remove
TARGETS=(
  "./build"
  "./composeApp/build"
  "./.gradle"
  "./.idea"
  "./iosApp/iosApp.xcodeproj/xcuserdata"
  "./composeApp/schemas/com.example.newoasa.database.room.TransitDatabase"
)

echo "Backup dir: $BACKUP_DIR"

if [ "$DRY_RUN" = true ]; then
  echo "DRY RUN: no changes will be made. Use --run to perform the moves. Use --delete to also remove after backup."
fi

mkdir -p "$BACKUP_DIR"

for t in "${TARGETS[@]}"; do
  if [ -e "$t" ]; then
    if [ "$DRY_RUN" = true ]; then
      echo "[DRY] Would move: $t -> $BACKUP_DIR/"
    else
      echo "Moving: $t -> $BACKUP_DIR/"
      mv "$t" "$BACKUP_DIR/" || echo "Warning: could not move $t"
    fi
  else
    echo "Not found (skipping): $t"
  fi
done

if [ "$DELETE_AFTER_BACKUP" = true ] && [ "$DRY_RUN" = false ]; then
  echo "Deleting backed up items from backup dir: $BACKUP_DIR"
  rm -rf "$BACKUP_DIR" || echo "Warning: failed to delete $BACKUP_DIR"
fi

echo "Done."

