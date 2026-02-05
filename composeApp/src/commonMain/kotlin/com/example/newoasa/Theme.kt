package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// We define a single, fixed color scheme that ignores system light/dark mode for the UI.
// The UI will ALWAYS be Deep Blue with White text.
private val OasaFixedColors = darkColorScheme(
    primary = OasaDeepBlue,
    onPrimary = OasaWhite,
    primaryContainer = OasaDeepBlue,
    onPrimaryContainer = OasaWhite,
    
    secondary = OasaAccentBlue,
    onSecondary = OasaWhite,
    
    background = OasaDeepBlue,
    onBackground = OasaWhite,
    
    surface = OasaDeepBlue,
    onSurface = OasaWhite,
    
    surfaceVariant = OasaDeepBlue,
    onSurfaceVariant = OasaWhiteTransparent
)

@Composable
fun NewOasaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(), // Parameter kept for compatibility but ignored for colors
    content: @Composable () -> Unit
) {
    // We always use the fixed scheme for the UI
    MaterialTheme(
        colorScheme = OasaFixedColors,
        content = content
    )
}
