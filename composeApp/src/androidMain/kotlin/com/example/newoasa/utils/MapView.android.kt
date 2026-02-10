package com.example.newoasa.utils

import android.util.Log
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
import com.example.newoasa.theme.LineColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapView as MapLibreMapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.android.style.expressions.Expression.*

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
                
                map.setStyle(styleUrl) { style ->
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
    
    // Handle selected line changes - load and display routes
    LaunchedEffect(selectedLine) {
        if (selectedLine != null) {
            withContext(Dispatchers.IO) {
                // Load all GeoJSON files for this line
                val routeData = mutableListOf<Pair<Int, String>>()
                
                selectedLine.routePaths.forEachIndexed { index, routePath ->
                    try {
                        val geoJsonString = loadGeoJsonFromResources(routePath)
                        if (geoJsonString != null) {
                            routeData.add(index to geoJsonString)
                        } else {
                            Log.w("MapView", "Failed to load GeoJSON from $routePath")
                        }
                    } catch (e: Exception) {
                        Log.e("MapView", "Error loading route $routePath", e)
                    }
                }
                
                // Display on map
                withContext(Dispatchers.Main) {
                    mapView.getMapAsync { map ->
                        map.getStyle { style ->
                            try {
                                // Clear previous line layers and sources
                                clearTransitLayers(style)
                                
                                // Get line color
                                val lineColor = when (selectedLine.category) {
                                    "metro", "tram" -> LineColors.getHexColorForLine(selectedLine.lineNumber)
                                    else -> LineColors.getHexColorForCategory(selectedLine.category, selectedLine.isBus)
                                }
                                
                                val allCoordinates = mutableListOf<LatLng>()
                                
                                // Display each loaded route
                                routeData.forEach { (index, geoJsonString) ->
                                    try {
                                        val sourceId = "route-source-$index"
                                        val lineLayerId = "route-line-$index"
                                        val stopsLayerId = "route-stops-$index"
                                        
                                        // Add source
                                        val source = GeoJsonSource(sourceId, geoJsonString)
                                        style.addSource(source)
                                        
                                        // Parse to extract coordinates for bounds
                                        try {
                                            val featureCollection = org.maplibre.geojson.FeatureCollection.fromJson(geoJsonString)
                                            featureCollection.features()?.forEach { feature ->
                                                val geometry = feature.geometry()
                                                if (geometry is org.maplibre.geojson.LineString) {
                                                    geometry.coordinates().forEach { point ->
                                                        allCoordinates.add(LatLng(point.latitude(), point.longitude()))
                                                    }
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.w("MapView", "Could not parse coordinates for bounds", e)
                                        }
                                        
                                        // Add line layer for route
                                        val lineLayer = LineLayer(lineLayerId, sourceId)
                                            .withProperties(
                                                lineColor(lineColor),
                                                lineWidth(5f),
                                                lineOpacity(0.85f),
                                                lineCap("round"),
                                                lineJoin("round")
                                            )
                                            .withFilter(
                                                eq(geometryType(), literal("LineString"))
                                            )
                                        style.addLayer(lineLayer)
                                        
                                        // Add circle layer for stops
                                        val circleLayer = CircleLayer(stopsLayerId, sourceId)
                                            .withProperties(
                                                circleRadius(6f),
                                                circleColor(lineColor),
                                                circleStrokeWidth(2.5f),
                                                circleStrokeColor("#FFFFFF"),
                                                circleOpacity(1.0f)
                                            )
                                            .withFilter(
                                                eq(geometryType(), literal("Point"))
                                            )
                                        style.addLayer(circleLayer)
                                        
                                        Log.d("MapView", "Successfully added route $index for line ${selectedLine.lineNumber}")
                                    } catch (e: Exception) {
                                        Log.e("MapView", "Error displaying route $index", e)
                                    }
                                }
                                
                                // Zoom to fit all routes with better padding
                                if (allCoordinates.isNotEmpty()) {
                                    val boundsBuilder = LatLngBounds.Builder()
                                    allCoordinates.forEach { boundsBuilder.include(it) }
                                    val bounds = boundsBuilder.build()
                                    
                                    // Use larger padding (200px instead of 100px) for better visual framing
                                    map.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(bounds, 200),
                                        1000
                                    )
                                    Log.d("MapView", "Zoomed to bounds for line ${selectedLine.lineNumber}")
                                } else {
                                    Log.w("MapView", "No coordinates found for line ${selectedLine.lineNumber}")
                                }
                            } catch (e: Exception) {
                                Log.e("MapView", "Error displaying transit line", e)
                            }
                        }
                    }
                }
            }
        } else {
            // Clear lines when no line is selected
            mapView.getMapAsync { map ->
                map.getStyle { style ->
                    clearTransitLayers(style)
                    
                    // Return to Athens view
                    val athensCenter = LatLng(37.9838, 23.7275)
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(athensCenter, 11.0),
                        1000
                    )
                }
            }
        }
    }
    
    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

/**
 * Clear all transit line layers and sources from the map
 */
private fun clearTransitLayers(style: Style) {
    // Remove all route layers and sources
    val layersToRemove = mutableListOf<String>()
    val sourcesToRemove = mutableListOf<String>()
    
    style.layers.forEach { layer ->
        if (layer.id.startsWith("route-")) {
            layersToRemove.add(layer.id)
        }
    }
    
    style.sources.forEach { source ->
        if (source.id.startsWith("route-")) {
            sourcesToRemove.add(source.id)
        }
    }
    
    layersToRemove.forEach { style.removeLayer(it) }
    sourcesToRemove.forEach { style.removeSource(it) }
}
