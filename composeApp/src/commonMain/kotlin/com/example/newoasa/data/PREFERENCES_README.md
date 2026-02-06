# Theme Persistence System

This document explains how the app remembers your theme preference (Light/Dark/Auto) across app restarts.

## Architecture

The theme persistence system uses a multiplatform architecture:

```
┌────────────────────────────────────┐
│          App.kt (Common)            │
│  - Loads saved theme on startup     │
│  - Auto-saves when theme changes    │
└────────────────────────────────────┘
               │
               │ uses
               ↓
┌────────────────────────────────────┐
│   ThemePreferences (Common)       │
│  - getThemeMode()                   │
│  - setThemeMode()                   │
└────────────────────────────────────┘
               │
               │ uses
               ↓
┌────────────────────────────────────┐
│   PreferenceStorage (Interface)   │
│  - getString() / putString()        │
│  - getBoolean() / putBoolean()      │
│  - getInt() / putInt()              │
└────────────────────────────────────┘
               │
      ┌────────┼────────┐
      │                 │
      ↓                 ↓
┌──────────────┐   ┌──────────────┐
│   Android    │   │   iOS/Other  │
│ SharedPrefs │   │ UserDefaults│
└──────────────┘   └──────────────┘
```

## Files

### Common Code

**`ThemePreferences.kt`**
- Manages theme preferences (get/set)
- Platform-agnostic logic
- Converts between `AppThemeMode` and string values

**`PreferenceStorageProvider.kt` (expect)**
- Declares platform-specific storage requirement
- Each platform must implement this

### Android Implementation

**`AndroidPreferenceStorage.kt`**
- Uses Android's `SharedPreferences`
- Stores data in `newoasa_preferences` file
- Thread-safe with `.apply()`

**`PreferenceStorageProvider.kt` (actual)**
- Provides `AndroidPreferenceStorage`
- Uses `LocalContext` to get application context

## How It Works

### 1. App Startup

```kotlin
// In App.kt
val preferenceStorage = rememberPreferenceStorage()
val themePreferences = remember { ThemePreferences(preferenceStorage) }
var themeMode by rememberThemeMode(themePreferences)
```

- Gets platform-specific storage (SharedPreferences on Android)
- Creates ThemePreferences manager
- Loads saved theme mode from storage
- Defaults to "Auto" if no preference saved

### 2. Theme Changes

When user changes theme in the drawer:

```kotlin
onThemeChange = { themeMode = it }
```

- Updates `themeMode` state
- `LaunchedEffect` in `rememberThemeMode` detects change
- Automatically saves to storage
- No manual save call needed!

### 3. Data Storage

**Storage Key:** `theme_mode`

**Values:**
- `"light"` → Light mode
- `"dark"` → Dark mode
- `"auto"` → Follow system (default)

**Android Location:**
```
/data/data/com.example.newoasa/shared_prefs/newoasa_preferences.xml
```

## Usage Examples

### Get Current Theme

```kotlin
val preferences = ThemePreferences(storage)
val currentTheme = preferences.getThemeMode()
// Returns: AppThemeMode.Light, .Dark, or .Auto
```

### Save Theme

```kotlin
preferences.setThemeMode(AppThemeMode.Dark)
// Saved immediately to storage
```

### Use in Composable

```kotlin
@Composable
fun MyApp() {
    val storage = rememberPreferenceStorage()
    val preferences = remember { ThemePreferences(storage) }
    var theme by rememberThemeMode(preferences)
    
    // theme is loaded from storage
    // Changes are auto-saved
    MaterialTheme(colorScheme = if (theme == Dark) darkColors else lightColors) {
        Content()
    }
}
```

## Adding More Preferences

The `PreferenceStorage` interface supports multiple data types:

```kotlin
// Strings
storage.putString("user_name", "John")
val name = storage.getString("user_name", "Guest")

// Booleans
storage.putBoolean("notifications_enabled", true)
val enabled = storage.getBoolean("notifications_enabled", false)

// Integers
storage.putInt("map_zoom_level", 15)
val zoom = storage.getInt("map_zoom_level", 10)
```

## Platform Support

### ✅ Android
- Fully implemented
- Uses SharedPreferences
- Persists across app restarts
- Survives app updates

### ⚠️ iOS (Future)
To add iOS support:

1. Create `iosMain/kotlin/com/example/newoasa/data/IosPreferenceStorage.kt`
2. Implement using `NSUserDefaults`
3. Create `iosMain/kotlin/com/example/newoasa/PreferenceStorageProvider.kt`

```kotlin
// iOS implementation example
class IosPreferenceStorage : PreferenceStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    
    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }
    
    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }
    // ... other methods
}
```

### 📦 In-Memory Fallback

If platform storage is unavailable:
- Falls back to `InMemoryPreferenceStorage`
- Works but doesn't persist across restarts
- Useful for testing

## Testing

```kotlin
@Test
fun testThemePreferences() {
    val storage = InMemoryPreferenceStorage()
    val prefs = ThemePreferences(storage)
    
    // Test save
    prefs.setThemeMode(AppThemeMode.Dark)
    
    // Test load
    val loaded = prefs.getThemeMode()
    assertEquals(AppThemeMode.Dark, loaded)
}
```

## Benefits

✅ **Seamless UX**: Theme persists across app restarts
✅ **Automatic**: No manual save/load calls needed
✅ **Multiplatform**: Easy to add iOS, Desktop support
✅ **Type-safe**: Uses enum instead of raw strings
✅ **Extensible**: Add more preferences easily
✅ **Testable**: In-memory implementation for tests

## Troubleshooting

**Theme not saving?**
- Check that `rememberThemeMode` is being used
- Verify platform implementation exists
- Check Android permissions (none needed for SharedPreferences)

**Theme resets on update?**
- SharedPreferences survives app updates
- Check if you're clearing app data

**Testing issues?**
- Use `InMemoryPreferenceStorage` for unit tests
- Remember it doesn't persist between test runs
