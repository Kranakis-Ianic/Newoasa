package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.ramani.MapLibre
import org.maplibre.compose.rememberSaveableMapViewCameraState
import org.maplibre.geojson.Point

/**
 * Unified MapView for both Android and iOS platforms
 * Uses MapLibre Compose multiplatform library
 */
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
) {
    val cameraState = rememberSaveableMapViewCameraState(
        initialCenter = Point.fromLngLat(23.7275, 37.9838), // Athens, Greece
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
    // TODO: Add markers for stops
    // TODO: Add route polylines
}