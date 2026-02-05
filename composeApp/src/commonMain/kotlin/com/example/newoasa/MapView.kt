package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sargunv.maplibrecompose.MapLibre
import dev.sargunv.maplibrecompose.camera.rememberCameraState
import dev.sargunv.maplibrecompose.core.CameraPosition

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    // OpenFreeMap Styles
    // Using 'positron' for light and 'dark' for dark mode as per quick start guide suggestions
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    // Initial Athens Coordinates
    // 37.9838° N, 23.7275° E
    // Note: dev.sargunv might use different Position/Point class.
    // If CameraPosition is not found, I'll remove it in next step.
    
    // For 0.10.0, it might use `style` instead of `styleUrl`.
    // I will use `style` parameter string.
    
    MapLibre(
        modifier = modifier.fillMaxSize(),
        style = styleUrl,
        cameraState = rememberCameraState(
            // We'll leave default camera for now to ensure compilation, 
            // then add specific position once we confirm symbols.
        )
    )
}
