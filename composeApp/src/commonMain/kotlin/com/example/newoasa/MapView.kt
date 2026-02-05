package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.MapLibreMap
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.model.CameraPosition
import org.maplibre.compose.model.LatLng
import org.maplibre.compose.settings.UiSettings

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    // OpenFreeMap Styles
    val styleUrl = if (isDark) "https://tiles.openfreemap.org/styles/dark" else "https://tiles.openfreemap.org/styles/positron"

    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = LatLng(37.9838, 23.7275),
            zoom = 12.0
        )
    )

    MapLibreMap(
        modifier = modifier.fillMaxSize(),
        styleUri = styleUrl,
        cameraState = cameraState,
        uiSettings = UiSettings(
            attributionEnabled = true,
            logoEnabled = false,
            compassEnabled = false
        )
    )
}
