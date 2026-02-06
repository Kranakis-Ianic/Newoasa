package com.example.newoasa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.TransitLine
import org.json.JSONObject
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Initialize MapLibre
    remember { MapLibre.getInstance(context) }

    // Use OpenFreeMap bright theme for light mode, dark theme for dark mode
    val styleUrl = if (isDark) {
        "https://tiles.openfreemap.org/styles/dark"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    val mapView = remember { 
        MapView(context)
    }

    // Lifecycle management
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
        }
    }

    // Load and display selected line
    LaunchedEffect(selectedLine) {
        if (selectedLine != null) {
            mapView.getMapAsync { map ->
                displayTransitLine(context, map, selectedLine)
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { mv ->
            mv.getMapAsync { map ->
                // Update style based on theme
                map.setStyle(styleUrl) { style ->
                    onMapReady()
                    
                    // Display selected line if present
                    if (selectedLine != null) {
                        displayTransitLine(context, map, selectedLine)
                    }
                }
                
                // Set initial position (Athens, Greece) only if no line is selected
                if (selectedLine == null) {
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(37.9838, 23.7275))
                        .zoom(12.0)
                        .build()
                }
                    
                // Enable attribution and logo (required for OpenFreeMap)
                map.uiSettings.isAttributionEnabled = false
                map.uiSettings.isLogoEnabled = false
            }
        }
    )
}

private fun displayTransitLine(
    context: android.content.Context,
    map: MapLibreMap,
    line: TransitLine
) {
    map.getStyle { style ->
        // Remove previous transit line layers and sources
        for (i in 0 until 10) {
            style.getLayer("transit-line-layer-$i")?.let { style.removeLayer(it) }
            style.getSource("transit-line-source-$i")?.let { style.removeSource(it) }
        }
        
        val allCoordinates = mutableListOf<LatLng>()
        
        // Load and display each route
        line.routePaths.forEachIndexed { index, path ->
            try {
                // Load GeoJSON from assets
                val geoJsonString = loadGeoJsonFromAssets(context, path)
                val geoJson = JSONObject(geoJsonString)
                
                // Extract coordinates for bounds calculation
                extractCoordinates(geoJson)?.let { coords ->
                    allCoordinates.addAll(coords)
                }
                
                // Add source
                val sourceId = "transit-line-source-$index"
                val source = GeoJsonSource(sourceId, geoJsonString)
                style.addSource(source)
                
                // Add line layer
                val layerId = "transit-line-layer-$index"
                val lineColor = if (line.isBus) "#2196F3" else "#9C27B0" // Blue for buses, Purple for trolleys
                val lineLayer = LineLayer(layerId, sourceId).withProperties(
                    PropertyFactory.lineColor(lineColor),
                    PropertyFactory.lineWidth(5f),
                    PropertyFactory.lineOpacity(0.8f)
                )
                style.addLayer(lineLayer)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Animate camera to show all routes
        if (allCoordinates.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            allCoordinates.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            
            // Animate to bounds with padding
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 100),
                1000 // 1 second animation
            )
        }
    }
}

private fun loadGeoJsonFromAssets(context: android.content.Context, path: String): String {
    return try {
        val inputStream = context.assets.open(path)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        "{}" // Return empty GeoJSON on error
    }
}

private fun extractCoordinates(geoJson: JSONObject): List<LatLng>? {
    return try {
        val coordinates = mutableListOf<LatLng>()
        val geometry = geoJson.optJSONObject("geometry")
        val coordsArray = geometry?.optJSONArray("coordinates")
        
        coordsArray?.let {
            for (i in 0 until it.length()) {
                val coord = it.getJSONArray(i)
                val lng = coord.getDouble(0)
                val lat = coord.getDouble(1)
                coordinates.add(LatLng(lat, lng))
            }
        }
        
        coordinates.takeIf { it.isNotEmpty() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
