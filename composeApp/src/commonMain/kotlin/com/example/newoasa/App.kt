package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    // Hoist the Theme Mode state here
    var themeMode by remember { mutableStateOf(AppThemeMode.Auto) }
    
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