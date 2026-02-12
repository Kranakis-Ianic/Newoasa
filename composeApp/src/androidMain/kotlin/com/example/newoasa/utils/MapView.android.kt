package com.example.newoasa.utils

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.Station
import com.example.newoasa.data.TransitLine
import com.example.newoasa.presentation.components.StationCard
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
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.android.style.expressions.Expression.*
import org.maplibre.geojson.Point

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
    var selectedStation by remember { mutableStateOf<Station?>(null) }
    
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
                    
                    // Load all transit lines and stations
                    coroutineScope.launch {
                        loadAllTransitLines(style, isDark)
                        loadAllStations(style, isDark)
                    }
                    
                    // Set up click listener
                    setupStationClickListener(map) { station ->
                        selectedStation = station
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
                // Reload all transit lines and stations after style change
                coroutineScope.launch {
                    loadAllTransitLines(style, isDark)
                    loadAllStations(style, isDark)
                }
                
                // Re-setup click listener after style change
                setupStationClickListener(map) { station ->
                    selectedStation = station
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
    
    // Render map with StationCard overlay
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
        
        // Show StationCard when a station is selected
        selectedStation?.let { station ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
            ) {
                StationCard(
                    station = station,
                    onDismiss = { selectedStation = null },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

/**
 * Set up click listener for stations with larger touch target
 */
private fun setupStationClickListener(
    map: MapLibreMap,
    onStationClick: (Station) -> Unit
) {
    map.addOnMapClickListener { point ->
        Log.d("MapView", "Map clicked at: $point")
        
        val screenPoint = map.projection.toScreenLocation(point)
        Log.d("MapView", "Screen point: $screenPoint")
        
        // Query with a larger radius for easier tapping (20 pixel radius)
        val buffer = 20f
        val rectF = android.graphics.RectF(
            screenPoint.x - buffer,
            screenPoint.y - buffer,
            screenPoint.x + buffer,
            screenPoint.y + buffer
        )
        
        val features = map.queryRenderedFeatures(rectF, "all-stations-layer")
        Log.d("MapView", "Found ${features.size} features")
        
        if (features.isNotEmpty()) {
            val feature = features.first()
            val geometry = feature.geometry()
            
            if (geometry is Point) {
                try {
                    val properties = feature.properties()
                    
                    // Extract station information
                    val stationId = properties?.get("@id")?.asString ?: ""
                    val stationName = properties?.get("name")?.asString ?: "Unknown Station"
                    val stationNameEn = properties?.get("name:en")?.asString
                    val stationIntName = properties?.get("int_name")?.asString
                    val wheelchair = properties?.get("wheelchair")?.asString == "yes"
                    
                    // Use English or international name if available
                    val displayNameEn = stationNameEn ?: stationIntName
                    
                    // Extract lines from @relations
                    val lines = mutableListOf<String>()
                    try {
                        val relations = properties?.get("@relations")?.asJsonArray
                        relations?.forEach { relation ->
                            val reltags = relation.asJsonObject.get("reltags")?.asJsonObject
                            val ref = reltags?.get("ref")?.asString
                            if (ref != null && !lines.contains(ref)) {
                                lines.add(ref)
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("MapView", "Error extracting lines from station", e)
                    }
                    
                    val station = Station(
                        id = stationId,
                        name = stationName,
                        nameEn = displayNameEn,
                        latitude = geometry.latitude(),
                        longitude = geometry.longitude(),
                        lines = lines.sorted(), // Sort for consistent display
                        wheelchair = wheelchair
                    )
                    
                    Log.d("MapView", "Station clicked: $stationName with lines: $lines")
                    onStationClick(station)
                    return@addOnMapClickListener true
                } catch (e: Exception) {
                    Log.e("MapView", "Error parsing station data", e)
                }
            }
        }
        false
    }
}

/**
 * Load and display all transit lines from final_all_lines.geojson
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
                        // Remove old layer first
                        if (style.getLayer("all-lines-layer") != null) {
                            style.removeLayer("all-lines-layer")
                        }
                        // Then remove source
                        style.removeSource("all-lines-source")
                        Log.d("MapView", "Removed existing all-lines source and layer")
                    }
                    
                    // Add source for all lines
                    val source = GeoJsonSource("all-lines-source", allLinesJson)
                    style.addSource(source)
                    
                    // Add line layer with data-driven styling based on 'colour' property
                    val lineLayer = LineLayer("all-lines-layer", "all-lines-source")
                        .withProperties(
                            // Use the colour property from the GeoJSON features
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
                    
                    Log.d("MapView", "Successfully loaded all transit lines")
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
 * Load and display all stations from final_all_stations.geojson
 */
private suspend fun loadAllStations(style: Style, isDark: Boolean) {
    try {
        // Load the stations file
        val allStationsJson = loadGeoJsonFromResources("files/geojson/final_all_stations.geojson")
        
        if (allStationsJson != null) {
            withContext(Dispatchers.Main) {
                try {
                    // Check if source already exists and remove it first
                    if (style.getSource("all-stations-source") != null) {
                        // Remove old layer first
                        if (style.getLayer("all-stations-layer") != null) {
                            style.removeLayer("all-stations-layer")
                        }
                        // Then remove source
                        style.removeSource("all-stations-source")
                        Log.d("MapView", "Removed existing all-stations source and layer")
                    }
                    
                    // Add source for all stations
                    val source = GeoJsonSource("all-stations-source", allStationsJson)
                    style.addSource(source)
                    
                    // Add circle layer for all stations
                    val stationColor = if (isDark) "#FFFFFF" else "#333333"
                    val stationStrokeColor = if (isDark) "#333333" else "#FFFFFF"
                    
                    val stationsLayer = CircleLayer("all-stations-layer", "all-stations-source")
                        .withProperties(
                            circleRadius(
                                interpolate(
                                    exponential(1.5f),
                                    zoom(),
                                    stop(10f, 3f),   // Slightly larger at zoom 10
                                    stop(12f, 4.5f), // Medium at zoom 12
                                    stop(14f, 6f),   // Larger at zoom 14
                                    stop(16f, 8f)    // Largest at zoom 16
                                )
                            ),
                            circleColor(stationColor),
                            circleStrokeWidth(
                                interpolate(
                                    exponential(1.5f),
                                    zoom(),
                                    stop(10f, 1f),
                                    stop(12f, 1.5f),
                                    stop(14f, 2f),
                                    stop(16f, 2.5f)
                                )
                            ),
                            circleStrokeColor(stationStrokeColor),
                            circleOpacity(0.9f)
                        )
                        .withFilter(
                            eq(geometryType(), literal("Point"))
                        )
                    style.addLayer(stationsLayer)
                    
                    Log.d("MapView", "Successfully loaded all stations")
                } catch (e: Exception) {
                    Log.e("MapView", "Error adding all stations to map", e)
                }
            }
        } else {
            Log.w("MapView", "Could not load final_all_stations.geojson")
        }
    } catch (e: Exception) {
        Log.e("MapView", "Error loading all stations", e)
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
