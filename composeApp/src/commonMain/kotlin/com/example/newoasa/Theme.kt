package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color.002e67,
    onPrimary = Color.White,
    primaryContainer = Color.002e67,
    onPrimaryContainer = Color.002e67,
    secondary = Color.002e67,
    onSecondary = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color.,
    onPrimary = Color.Blue,
    primaryContainer = Color.Blue,
    onPrimaryContainer = Color.Blue,
    secondary = Color.Blue,
    onSecondary = Color.Blue,
    background = Color.Blue,
    surface = Color.Blue,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun NewOasaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
