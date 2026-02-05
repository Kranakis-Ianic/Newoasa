package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

// Define OASA Blue colors
val OasaBlue = Color(0xFF0054A6)
val OasaBlueContainer = Color(0xFFD6E4F7)
val OasaOnBlueContainer = Color(0xFF001C3D)

// Dark Theme Colors (Lighter blue for contrast against dark background)
val OasaBlueDark = Color(0xFFA8C8FF)
val OasaBlueContainerDark = Color(0xFF004085)

private val LightColors = lightColorScheme(
    primary = OasaBlue,
    onPrimary = Color.White,
    primaryContainer = OasaBlueContainer,
    onPrimaryContainer = OasaOnBlueContainer,
    secondary = OasaBlue,
    onSecondary = Color.White
)

private val DarkColors = darkColorScheme(
    primary = OasaBlueDark,
    onPrimary = Color(0xFF002B5E),
    primaryContainer = OasaBlueContainerDark,
    onPrimaryContainer = Color(0xFFD6E4F7),
    secondary = OasaBlueDark,
    onSecondary = Color(0xFF002B5E),
    background = Color(0xFF1A1C1E),
    surface = Color(0xFF1A1C1E)
)

@Composable
@Preview
fun App() {
    val isDark = isSystemInDarkTheme()
    val colorScheme = if (isDark) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        MapScreen()
    }
}
