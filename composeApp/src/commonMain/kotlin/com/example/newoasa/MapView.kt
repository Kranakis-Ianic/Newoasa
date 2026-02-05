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
    // OpenFreeMap Styles
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    MaplibreMap(
        modifier = modifier.fillMaxSize(),
        styleUri = styleUrl
    )
}
