package com.example.newoasa.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.newoasa.AppThemeMode

/**
 * Manages theme preferences with persistence
 * Uses platform-specific storage (SharedPreferences on Android, UserDefaults on iOS, etc.)
 */
class ThemePreferences(private val storage: PreferenceStorage) {
    
    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val DEFAULT_THEME_MODE = "auto"
    }
    
    /**
     * Get the saved theme mode
     */
    fun getThemeMode(): AppThemeMode {
        val saved = storage.getString(KEY_THEME_MODE, DEFAULT_THEME_MODE)
        return when (saved) {
            "light" -> AppThemeMode.Light
            "dark" -> AppThemeMode.Dark
            else -> AppThemeMode.Auto
        }
    }
    
    /**
     * Save the theme mode
     */
    fun setThemeMode(mode: AppThemeMode) {
        val value = when (mode) {
            AppThemeMode.Light -> "light"
            AppThemeMode.Dark -> "dark"
            AppThemeMode.Auto -> "auto"
        }
        storage.putString(KEY_THEME_MODE, value)
    }
}

/**
 * Platform-agnostic storage interface
 * Implement this for each platform (Android, iOS, etc.)
 */
interface PreferenceStorage {
    fun getString(key: String, defaultValue: String): String
    fun putString(key: String, value: String)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getInt(key: String, defaultValue: Int): Int
    fun putInt(key: String, value: Int)
}

/**
 * In-memory implementation for testing or when platform storage is unavailable
 */
class InMemoryPreferenceStorage : PreferenceStorage {
    private val storage = mutableMapOf<String, Any>()
    
    override fun getString(key: String, defaultValue: String): String {
        return storage[key] as? String ?: defaultValue
    }
    
    override fun putString(key: String, value: String) {
        storage[key] = value
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return storage[key] as? Boolean ?: defaultValue
    }
    
    override fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }
    
    override fun getInt(key: String, defaultValue: Int): Int {
        return storage[key] as? Int ?: defaultValue
    }
    
    override fun putInt(key: String, value: Int) {
        storage[key] = value
    }
}

/**
 * Composable function to remember theme preferences
 */
@Composable
fun rememberThemePreferences(storage: PreferenceStorage = remember { InMemoryPreferenceStorage() }): ThemePreferences {
    return remember { ThemePreferences(storage) }
}

/**
 * Composable state holder for theme mode with persistence
 */
@Composable
fun rememberThemeMode(preferences: ThemePreferences): androidx.compose.runtime.MutableState<AppThemeMode> {
    val themeMode = remember { mutableStateOf(preferences.getThemeMode()) }
    
    // Save theme mode whenever it changes
    LaunchedEffect(themeMode.value) {
        preferences.setThemeMode(themeMode.value)
    }
    
    return themeMode
}
