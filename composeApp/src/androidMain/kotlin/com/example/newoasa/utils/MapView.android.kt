package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.ramani.MapLibre
import org.maplibre.compose.rememberSaveableMapViewCameraState
import org.maplibre.geojson.Point

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val cameraState = rememberSaveableMapViewCameraState(
        initialCenter = Point.fromLngLat(23.7275, 37.9838), // Athens
        initialZoom = 11.0
    )

    val styleUrl = remember(isDark) {
        if (isDark) {
            "https://tiles.openfreemap.org/styles/dark"
        } else {
            "https://tiles.openfreemap.org/styles/bright"
        }
    }

    MapLibre {
        MaplibreMap(
            modifier = modifier,
            cameraState = cameraState,
            styleUrl = styleUrl
        )
    }

    remember(Unit) {
        onMapReady()
        null
    }
    
    // TODO: Add custom layers for transit lines when selectedLine != null
    // Can reference previous native implementation for full transit visualization
}