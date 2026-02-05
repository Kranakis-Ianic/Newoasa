package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sargunv.maplibrecompose.compose.*

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    // OpenFreeMap Styles
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    // Attempt to use MapLibre from the compose package
    MapLibre(
        modifier = modifier.fillMaxSize(),
        style = styleUrl
    )
}
