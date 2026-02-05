package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sargunv.maplibrecompose.MapLibre
import dev.sargunv.maplibrecompose.camera.rememberCameraState

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    // OpenFreeMap Styles
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    // Minimal usage to verify imports
    MapLibre(
        modifier = modifier.fillMaxSize(),
        styleUrl = styleUrl
    )
}
