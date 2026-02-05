package com.example.newoasa

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

// Define a nice OASA-like Blue color
val OasaBlue = Color(0xFF0054A6)
val OasaBlueContainer = Color(0xFFD6E4F7)
val OasaOnBlueContainer = Color(0xFF001C3D)

private val LightColors = lightColorScheme(
    primary = OasaBlue,
    onPrimary = Color.White,
    primaryContainer = OasaBlueContainer,
    onPrimaryContainer = OasaOnBlueContainer,
    secondary = OasaBlue,
    onSecondary = Color.White,
    tertiary = Color(0xFF0077CC) // A slightly lighter blue for accents
)

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = LightColors
    ) {
        MapScreen()
    }
}
