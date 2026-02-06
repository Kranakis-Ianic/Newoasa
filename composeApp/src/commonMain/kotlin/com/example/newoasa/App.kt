package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.newoasa.data.ThemePreferences
import com.example.newoasa.data.rememberThemeMode

@Composable
@Preview
fun App() {
    // Get platform-specific preference storage
    val preferenceStorage = rememberPreferenceStorage()
    
    // Create theme preferences manager
    val themePreferences = androidx.compose.runtime.remember { 
        ThemePreferences(preferenceStorage) 
    }
    
    // Load saved theme mode and persist changes automatically
    var themeMode by rememberThemeMode(themePreferences)
    
    // Calculate if we should use Dark Theme features (e.g. Map style)
    // Note: The UI Colors are fixed Blue/White, but this boolean controls the Map layer.
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.Auto -> isSystemDark
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }

    NewOasaTheme(useDarkTheme = isDark) {
        MapScreen(
            currentThemeMode = themeMode,
            onThemeChange = { themeMode = it },
            isDark = isDark
        )
    }
}
//1