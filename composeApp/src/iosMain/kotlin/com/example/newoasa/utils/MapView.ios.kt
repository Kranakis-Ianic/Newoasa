package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.rememberCameraState
import org.maplibre.compose.settings.MapControls
import org.maplibre.geojson.Point

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val cameraState = rememberCameraState(
        initialCenter = Point.fromLngLat(23.7275, 37.9838), // Athens
        initialZoom = 11.0
    )

    val styleUrl = if (isDark) {
        "https://tiles.openfreemap.org/styles/dark"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    MaplibreMap(
        modifier = modifier,
        styleUrl = styleUrl,
        cameraState = cameraState,
        mapControls = MapControls(
            isCompassEnabled = true,
            isLogoEnabled = false,
            isAttributionEnabled = true
        )
    )

    LaunchedEffect(Unit) {
        onMapReady()
    }
}