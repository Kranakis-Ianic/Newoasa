package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.map.MapLibreMap
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.geometry.LatLng
import org.maplibre.compose.settings.UiSettings

// If LatLng is not found, it might be 'Position'
// But commonly it's LatLng in map libs. 
// Result 166 said 'val target: Position'. So I should check if I can import Position.
// I will try LatLng first as it's more standard, but if it fails I'll swap to Position in next turn if needed.
// Actually, I'll check if I can assume `org.maplibre.compose.model.LatLng` was wrong.
// Previous error: `Unresolved reference 'model'`. So `org.maplibre.compose.model` does not exist.
// Previous error: `Unresolved reference 'MapLibreMap'` when I imported `org.maplibre.compose.MapLibreMap`. So it's not in root.
// So `org.maplibre.compose.map.MapLibreMap` is a good guess.

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
