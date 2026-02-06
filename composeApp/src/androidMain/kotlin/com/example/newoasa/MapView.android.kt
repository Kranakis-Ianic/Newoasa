package com.example.newoasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
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
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.layers.SymbolLayer
import newoasa.composeapp.generated.resources.Res

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
                coroutineScope.launch {
                    displayTransitLine(map, selectedLine)
                }
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
                        coroutineScope.launch {
                            displayTransitLine(map, selectedLine)
                        }
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

@OptIn(ExperimentalResourceApi::class)
private suspend fun displayTransitLine(
    map: MapLibreMap,
    line: TransitLine
) = withContext(Dispatchers.Main) {
    map.getStyle { style ->
        // Remove ALL previous transit line layers and sources
        // Clean up with a wider range to ensure all are removed
        for (i in 0 until 50) {
            try {
                style.getLayer("transit-line-layer-$i")?.let { 
                    style.removeLayer(it)
                    println("Removed layer: transit-line-layer-$i")
                }
            } catch (e: Exception) {
                // Ignore if layer doesn't exist
            }
            
            try {
                style.getSource("transit-line-source-$i")?.let { 
                    style.removeSource(it)
                    println("Removed source: transit-line-source-$i")
                }
            } catch (e: Exception) {
                // Ignore if source doesn't exist
            }
        }
        
        // Remove previous stops layer and source
        try {
            style.getLayer("transit-stops-layer")?.let { 
                style.removeLayer(it)
                println("Removed stops layer")
            }
        } catch (e: Exception) {
            // Ignore
        }
        
        try {
            style.getSource("transit-stops-source")?.let { 
                style.removeSource(it)
                println("Removed stops source")
            }
        } catch (e: Exception) {
            // Ignore
        }
        
        val allCoordinates = mutableListOf<LatLng>()
        val allStops = mutableListOf<LatLng>()
        val loadedGeoJsonData = mutableListOf<Pair<Int, String>>() // index to geoJson
        
        // Load all GeoJSON files sequentially in IO dispatcher
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            // Load all routes first
            line.routePaths.forEachIndexed { index, path ->
                try {
                    println("Loading GeoJSON from composeResources: $path")
                    
                    // Load GeoJSON from Compose Multiplatform resources
                    val geoJsonString = loadGeoJsonFromResources(path)
                    
                    // Validate GeoJSON before adding
                    if (geoJsonString.isBlank() || geoJsonString == "{}") {
                        println("Empty GeoJSON for path: $path")
                        return@forEachIndexed
                    }
                    
                    val geoJson = JSONObject(geoJsonString)
                    
                    // Validate that it has required properties
                    if (!geoJson.has("type")) {
                        println("Invalid GeoJSON - missing 'type' property for path: $path")
                        return@forEachIndexed
                    }
                    
                    // Extract coordinates for bounds calculation and stops
                    extractCoordinates(geoJson)?.let { coords ->
                        allCoordinates.addAll(coords)
                        // Add stops (every 10th point along the route for demonstration)
                        coords.filterIndexed { idx, _ -> idx % 10 == 0 }.forEach { stop ->
                            allStops.add(stop)
                        }
                    }
                    
                    // Store loaded data
                    loadedGeoJsonData.add(index to geoJsonString)
                    println("Successfully loaded GeoJSON for route: $path")
                    
                } catch (e: Exception) {
                    println("Error loading route $path: ${e.message}")
                    e.printStackTrace()
                }
            }
            
            // Now add all sources and layers on main thread sequentially
            withContext(Dispatchers.Main) {
                // Add line color based on type
                val lineColor = if (line.isBus) "#2196F3" else "#9C27B0" // Blue for buses, Purple for trolleys
                
                // Add route lines
                loadedGeoJsonData.forEach { (index, geoJsonString) ->
                    try {
                        val sourceId = "transit-line-source-$index"
                        val layerId = "transit-line-layer-$index"
                        
                        // Check if source already exists (safety check)
                        if (style.getSource(sourceId) != null) {
                            println("Source $sourceId already exists, skipping")
                            return@forEach
                        }
                        
                        // Add source
                        val source = GeoJsonSource(sourceId, geoJsonString)
                        style.addSource(source)
                        println("Added source: $sourceId")
                        
                        // Add line layer
                        val lineLayer = LineLayer(layerId, sourceId).withProperties(
                            PropertyFactory.lineColor(lineColor),
                            PropertyFactory.lineWidth(5f),
                            PropertyFactory.lineOpacity(0.8f)
                        )
                        style.addLayer(lineLayer)
                        println("Added layer: $layerId")
                        
                        println("Successfully displayed route with index: $index")
                        
                    } catch (e: Exception) {
                        println("Error adding source/layer for index $index: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                // Add stops as markers
                if (allStops.isNotEmpty()) {
                    try {
                        // Create pin marker bitmap
                        val pinBitmap = createPinBitmap(if (line.isBus) Color.parseColor("#2196F3") else Color.parseColor("#9C27B0"))
                        style.addImage("stop-pin", pinBitmap)
                        
                        // Create GeoJSON features for stops
                        val stopFeatures = allStops.map { stop ->
                            Feature.fromGeometry(Point.fromLngLat(stop.longitude, stop.latitude))
                        }
                        
                        val featureCollection = FeatureCollection.fromFeatures(stopFeatures)
                        
                        // Add stops source
                        val stopsSource = GeoJsonSource("transit-stops-source", featureCollection)
                        style.addSource(stopsSource)
                        println("Added stops source with ${allStops.size} stops")
                        
                        // Add stops layer
                        val stopsLayer = SymbolLayer("transit-stops-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.iconImage("stop-pin"),
                                PropertyFactory.iconSize(0.5f),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true),
                                PropertyFactory.iconAnchor("bottom")
                            )
                        style.addLayer(stopsLayer)
                        println("Added stops layer")
                        
                    } catch (e: Exception) {
                        println("Error adding stops: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                // Animate camera to show all routes
                if (allCoordinates.isNotEmpty()) {
                    try {
                        val boundsBuilder = LatLngBounds.Builder()
                        allCoordinates.forEach { boundsBuilder.include(it) }
                        val bounds = boundsBuilder.build()
                        
                        // Animate to bounds with padding
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 100),
                            1000 // 1 second animation
                        )
                        println("Camera animated to show all routes")
                    } catch (e: Exception) {
                        println("Error animating camera: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

/**
 * Create a custom pin marker bitmap
 */
private fun createPinBitmap(color: Int): Bitmap {
    val size = 80
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    val paint = Paint().apply {
        this.color = color
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    // Draw circle (top part of pin)
    val circleRadius = size / 3f
    canvas.drawCircle(size / 2f, circleRadius, circleRadius, paint)
    
    // Draw triangle (bottom part of pin)
    val path = android.graphics.Path()
    path.moveTo(size / 2f - circleRadius * 0.6f, circleRadius * 1.5f)
    path.lineTo(size / 2f + circleRadius * 0.6f, circleRadius * 1.5f)
    path.lineTo(size / 2f, size * 0.85f)
    path.close()
    canvas.drawPath(path, paint)
    
    // Draw white inner circle
    paint.color = Color.WHITE
    canvas.drawCircle(size / 2f, circleRadius, circleRadius * 0.5f, paint)
    
    return bitmap
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun loadGeoJsonFromResources(path: String): String {
    return try {
        // Path already includes "files/" prefix from TransitLineRepository
        // e.g., "files/geojson/buses/035/route_2953.geojson"
        val bytes = Res.readBytes(path)
        bytes.decodeToString()
    } catch (e: Exception) {
        println("Failed to load GeoJSON from composeResources path: $path")
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
