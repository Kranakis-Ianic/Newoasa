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

// Dark Theme Colors
// Primary is lighter for visibility on dark backgrounds
val OasaBlueDark = Color(0xFF64B5F6) 
// Deep Midnight Blue for Background (requested "Dark Blue" app color)
val DeepMidnightBlue = Color(0xFF0A1A2F)
val DarkSurfaceBlue = Color(0xFF112240)

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
    primaryContainer = Color(0xFF004085),
    onPrimaryContainer = Color(0xFFD6E4F7),
    secondary = OasaBlueDark,
    onSecondary = Color(0xFF002B5E),
    background = DeepMidnightBlue, // Dark Blue background
    surface = DarkSurfaceBlue,     // Slightly lighter blue for cards/sheets
    onBackground = Color.White,
    onSurface = Color.White
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
