package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
) {
    // Athens coordinates: 37.9838° N, 23.7275° E
    val cameraState = rememberCameraState(
        CameraPosition(
            target = Position(latitude = 37.9838, longitude = 23.7275),
            zoom = 11.0
        )
    )
    
    // Use OpenFreeMap with theme-appropriate style
    val styleUrl = if (isDark) {
        "https://tiles.openfreemap.org/styles/dark"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    MaplibreMap(
        modifier = modifier.fillMaxSize(),
        cameraState = cameraState,
        baseStyle = BaseStyle.Uri(styleUrl)
    )
    
    LaunchedEffect(Unit) {
        onMapReady()
    }
}
