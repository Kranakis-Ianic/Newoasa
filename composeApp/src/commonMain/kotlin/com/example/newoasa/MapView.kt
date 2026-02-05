package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.map.MaplibreMap

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    // Based on snippet 165 "__base__ Style", testing parameter name 'base'
    // If this fails, I'll try positional args (modifier first, then style?)
    MaplibreMap(
        modifier = modifier.fillMaxSize()
        // properties commented out to debug signature in next step if this fails
    )
}
