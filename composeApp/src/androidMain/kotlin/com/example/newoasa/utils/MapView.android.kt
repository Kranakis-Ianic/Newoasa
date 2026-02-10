package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.TransitLine
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView as MapLibreMapView

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Initialize MapLibre instance
    remember {
        MapLibre.getInstance(context)
    }
    
    val mapView = remember {
        MapLibreMapView(context).apply {
            getMapAsync { map ->
                // Use OpenFreeMap style based on theme
                val styleUrl = if (isDark) {
                    "https://tiles.openfreemap.org/styles/dark"
                } else {
                    "https://tiles.openfreemap.org/styles/bright"
                }
                
                map.setStyle(styleUrl) {
                    // Center on Athens, Greece
                    val athensCenter = LatLng(37.9838, 23.7275)
                    val position = CameraPosition.Builder()
                        .target(athensCenter)
                        .zoom(11.0)
                        .build()
                    
                    map.cameraPosition = position
                    onMapReady()
                }
            }
        }
    }
    
    // Handle lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }
    
    // Update map style when theme changes
    DisposableEffect(isDark) {
        mapView.getMapAsync { map ->
            val styleUrl = if (isDark) {
                "https://tiles.openfreemap.org/styles/dark"
            } else {
                "https://tiles.openfreemap.org/styles/bright"
            }
            map.setStyle(styleUrl)
        }
        onDispose { }
    }
    
    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
