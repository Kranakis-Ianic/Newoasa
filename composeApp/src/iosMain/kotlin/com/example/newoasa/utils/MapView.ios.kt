package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MapLibre
import org.maplibre.compose.camera.MapViewCamera
import org.maplibre.compose.ramani.MapLibreComposable

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    // Athens center coordinates
    val athensCenter = remember { 
        org.maplibre.geojson.Point.fromLngLat(23.7275, 37.9838)
    }
    
    val camera = remember {
        MapViewCamera(
            center = athensCenter,
            zoom = 11.0
        )
    }
    
    // Map style based on theme
    val styleUrl = if (isDark) {
        "https://demotiles.maplibre.org/style.json" // MapLibre demo dark style
    } else {
        "https://demotiles.maplibre.org/style.json" // MapLibre demo light style
    }
    
    LaunchedEffect(Unit) {
        onMapReady()
    }
    
    MapLibreComposable(
        modifier = modifier.fillMaxSize(),
        styleUrl = styleUrl,
        camera = camera
    ) {
        // Map content and layers can be added here
        // e.g., markers, polylines for transit routes, etc.
    }
}
