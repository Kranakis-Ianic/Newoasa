package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
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

    // 2. Fix Composable Name:
    MapLibreMap(
        modifier = modifier,
        style = styleUrl,
        cameraPositionState = cameraPositionState
    )

    LaunchedEffect(Unit) {
        onMapReady()
    }
}