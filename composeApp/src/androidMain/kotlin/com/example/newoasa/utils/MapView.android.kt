package com.example.newoasa.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.TransitLine
import com.example.newoasa.theme.LineColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    val coroutineScope = rememberCoroutineScope()
    
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
                    
                    // Load all transit lines
                    coroutineScope.launch {
                        loadAllTransitLines(style, isDark)
                    }
                    
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
    LaunchedEffect(isDark) {
        mapView.getMapAsync { map ->
            val styleUrl = if (isDark) {
                "https://tiles.openfreemap.org/styles/dark"
            } else {
                "https://tiles.openfreemap.org/styles/bright"
            }
            map.setStyle(styleUrl) { style ->
                // Reload all transit lines after style change
                coroutineScope.launch {
                    loadAllTransitLines(style, isDark)
                }
            }
        }
    }
    
    // Handle selected line changes - highlight selected line
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
                
                // Display highlighted line on map
                withContext(Dispatchers.Main) {
                    mapView.getMapAsync { map ->
                        map.getStyle { style ->
                            try {
                                // Clear previous highlight layers
                                clearHighlightLayers(style)
                                
                                // Get line color
                                val lineColor = when (selectedLine.category) {
                                    "metro", "tram" -> LineColors.getHexColorForLine(selectedLine.lineNumber)
                                    else -> LineColors.getHexColorForCategory(selectedLine.category, selectedLine.isBus)
                                }
                                
                                val allCoordinates = mutableListOf<LatLng>()
                                
                                // Display each loaded route as highlight
                                routeData.forEach { (index, geoJsonString) ->
                                    try {
                                        val sourceId = "highlight-source-$index"
                                        val lineLayerId = "highlight-line-$index"
                                        val stopsLayerId = "highlight-stops-$index"
                                        
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
                                        
                                        // Add thicker highlighted line layer
                                        val lineLayer = LineLayer(lineLayerId, sourceId)
                                            .withProperties(
                                                lineColor(lineColor),
                                                lineWidth(8f),  // Thicker for highlight
                                                lineOpacity(0.95f),
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
                                                circleRadius(8f),  // Larger for highlight
                                                circleColor(lineColor),
                                                circleStrokeWidth(3f),
                                                circleStrokeColor("#FFFFFF"),
                                                circleOpacity(1.0f)
                                            )
                                            .withFilter(
                                                eq(geometryType(), literal("Point"))
                                            )
                                        style.addLayer(circleLayer)
                                        
                                        Log.d("MapView", "Successfully highlighted route $index for line ${selectedLine.lineNumber}")
                                    } catch (e: Exception) {
                                        Log.e("MapView", "Error highlighting route $index", e)
                                    }
                                }
                                
                                // Zoom to fit selected line with padding
                                if (allCoordinates.isNotEmpty()) {
                                    val boundsBuilder = LatLngBounds.Builder()
                                    allCoordinates.forEach { boundsBuilder.include(it) }
                                    val bounds = boundsBuilder.build()
                                    
                                    map.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(bounds, 200),
                                        1000
                                    )
                                    Log.d("MapView", "Zoomed to bounds for line ${selectedLine.lineNumber}")
                                }
                            } catch (e: Exception) {
                                Log.e("MapView", "Error highlighting transit line", e)
                            }
                        }
                    }
                }
            }
        } else {
            // Clear highlight when no line is selected
            mapView.getMapAsync { map ->
                map.getStyle { style ->
                    clearHighlightLayers(style)
                    
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
 * Load and display all transit lines from final_all_lines.geojson
 * This is always visible as the base layer
 */
private suspend fun loadAllTransitLines(style: Style, isDark: Boolean) {
    try {
        // Load the combined all lines file
        val allLinesJson = loadGeoJsonFromResources("files/geojson/final_all_lines.geojson")
        
        if (allLinesJson != null) {
            withContext(Dispatchers.Main) {
                try {
                    // Check if source already exists and remove it first
                    if (style.getSource("all-lines-source") != null) {
                        // Remove old layers first
                        listOf("all-lines-layer", "all-stations-layer").forEach { layerId ->
                            if (style.getLayer(layerId) != null) {
                                style.removeLayer(layerId)
                            }
                        }
                        // Then remove source
                        style.removeSource("all-lines-source")
                        Log.d("MapView", "Removed existing all-lines source and layers")
                    }
                    
                    // Add source for all lines
                    val source = GeoJsonSource("all-lines-source", allLinesJson)
                    style.addSource(source)
                    
                    // Add line layer with data-driven styling based on 'colour' property
                    // OpenStreetMap uses 'colour' property (British spelling)
                    val lineLayer = LineLayer("all-lines-layer", "all-lines-source")
                        .withProperties(
                            // Use the colour property from the GeoJSON features
                            // Try 'colour' first, then fall back to 'color' or 'lineColor'
                            lineColor(
                                coalesce(
                                    get("colour"),
                                    get("color"),
                                    get("lineColor"),
                                    literal("#666666")  // Fallback color
                                )
                            ),
                            lineWidth(3.5f),
                            lineOpacity(0.75f),
                            lineCap("round"),
                            lineJoin("round")
                        )
                        .withFilter(
                            eq(geometryType(), literal("LineString"))
                        )
                    style.addLayer(lineLayer)
                    
                    // Add circle layer for all stations
                    val stationColor = if (isDark) "#FFFFFF" else "#333333"
                    val stationStrokeColor = if (isDark) "#333333" else "#FFFFFF"
                    
                    val stationsLayer = CircleLayer("all-stations-layer", "all-lines-source")
                        .withProperties(
                            circleRadius(
                                interpolate(
                                    exponential(1.5f),
                                    zoom(),
                                    stop(10f, 2f),   // Small at zoom 10
                                    stop(12f, 3.5f), // Medium at zoom 12
                                    stop(14f, 5f),   // Larger at zoom 14
                                    stop(16f, 7f)    // Largest at zoom 16
                                )
                            ),
                            circleColor(stationColor),
                            circleStrokeWidth(
                                interpolate(
                                    exponential(1.5f),
                                    zoom(),
                                    stop(10f, 0.5f),
                                    stop(12f, 1f),
                                    stop(14f, 1.5f),
                                    stop(16f, 2f)
                                )
                            ),
                            circleStrokeColor(stationStrokeColor),
                            circleOpacity(0.9f)
                        )
                        .withFilter(
                            eq(geometryType(), literal("Point"))
                        )
                    style.addLayer(stationsLayer)
                    
                    Log.d("MapView", "Successfully loaded all transit lines and stations")
                } catch (e: Exception) {
                    Log.e("MapView", "Error adding all lines to map", e)
                }
            }
        } else {
            Log.w("MapView", "Could not load final_all_lines.geojson")
        }
    } catch (e: Exception) {
        Log.e("MapView", "Error loading all transit lines", e)
    }
}

/**
 * Clear highlight layers (not the base all-lines layer)
 */
private fun clearHighlightLayers(style: Style) {
    val layersToRemove = mutableListOf<String>()
    val sourcesToRemove = mutableListOf<String>()
    
    style.layers.forEach { layer ->
        if (layer.id.startsWith("highlight-")) {
            layersToRemove.add(layer.id)
        }
    }
    
    style.sources.forEach { source ->
        if (source.id.startsWith("highlight-")) {
            sourcesToRemove.add(source.id)
        }
    }
    
    layersToRemove.forEach { style.removeLayer(it) }
    sourcesToRemove.forEach { style.removeSource(it) }
}
