#!/usr/bin/env python3
"""
Cleanup script to remove duplicate files after reorganization.
Run this to delete old files that have been moved to organized folders.
"""

import subprocess
import sys

# Files to delete (already moved to organized folders)
FILES_TO_DELETE = [
    "composeApp/src/commonMain/kotlin/com/example/newoasa/AppDrawer.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/BackHandler.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/BottomNavBar.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/Color.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/FavoritesScreen.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/LineCard.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/LineColors.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/MapView.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/PreferenceStorageProvider.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/StopInfoCard.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/Theme.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/ThemeOption.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/TopSearchBar.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/TransitLineItem.kt",
    "composeApp/src/commonMain/kotlin/com/example/newoasa/TripScreen.kt",
]

def main():
    print("Cleaning up duplicate files after reorganization...")
    print(f"Files to delete: {len(FILES_TO_DELETE)}")
    print()
    
    for file_path in FILES_TO_DELETE:
        print(f"Deleting: {file_path}")
        try:
            subprocess.run(["git", "rm", file_path], check=True)
        except subprocess.CalledProcessError as e:
            print(f"  ⚠️  Error deleting {file_path}: {e}")
    
    print()
    print("✅ Cleanup complete!")
    print("Run 'git commit -m \"Remove duplicate files after reorganization\"' to commit changes.")

if __name__ == "__main__":
    main()
