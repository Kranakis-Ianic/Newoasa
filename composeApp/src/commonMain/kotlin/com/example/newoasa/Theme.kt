package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    onPrimary = OasaOnPrimaryDark,
    primaryContainer = OasaPrimaryContainerDark,
    onPrimaryContainer = OasaOnPrimaryContainerDark,
    secondary = OasaBlueDark,
    onSecondary = OasaOnPrimaryDark,
    background = DeepMidnightBlue,
    surface = DarkSurfaceBlue,
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
