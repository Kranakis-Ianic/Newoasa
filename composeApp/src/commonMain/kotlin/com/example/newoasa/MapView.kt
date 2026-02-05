package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Try multiple likely locations based on KMP structure
// import dev.sargunv.maplibrecompose.MapLibre
// import dev.sargunv.maplibrecompose.compose.MapLibre
import dev.sargunv.maplibrecompose.compose.MapLibre

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    MapLibre(
        modifier = modifier.fillMaxSize(),
        style = styleUrl
    )
}
