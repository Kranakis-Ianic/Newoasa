package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.ramani.MapLibre
import org.maplibre.compose.rememberSaveableMapViewCameraState

/**
 * Transit Map View using MapLibre Compose
 * 
 * This view displays an interactive map for visualizing transit routes,
 * stops, and other transit-related data.
 * 
 * Implementation based on City_Transit working pattern.
 */
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
) {
    val cameraState = rememberSaveableMapViewCameraState()

    MaplibreMap(
        modifier = modifier.fillMaxSize(),
        cameraState = cameraState
    )
    
    LaunchedEffect(Unit) {
        onMapReady()
    }

}