package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
// Update Import: Use MapLibreMap instead of MapLibre
import org.maplibre.compose.MapLibreMap
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraPositionState
import org.maplibre.compose.geometry.LatLng

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val styleUrl = if (isDark) {
        "https://tiles.openfreemap.org/styles/dark"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            target = LatLng(37.9838, 23.7275),
            zoom = 11.0
        )
    }

    // Correct Composable Name: MapLibreMap
    MapLibreMap(
        modifier = modifier,
        style = styleUrl,
        cameraPositionState = cameraPositionState
    )

    // Notify that map is ready
    LaunchedEffect(Unit) {
        onMapReady()
    }
}