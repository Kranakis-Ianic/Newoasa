package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.camera.MapViewCameraState
import org.maplibre.compose.map.MapLibreMap
import org.maplibre.compose.settings.Position

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    // Athens center coordinates
    val athensCenter = remember { Position(37.9838, 23.7275) }
    
    // Camera state - centered on Athens with zoom level 11
    val cameraState = remember {
        MapViewCameraState().apply {
            position = athensCenter
            zoom = 11.0
        }
    }
    
    // Map style based on theme
    val styleUrl = if (isDark) {
        "https://demotiles.maplibre.org/style.json" // MapLibre demo style
    } else {
        "https://demotiles.maplibre.org/style.json" // MapLibre demo style
    }
    
    LaunchedEffect(Unit) {
        onMapReady()
    }
    
    MapLibreMap(
        modifier = modifier.fillMaxSize(),
        baseStyle = styleUrl,
        cameraState = cameraState
    ) {
        // Map content (markers, polylines, etc.) can be added here
    }
}
