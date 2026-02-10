# CommonMain Reorganization

## New Folder Structure

```
composeApp/src/commonMain/kotlin/com/example/newoasa/
├── App.kt                          # Main application entry point
├── data/                           # Data models and repositories
│   ├── PreferenceStorage.kt
│   ├── ThemePreferences.kt
│   └── TransitLineRepository.kt
├── theme/                          # Theme and styling
│   ├── Color.kt
│   ├── LineColors.kt
│   ├── Theme.kt
│   └── ThemeOption.kt
├── ui/                             # UI components
│   ├── components/                 # Reusable UI components
│   │   ├── LineCard.kt
│   │   ├── StopInfoCard.kt
│   │   └── TransitLineItem.kt
│   ├── navigation/                 # Navigation components
│   │   ├── AppDrawer.kt
│   │   ├── BottomNavBar.kt
│   │   └── TopSearchBar.kt
│   └── screens/                    # Screen composables
│       ├── FavoritesScreen.kt
│       ├── MapScreen.kt
│       └── TripScreen.kt
└── utils/                          # Utility classes and expect/actual
    ├── BackHandler.kt
    ├── MapView.kt
    └── PreferenceStorageProvider.kt
```

## Files to Delete (Old Locations)

The following files in the root `com/example/newoasa/` directory should be deleted as they've been moved:

- ❌ `FavoritesScreen.kt` → Moved to `ui/screens/`
- ❌ `TripScreen.kt` → Moved to `ui/screens/`
- ❌ `LineCard.kt` → Moved to `ui/components/`
- ❌ `StopInfoCard.kt` → Moved to `ui/components/`
- ❌ `TransitLineItem.kt` → Moved to `ui/components/`
- ❌ `AppDrawer.kt` → Moved to `ui/navigation/`
- ❌ `BottomNavBar.kt` → Moved to `ui/navigation/`
- ❌ `TopSearchBar.kt` → Moved to `ui/navigation/`
- ❌ `Color.kt` → Moved to `theme/`
- ❌ `LineColors.kt` → Moved to `theme/`
- ❌ `Theme.kt` → Moved to `theme/`
- ❌ `ThemeOption.kt` → Moved to `theme/`
- ❌ `BackHandler.kt` → Moved to `utils/`
- ❌ `PreferenceStorageProvider.kt` → Moved to `utils/`
- ❌ `MapView.kt` → Moved to `utils/`

## Platform-Specific Files to Update

Update imports in the following platform-specific implementations:

### Android (`androidMain/kotlin/com/example/newoasa/`):
- `BackHandler.android.kt` - Change import to `com.example.newoasa.utils`
- `MapView.android.kt` - Change import to `com.example.newoasa.utils`
- `PreferenceStorageProvider.android.kt` - Change import to `com.example.newoasa.utils`

### iOS (`iosMain/kotlin/com/example/newoasa/`):
- `BackHandler.ios.kt` - Change import to `com.example.newoasa.utils`
- `MapView.ios.kt` - Change import to `com.example.newoasa.utils`
- `PreferenceStorageProvider.ios.kt` - Change import to `com.example.newoasa.utils`

## Benefits of New Structure

✅ **Better organization** - Related files grouped together  
✅ **Easier navigation** - Clear separation of concerns  
✅ **Scalability** - Easy to add new screens, components, or utilities  
✅ **Standard Android architecture** - Follows common Compose patterns  
✅ **Improved maintainability** - Logical grouping makes code easier to find and modify
