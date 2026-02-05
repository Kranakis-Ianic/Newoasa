package com.example.newoasa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean
) {
    val context = LocalContext.current
    
    // Initialize MapLibre
    remember {
        MapLibre.getInstance(context)
    }

    // Styles for OpenFreeMap
    val lightStyle = "https://tiles.openfreemap.org/styles/positron/style.json"
    val darkStyle = "https://tiles.openfreemap.org/styles/dark/style.json"
    
    // Keep reference to the map object to update style
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }

    val mapView = remember {
        MapView(context).apply {
            // Configuration handled in getMapAsync
        }
    }

    // Handle Style Switching
    LaunchedEffect(isDark, mapRef.value) {
        mapRef.value?.setStyle(if (isDark) darkStyle else lightStyle)
    }

    DisposableEffect(mapView) {
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { 
                mapView.apply {
                    getMapAsync { map ->
                        mapRef.value = map
                        
                        // Initial Camera Position (Athens)
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(37.9838, 23.7275))
                            .zoom(12.0)
                            .build()
                        
                        // Disable default UI to use custom buttons
                        map.uiSettings.isLogoEnabled = false 
                        map.uiSettings.isAttributionEnabled = true
                        map.uiSettings.isCompassEnabled = false
                        map.uiSettings.isRotateGesturesEnabled = true
                        map.uiSettings.isTiltGesturesEnabled = true
                        
                        // Set initial style
                        map.setStyle(if (isDark) darkStyle else lightStyle)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Custom Zoom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { 
                     val currentZoom = mapRef.value?.cameraPosition?.zoom ?: 12.0
                     mapRef.value?.animateCamera(
                         org.maplibre.android.camera.CameraUpdateFactory.newLatLngZoom(
                             mapRef.value?.cameraPosition?.target ?: LatLng(37.9838, 23.7275),
                             currentZoom + 1
                         )
                     )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }
            
            SmallFloatingActionButton(
                onClick = { 
                     val currentZoom = mapRef.value?.cameraPosition?.zoom ?: 12.0
                     mapRef.value?.animateCamera(
                         org.maplibre.android.camera.CameraUpdateFactory.newLatLngZoom(
                             mapRef.value?.cameraPosition?.target ?: LatLng(37.9838, 23.7275),
                             currentZoom - 1
                         )
                     )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }
        }
    }
}
