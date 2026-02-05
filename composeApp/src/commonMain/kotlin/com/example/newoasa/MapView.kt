package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.MapLibre
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.style.MapStyle

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val cameraState = rememberCameraState(
        firstPosition = org.maplibre.compose.geometry.LatLng(37.9838, 23.7275),
        firstZoom = 12.0
    )

    // OpenFreeMap Styles
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    MapLibre(
        modifier = modifier.fillMaxSize(),
        style = MapStyle(styleUrl),
        cameraState = cameraState,
        uiSettings = org.maplibre.compose.settings.UiSettings(
            attributionEnabled = true,
            logoEnabled = false,
            compassEnabled = false
        )
    )
}
