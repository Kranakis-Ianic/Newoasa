package com.example.newoasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.TransitLine
import com.example.newoasa.data.TransitLineRepository
import com.google.gson.JsonObject
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
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.layers.SymbolLayer
import newoasa.composeapp.generated.resources.Res
import androidx.compose.ui.unit.dp

data class Stop(
    val name: String,
    val stopCode: String,
    val order: String,
    val coordinate: LatLng
)

data class StopInfoState(
    val stop: Stop
)

/**
 * Get the color for a transit line based on its category and line number
 * Returns color in hex format (e.g., "#00734c")
 */
private fun getTransitLineColor(line: TransitLine): String {
    return when {
        line.isMetro -> when (line.lineNumber) {
            "1" -> "#00734c" // Green
            "2" -> "#e60000" // Red
            "3" -> "#002673" // Blue
            else -> "#000000" // Default to black
        }
        line.isTram -> when (line.lineNumber.uppercase()) {
            "T6" -> "#a7c636" // Lime green
            "T7" -> "#b9348b" // Pink
            else -> "#000000" // Default to black
        }
        line.isBus -> "#009cc7" // Cyan
        line.isTrolley -> "#f07c00" // Orange
        else -> "#000000" // Default to black
    }
}

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
    var isMapReady by remember { mutableStateOf(false) }
    var baseLinesLoaded by remember { mutableStateOf(false) }
    var currentDisplayedLine by remember { mutableStateOf<TransitLine?>(null) }
    var allStopsMap by remember { mutableStateOf<Map<String, Stop>>(emptyMap()) }
    var selectedStopInfo by remember { mutableStateOf<StopInfoState?>(null) }
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }
    var mapLibreInstance by remember { mutableStateOf<MapLibreMap?>(null) }
    
    // Initialize MapLibre
    remember { MapLibre.getInstance(context) }

    // Use OpenFreeMap bright theme for light mode, dark theme for dark mode
    val styleUrl = if (isDark) {
        "https://tiles.openfreemap.org/styles/dark"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    val mapView = remember { 
        MapView(context).also { mapViewInstance = it }
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

    // Load base transit lines (metro and tram) after map is ready
    LaunchedEffect(isMapReady) {
        if (isMapReady && !baseLinesLoaded) {
            mapView.getMapAsync { map ->
                coroutineScope.launch {
                    // Load all metro lines
                    val metroLines = TransitLineRepository.getMetroLines()
                    metroLines.forEach { metroLine ->
                        displayPersistentTransitLine(map, metroLine, context, "metro")
                    }
                    
                    // Load all tram lines
                    val tramLines = TransitLineRepository.getTramLines()
                    tramLines.forEach { tramLine ->
                        displayPersistentTransitLine(map, tramLine, context, "tram")
                    }
                    
                    baseLinesLoaded = true
                    println("Base transit lines loaded: ${metroLines.size} metro + ${tramLines.size} tram")
                }
            }
        }
    }

    // Load and display selected line only when it changes and map is ready
    LaunchedEffect(selectedLine, isMapReady) {
        if (selectedLine != null && isMapReady && selectedLine != currentDisplayedLine) {
            mapView.getMapAsync { map ->
                coroutineScope.launch {
                    val stops = displayTransitLine(map, selectedLine, context)
                    allStopsMap = stops
                    currentDisplayedLine = selectedLine
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { mv ->
                mv.getMapAsync { map ->
                    mapLibreInstance = map
                    
                    // Only set up style once
                    if (!isMapReady) {
                        map.setStyle(styleUrl) { style ->
                            isMapReady = true
                            onMapReady()
                            
                            // Add click listener for stop pins
                            map.addOnMapClickListener { point ->
                                val screenPoint = map.projection.toScreenLocation(point)
                                
                                // Check all possible stop hit area layers (metro, tram, and regular transit)
                                val layersToCheck = listOf(
                                    "transit-stops-hit-area",
                                    "metro-1-stops-hit-area",
                                    "metro-2-stops-hit-area",
                                    "metro-3-stops-hit-area",
                                    "tram-T6-stops-hit-area",
                                    "tram-T7-stops-hit-area"
                                )
                                
                                var handled = false
                                for (layerId in layersToCheck) {
                                    val hitAreaFeatures = map.queryRenderedFeatures(screenPoint, layerId)
                                    
                                    if (hitAreaFeatures.isNotEmpty()) {
                                        val feature = hitAreaFeatures[0]
                                        val properties = feature.properties()
                                        
                                        if (properties != null) {
                                            val stopName = properties.get("name")?.asString ?: "Unknown"
                                            val stopCode = properties.get("stop_code")?.asString ?: ""
                                            val order = properties.get("order")?.asString ?: ""
                                            
                                            val geometry = feature.geometry()
                                            if (geometry is Point) {
                                                val stopCoord = LatLng(geometry.latitude(), geometry.longitude())
                                                
                                                val currentZoom = map.cameraPosition.zoom
                                                val newCameraPosition = CameraPosition.Builder()
                                                    .target(stopCoord)
                                                    .zoom(if (currentZoom < 14) 14.0 else currentZoom)
                                                    .build()
                                                
                                                map.animateCamera(
                                                    CameraUpdateFactory.newCameraPosition(newCameraPosition),
                                                    300
                                                )
                                                
                                                coroutineScope.launch {
                                                    kotlinx.coroutines.delay(350)
                                                    selectedStopInfo = StopInfoState(
                                                        stop = Stop(
                                                            name = stopName,
                                                            stopCode = stopCode,
                                                            order = order,
                                                            coordinate = stopCoord
                                                        )
                                                    )
                                                }
                                            }
                                            
                                            handled = true
                                            break
                                        }
                                    }
                                }
                                
                                if (!handled) {
                                    selectedStopInfo = null
                                }
                                
                                handled
                            }
                        }
                        
                        // Set initial position (Athens, Greece)
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(37.9838, 23.7275))
                            .zoom(12.0)
                            .build()
                            
                        // Disable attribution and logo
                        map.uiSettings.isAttributionEnabled = false
                        map.uiSettings.isLogoEnabled = false
                    }
                }
            }
        )
        
        // Info window overlay
        selectedStopInfo?.let { info ->
            StopInfoWindow(
                stop = info.stop,
                onClose = { selectedStopInfo = null }
            )
        }
    }
}

@Composable
fun StopInfoWindow(
    stop: Stop,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 200.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .clickable(onClick = {}),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stop.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Stop Code: ${stop.stopCode}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Order: ${stop.order}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Display a persistent transit line (metro or tram) that stays on the map
 * Also displays stop dots and labels (visible when zoomed in)
 */
@OptIn(ExperimentalResourceApi::class)
private suspend fun displayPersistentTransitLine(
    map: MapLibreMap,
    line: TransitLine,
    context: android.content.Context,
    prefix: String
) = withContext(Dispatchers.Main) {
    map.getStyle { style ->
        val allCoordinates = mutableListOf<LatLng>()
        val allStops = mutableListOf<Stop>()
        val loadedGeoJsonData = mutableListOf<Pair<String, String>>()
        
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            line.routePaths.forEachIndexed { index, path ->
                try {
                    val geoJsonString = loadGeoJsonFromResources(path)
                    
                    if (geoJsonString.isBlank() || geoJsonString == "{}") {
                        return@forEachIndexed
                    }
                    
                    val geoJson = JSONObject(geoJsonString)
                    val features = geoJson.optJSONArray("features")
                    
                    if (features != null) {
                        for (i in 0 until features.length()) {
                            val feature = features.getJSONObject(i)
                            val properties = feature.optJSONObject("properties")
                            val geometry = feature.optJSONObject("geometry")
                            
                            val featureType = properties?.optString("type")
                            val geometryType = geometry?.optString("type")
                            
                            // Handle routes
                            if (geometryType == "LineString" || geometryType == "MultiLineString") {
                                val coords = geometry.optJSONArray("coordinates")
                                coords?.let {
                                    if (geometryType == "LineString") {
                                        for (j in 0 until it.length()) {
                                            val coord = it.getJSONArray(j)
                                            allCoordinates.add(LatLng(coord.getDouble(1), coord.getDouble(0)))
                                        }
                                    } else if (geometryType == "MultiLineString") {
                                        for (k in 0 until it.length()) {
                                            val lineString = it.getJSONArray(k)
                                            for (j in 0 until lineString.length()) {
                                                val coord = lineString.getJSONArray(j)
                                                allCoordinates.add(LatLng(coord.getDouble(1), coord.getDouble(0)))
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // Handle stops
                            val isStop = featureType == "stop" || 
                                        (featureType.isNullOrEmpty() && (geometryType == "Point" || geometryType == "MultiPoint"))
                            
                            if (isStop) {
                                val coordinates = geometry?.optJSONArray("coordinates")
                                if (coordinates != null && coordinates.length() >= 2) {
                                    val lng = coordinates.getDouble(0)
                                    val lat = coordinates.getDouble(1)
                                    val stopName = properties?.optString("name") ?: "Unknown"
                                    val stopCode = properties?.optString("stop_code") ?: ""
                                    val order = properties?.optString("order") ?: ""
                                    
                                    allStops.add(
                                        Stop(
                                            name = stopName,
                                            stopCode = stopCode,
                                            order = order,
                                            coordinate = LatLng(lat, lng)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
                    if (routeOnlyGeoJson != "{}") {
                        val layerId = "${prefix}-${line.lineNumber}-$index"
                        loadedGeoJsonData.add(layerId to routeOnlyGeoJson)
                    }
                } catch (e: Exception) {
                    println("Error loading persistent route $path: ${e.message}")
                }
            }
            
            withContext(Dispatchers.Main) {
                val lineColor = getTransitLineColor(line)
                
                // Add route lines
                loadedGeoJsonData.forEach { (layerId, geoJsonString) ->
                    try {
                        val sourceId = "source-$layerId"
                        
                        if (style.getSource(sourceId) == null) {
                            val source = GeoJsonSource(sourceId, geoJsonString)
                            style.addSource(source)
                            
                            val lineLayer = LineLayer(layerId, sourceId).withProperties(
                                PropertyFactory.lineColor(lineColor),
                                PropertyFactory.lineWidth(4f),
                                PropertyFactory.lineOpacity(0.8f)
                            )
                            style.addLayer(lineLayer)
                        }
                    } catch (e: Exception) {
                        println("Error adding persistent layer: ${e.message}")
                    }
                }
                
                // Add stops if any
                if (allStops.isNotEmpty()) {
                    try {
                        val stopsSourceId = "${prefix}-${line.lineNumber}-stops-source"
                        
                        // Check if stops already added
                        if (style.getSource(stopsSourceId) == null) {
                            val stopFeatures = allStops.map { stop ->
                                val properties = JsonObject()
                                properties.addProperty("name", stop.name)
                                properties.addProperty("stop_code", stop.stopCode)
                                properties.addProperty("order", stop.order)
                                
                                Feature.fromGeometry(
                                    Point.fromLngLat(stop.coordinate.longitude, stop.coordinate.latitude),
                                    properties
                                )
                            }
                            
                            val featureCollection = FeatureCollection.fromFeatures(stopFeatures)
                            val stopsSource = GeoJsonSource(stopsSourceId, featureCollection)
                            style.addSource(stopsSource)
                            
                            // 1. Hit area layer (invisible, for clicking)
                            val hitAreaLayer = CircleLayer(
                                "${prefix}-${line.lineNumber}-stops-hit-area",
                                stopsSourceId
                            ).withProperties(
                                PropertyFactory.circleRadius(15f),
                                PropertyFactory.circleColor(Color.TRANSPARENT),
                                PropertyFactory.circleOpacity(0f)
                            )
                            style.addLayer(hitAreaLayer)
                            
                            // 2. Visible dots layer
                            val dotsLayer = CircleLayer(
                                "${prefix}-${line.lineNumber}-stops-dots",
                                stopsSourceId
                            ).withProperties(
                                PropertyFactory.circleRadius(4f),
                                PropertyFactory.circleColor(Color.WHITE),
                                PropertyFactory.circleStrokeWidth(2f),
                                PropertyFactory.circleStrokeColor(lineColor)
                            )
                            style.addLayer(dotsLayer)
                            
                            // 3. Labels layer (only visible when zoomed in)
                            val labelsLayer = SymbolLayer(
                                "${prefix}-${line.lineNumber}-stops-labels",
                                stopsSourceId
                            ).withProperties(
                                PropertyFactory.textField("{name}"),
                                PropertyFactory.textSize(11f),
                                PropertyFactory.textOffset(arrayOf(0f, 1.5f)),
                                PropertyFactory.textAnchor("top"),
                                PropertyFactory.textColor(Color.parseColor(lineColor)),
                                PropertyFactory.textHaloColor(Color.WHITE),
                                PropertyFactory.textHaloWidth(1.5f)
                            )
                            labelsLayer.minZoom = 14f // Only show when zoomed in
                            style.addLayer(labelsLayer)
                            
                            println("Added ${allStops.size} stops for ${prefix} line ${line.lineNumber}")
                        }
                    } catch (e: Exception) {
                        println("Error adding persistent stops: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun displayTransitLine(
    map: MapLibreMap,
    line: TransitLine,
    context: android.content.Context
): Map<String, Stop> = withContext(Dispatchers.Main) {
    var stopsMap = emptyMap<String, Stop>()
    
    map.getStyle { style ->
        // Remove ONLY temporary transit line layers (not metro/tram base layers)
        for (i in 0 until 50) {
            try {
                val layerId = "transit-line-layer-$i"
                style.getLayer(layerId)?.let { 
                    style.removeLayer(it)
                }
            } catch (e: Exception) {
                // Ignore if layer doesn't exist
            }
            
            try {
                val sourceId = "transit-line-source-$i"
                style.getSource(sourceId)?.let { 
                    style.removeSource(it)
                }
            } catch (e: Exception) {
                // Ignore if source doesn't exist
            }
        }
        
        // Remove previous stops layers and sources
        try {
            style.getLayer("transit-stops-hit-area")?.let { 
                style.removeLayer(it)
            }
        } catch (e: Exception) {
            // Ignore
        }
        
        try {
            style.getLayer("transit-stops-labels-layer")?.let { 
                style.removeLayer(it)
            }
        } catch (e: Exception) {
            // Ignore
        }

        try {
            style.getLayer("transit-stops-dots-layer")?.let { 
                style.removeLayer(it)
            }
        } catch (e: Exception) {
            // Ignore
        }
        
        try {
            style.getSource("transit-stops-source")?.let { 
                style.removeSource(it)
            }
        } catch (e: Exception) {
            // Ignore
        }
        
        val allCoordinates = mutableListOf<LatLng>()
        val allStops = mutableListOf<Stop>()
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
                    
                    // Validate that it has features - type check relaxed to allow FeatureCollection or manual parsing
                    val features = geoJson.optJSONArray("features")
                    if (features != null) {
                        for (i in 0 until features.length()) {
                            val feature = features.getJSONObject(i)
                            val properties = feature.optJSONObject("properties")
                            val geometry = feature.optJSONObject("geometry")
                            
                            val featureType = properties?.optString("type")
                            val geometryType = geometry?.optString("type")
                            
                            // Check for route path (either explicit type or LineString geometry)
                            val isRoute = featureType == "route_path" || 
                                         (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))
                            
                            // Check for stop (either explicit type or Point geometry)
                            val isStop = featureType == "stop" || 
                                        (featureType.isNullOrEmpty() && (geometryType == "Point" || geometryType == "MultiPoint"))

                            if (isRoute) {
                                // Extract route coordinates for bounds calculation
                                val coords = geometry?.optJSONArray("coordinates")
                                coords?.let {
                                    // Handle LineString (array of points)
                                    if (geometryType == "LineString") {
                                        for (j in 0 until it.length()) {
                                            val coord = it.getJSONArray(j)
                                            val lng = coord.getDouble(0)
                                            val lat = coord.getDouble(1)
                                            allCoordinates.add(LatLng(lat, lng))
                                        }
                                    }
                                    // Basic support for MultiLineString if needed
                                    else if (geometryType == "MultiLineString") {
                                         for (k in 0 until it.length()) {
                                            val lineString = it.getJSONArray(k)
                                            for (j in 0 until lineString.length()) {
                                                val coord = lineString.getJSONArray(j)
                                                val lng = coord.getDouble(0)
                                                val lat = coord.getDouble(1)
                                                allCoordinates.add(LatLng(lat, lng))
                                            }
                                         }
                                    }
                                }
                            } else if (isStop) {
                                // Extract stop information
                                val coordinates = geometry?.optJSONArray("coordinates")
                                if (coordinates != null && coordinates.length() >= 2) {
                                    val lng = coordinates.getDouble(0)
                                    val lat = coordinates.getDouble(1)
                                    val stopName = properties?.optString("name") ?: "Unknown"
                                    val stopCode = properties?.optString("stop_code") ?: ""
                                    val order = properties?.optString("order") ?: ""
                                    
                                    allStops.add(
                                        Stop(
                                            name = stopName,
                                            stopCode = stopCode,
                                            order = order,
                                            coordinate = LatLng(lat, lng)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    // Store loaded data (only the route LineString, not stops)
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
                    // Only add if we actually found a route
                    if (routeOnlyGeoJson != "{}") {
                        loadedGeoJsonData.add(index to routeOnlyGeoJson)
                        println("Successfully loaded GeoJSON for route: $path")
                    } else {
                        println("Warning: No route path found in $path")
                    }
                    
                } catch (e: Exception) {
                    println("Error loading route $path: ${e.message}")
                    e.printStackTrace()
                }
            }
            
            println("Finished loading all routes. Total stops: ${allStops.size}")
            
            // Create stops map for quick lookup
            stopsMap = allStops.associateBy { "${it.coordinate.latitude},${it.coordinate.longitude}" }
            
            // Now add all sources and layers on main thread sequentially
            withContext(Dispatchers.Main) {
                // Get line-specific color
                val lineColor = getTransitLineColor(line)
                
                // Add route lines
                loadedGeoJsonData.forEach { (index, geoJsonString) ->
                    try {
                        val sourceId = "transit-line-source-$index"
                        val layerId = "transit-line-layer-$index"
                        
                        // Add source
                        val source = GeoJsonSource(sourceId, geoJsonString)
                        style.addSource(source)
                        
                        // Add line layer
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
                
                // Add stops as markers
                if (allStops.isNotEmpty()) {
                    try {
                        // Create pin marker bitmap (bigger size)
                        val pinBitmap = createPinBitmap(Color.parseColor(lineColor))
                        style.addImage("stop-pin", pinBitmap)
                        println("Added pin image to style")
                        
                        // Create GeoJSON features for stops with properties
                        val stopFeatures = allStops.map { stop ->
                            val properties = JsonObject()
                            properties.addProperty("name", stop.name)
                            properties.addProperty("stop_code", stop.stopCode)
                            properties.addProperty("order", stop.order)
                            
                            Feature.fromGeometry(
                                Point.fromLngLat(stop.coordinate.longitude, stop.coordinate.latitude),
                                properties
                            )
                        }
                        
                        val featureCollection = FeatureCollection.fromFeatures(stopFeatures)
                        
                        // Add stops source
                        val stopsSource = GeoJsonSource("transit-stops-source", featureCollection)
                        style.addSource(stopsSource)
                        
                        // 1. Add invisible hit area layer (for clicking)
                        val hitAreaLayer = CircleLayer("transit-stops-hit-area", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(15f), // Clickable radius
                                PropertyFactory.circleColor(Color.TRANSPARENT),
                                PropertyFactory.circleOpacity(0f)
                            )
                        style.addLayer(hitAreaLayer)
                        
                        // 2. Add visible dots layer (CircleLayer)
                        val stopsLayer = CircleLayer("transit-stops-dots-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(5f),
                                PropertyFactory.circleColor(Color.WHITE),
                                PropertyFactory.circleStrokeWidth(2f),
                                PropertyFactory.circleStrokeColor(lineColor)
                            )
                        style.addLayer(stopsLayer)

                        // 3. Add labels layer (SymbolLayer) - visible only when zoomed in
                        val labelsLayer = SymbolLayer("transit-stops-labels-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.textField("{name}"),
                                PropertyFactory.textSize(12f),
                                PropertyFactory.textOffset(arrayOf(0f, 1.5f)),
                                PropertyFactory.textAnchor("top"),
                                PropertyFactory.textColor(Color.parseColor(lineColor)), 
                                PropertyFactory.textHaloColor(Color.WHITE),
                                PropertyFactory.textHaloWidth(1f)
                            )
                        labelsLayer.minZoom = 14f // Only show labels when zoomed in
                        style.addLayer(labelsLayer)
                        
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
    
    return@withContext stopsMap
}

/**
 * Extract only the route path from the GeoJSON FeatureCollection
 */
private fun extractRoutePathOnly(geoJson: JSONObject): String {
    return try {
        val features = geoJson.optJSONArray("features")
        if (features != null) {
            for (i in 0 until features.length()) {
                val feature = features.getJSONObject(i)
                val properties = feature.optJSONObject("properties")
                val geometry = feature.optJSONObject("geometry")
                
                val featureType = properties?.optString("type")
                val geometryType = geometry?.optString("type")
                
                // Return if explicit route_path OR fallback to LineString/MultiLineString geometry
                if (featureType == "route_path" || 
                   (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))) {
                    // Return just this feature as a FeatureCollection
                    return """{"type":"FeatureCollection","features":[$feature]}"""
                }
            }
        }
        "{}" // Return empty if no route_path found
    } catch (e: Exception) {
        e.printStackTrace()
        "{}"
    }
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

private fun createPinBitmap(color: Int): Bitmap {
    val size = 48
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        isAntiAlias = true
    }
    
    // Draw pin shape (circle)
    paint.color = color
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
    
    // Draw white border
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3f
    paint.color = Color.WHITE
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
    
    return bitmap
}
