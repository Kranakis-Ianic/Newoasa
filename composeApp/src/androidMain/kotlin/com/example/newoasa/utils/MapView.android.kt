package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newoasa.data.TransitLine
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView as MapLibreMapView
import com.mapbox.mapboxsdk.maps.Style

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val context = LocalContext.current
    
    // Initialize MapLibre
    LaunchedEffect(Unit) {
        Mapbox.getInstance(context)
    }
    
    val mapView = remember {
        MapLibreMapView(context).apply {
            getMapAsync { map ->
                // Set map style based on theme
                val styleUrl = if (isDark) {
                    Style.DARK
                } else {
                    Style.MAPBOX_STREETS
                }
                
                map.setStyle(styleUrl) { style ->
                    // Center on Athens, Greece
                    val athensCenter = LatLng(37.9838, 23.7275)
                    val cameraPosition = CameraPosition.Builder()
                        .target(athensCenter)
                        .zoom(11.0)
                        .build()
                    
                    map.cameraPosition = cameraPosition
                    onMapReady()
                }
            }
        }
    }
    
    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            // Update map style when theme changes
            view.getMapAsync { map ->
                val styleUrl = if (isDark) Style.DARK else Style.MAPBOX_STREETS
                if (map.style?.uri != styleUrl) {
                    map.setStyle(styleUrl)
                }
            }
        }
    )
}
