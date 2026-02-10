package com.example.newoasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.Stop
import com.example.newoasa.data.StopInfoState
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
import org.maplibre.android.style.expressions.Expression
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.layers.SymbolLayer
import newoasa.composeapp.generated.resources.Res

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
        line.isSuburban -> when (line.lineNumber.uppercase()) {
            "A1" -> "#ffcd00" // Yellow
            "A2" -> "#753bbd" // Purple
            "A3" -> "#78be20" // Light green
            "A4" -> "#00a3e0" // Light blue
            else -> "#000000" // Default to black
        }
        line.isBus -> "#009cc7" // Cyan
        line.isTrolley -> "#f07c00" // Orange
        else -> "#000000" // Default to black
    }
}

/**
 * Get Compose Color for a line ID string (for UI display)
 */
private fun getLineComposeColor(lineId: String): ComposeColor {
    return when (lineId.uppercase()) {
        "1" -> ComposeColor(0xFF00734c) // Green - Metro 1
        "2" -> ComposeColor(0xFFe60000) // Red - Metro 2
        "3" -> ComposeColor(0xFF002673) // Blue - Metro 3
        "T6" -> ComposeColor(0xFFa7c636) // Lime green - Tram T6
        "T7" -> ComposeColor(0xFFb9348b) // Pink - Tram T7
        "A1" -> ComposeColor(0xFFffcd00) // Yellow - Suburban A1
        "A2" -> ComposeColor(0xFF753bbd) // Purple - Suburban A2
        "A3" -> ComposeColor(0xFF78be20) // Light green - Suburban A3
        "A4" -> ComposeColor(0xFF00a3e0) // Light blue - Suburban A4
        else -> ComposeColor(0xFF000000) // Default to black
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

    // Load base transit lines (metro, tram, suburban) after map is ready
    LaunchedEffect(isMapReady) {
        if (isMapReady && !baseLinesLoaded) {
            mapView.getMapAsync { map ->
                coroutineScope.launch {
                    println("Starting to load base transit lines...")
                    
                    // Load all metro lines
                    try {
                        val metroLines = TransitLineRepository.getMetroLines()
                        println("Loading ${metroLines.size} metro lines")
                        metroLines.forEach { metroLine ->
                            displayPersistentTransitLine(map, metroLine, context, "metro")
                        }
                    } catch (e: Exception) { 
                        println("Error loading metro lines: ${e.message}")
                        e.printStackTrace()
                    }
                    
                    // Load all tram lines
                    try {
                        val tramLines = TransitLineRepository.getTramLines()
                        println("Loading ${tramLines.size} tram lines")
                        tramLines.forEach { tramLine ->
                            displayPersistentTransitLine(map, tramLine, context, "tram")
                        }
                    } catch (e: Exception) { 
                        println("Error loading tram lines: ${e.message}")
                        e.printStackTrace()
                    }
                    
                    // Load all suburban lines
                    try {
                        val suburbanLines = TransitLineRepository.getSuburbanLines()
                        println("Loading ${suburbanLines.size} suburban lines")
                        suburbanLines.forEach { suburbanLine ->
                            displayPersistentTransitLine(map, suburbanLine, context, "suburban")
                        }
                    } catch (e: Exception) { 
                        println("Error loading suburban lines: ${e.message}")
                        e.printStackTrace()
                    }
                    
                    // Load combined stations AFTER lines so dots appear on top
                    displayCombinedStations(map, context)
                    
                    baseLinesLoaded = true
                    println("✓ Base transit lines and stations loaded")
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
                                
                                // Check combined stations first
                                val combinedFeatures = map.queryRenderedFeatures(screenPoint, "combined-stations-hit-area")
                                
                                var handled = false
                                if (combinedFeatures.isNotEmpty()) {
                                    val feature = combinedFeatures[0]
                                    val properties = feature.properties()
                                    
                                    if (properties != null) {
                                        val stopName = properties.get("name")?.asString ?: "Unknown"
                                        
                                        // Extract lines from properties
                                        val linesJson = properties.get("lines")
                                        val lines = if (linesJson != null && linesJson.isJsonArray) {
                                            linesJson.asJsonArray.map { it.asString }
                                        } else {
                                            emptyList()
                                        }
                                        
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
                                                        latitude = stopCoord.latitude,
                                                        longitude = stopCoord.longitude
                                                    )
                                                )
                                            }
                                        }
                                        
                                        return@addOnMapClickListener true
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
        
        // Info card overlay - using shared StopInfoCard component
        selectedStopInfo?.let { info ->
            StopInfoCard(
                stop = info.stop,
                onClose = { selectedStopInfo = null }
            )
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
        // Remove ONLY temporary transit line layers
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
            style.getLayer("transit-stops-hit-area")?.let { 
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
        val loadedGeoJsonData = mutableListOf<Pair<Int, String>>()
        
        // Load all GeoJSON files sequentially in IO dispatcher
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            line.routePaths.forEachIndexed { index, path ->
                try {
                    val geoJsonString = loadGeoJsonFromResources(path)
                    
                    if (geoJsonString.isBlank() || geoJsonString == "{}") {
                        return@forEachIndexed
                    }
                    
                    val geoJson = JSONObject(geoJsonString)
                    
                    // Validate that it has features
                    val features = geoJson.optJSONArray("features")
                    if (features != null) {
                        for (i in 0 until features.length()) {
                            val feature = features.getJSONObject(i)
                            val properties = feature.optJSONObject("properties")
                            val geometry = feature.optJSONObject("geometry")
                            
                            val featureType = properties?.optString("type")
                            val geometryType = geometry?.optString("type")
                            
                            // Check for route path
                            val isRoute = featureType == "route_path" || 
                                         featureType == "Line" ||
                                         (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))
                            
                            // Check for stop
                            val isStop = featureType == "stop" || 
                                        featureType == "Station" ||
                                        (featureType.isNullOrEmpty() && (geometryType == "Point" || geometryType == "MultiPoint"))

                            if (isRoute) {
                                val coords = geometry?.optJSONArray("coordinates")
                                coords?.let {
                                    if (geometryType == "LineString") {
                                        for (j in 0 until it.length()) {
                                            val coord = it.getJSONArray(j)
                                            allCoordinates.add(LatLng(coord.getDouble(1), coord.getDouble(0)))
                                        }
                                    }
                                    else if (geometryType == "MultiLineString") {
                                         for (k in 0 until it.length()) {
                                            val lineString = it.getJSONArray(k)
                                            for (j in 0 until lineString.length()) {
                                                val coord = lineString.getJSONArray(j)
                                                allCoordinates.add(LatLng(coord.getDouble(1), coord.getDouble(0)))
                                            }
                                         }
                                    }
                                }
                            } else if (isStop) {
                                val coordinates = geometry?.optJSONArray("coordinates")
                                if (coordinates != null && coordinates.length() >= 2) {
                                    val stopName = properties?.optString("name") ?: "Unknown"
                                    val stopCode = properties?.optString("stop_code") ?: ""
                                    val order = properties?.optString("order") ?: ""
                                    
                                    allStops.add(
                                        Stop(
                                            name = stopName,
                                            stopCode = stopCode,
                                            order = order,
                                            latitude = lat,
                                            longitude = lng
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
                    if (routeOnlyGeoJson != "{}") {
                        loadedGeoJsonData.add(index to routeOnlyGeoJson)
                    }
                    
                } catch (e: Exception) {
                    println("Error loading route $path: ${e.message}")
                }
            }
            
            println("Finished loading all routes. Total stops: ${allStops.size}")
            
            // Create stops map for quick lookup
            stopsMap = allStops.associateBy { "${it.latitude},${it.longitude}" }
            
            withContext(Dispatchers.Main) {
                // Use LineColors from commonMain to get consistent color
                val lineColor = LineColors.getHexColorForCategory(line.category, line.isBus)
                
                // Add route lines below stations
                loadedGeoJsonData.forEach { (index, geoJsonString) ->
                    try {
                        val sourceId = "transit-line-source-$index"
                        val layerId = "transit-line-layer-$index"
                        
                        val source = GeoJsonSource(sourceId, geoJsonString)
                        style.addSource(source)
                        
                        val lineLayer = LineLayer(layerId, sourceId).withProperties(
                            PropertyFactory.lineColor(lineColor),
                            PropertyFactory.lineWidth(5f),
                            PropertyFactory.lineOpacity(0.8f)
                        )
                        
                        // Try to add below station dots if they exist
                        try {
                            if (style.getLayer("combined-stations-hit-area") != null) {
                                style.addLayerBelow(lineLayer, "combined-stations-hit-area")
                            } else {
                                style.addLayer(lineLayer)
                            }
                        } catch (e: Exception) {
                            style.addLayer(lineLayer)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                // Add stops as markers (for buses and trolleys)
                if (allStops.isNotEmpty()) {
                    try {
                        // Create pin marker bitmap
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
                                Point.fromLngLat(stop.longitude, stop.latitude),
                                properties
                            )
                        }
                        
                        val featureCollection = FeatureCollection.fromFeatures(stopFeatures)
                        val stopsSource = GeoJsonSource("transit-stops-source", featureCollection)
                        style.addSource(stopsSource)
                        
                        val hitAreaLayer = CircleLayer("transit-stops-hit-area", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(15f),
                                PropertyFactory.circleColor(Color.TRANSPARENT),
                                PropertyFactory.circleOpacity(0f)
                            )
                        style.addLayer(hitAreaLayer)
                        
                        val stopsLayer = CircleLayer("transit-stops-dots-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(5f),
                                PropertyFactory.circleColor("#FFFFFF"),  // Explicit white hex
                                PropertyFactory.circleStrokeWidth(2f),
                                PropertyFactory.circleStrokeColor(lineColor)
                            )
                        style.addLayer(stopsLayer)

                        val labelsLayer = SymbolLayer("transit-stops-labels-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.textField("{name}"),
                                PropertyFactory.textSize(12f),
                                PropertyFactory.textOffset(arrayOf(0f, 1.5f)),
                                PropertyFactory.textAnchor("top"),
                                PropertyFactory.textColor(if (line.category == "metro") Color.parseColor(lineColor) else Color.BLACK), 
                                PropertyFactory.textHaloColor(Color.WHITE),
                                PropertyFactory.textHaloWidth(1f)
                            )
                        labelsLayer.minZoom = 14f
                        style.addLayer(labelsLayer)
                        
                    } catch (e: Exception) {
                        println("Error adding stops: ${e.message}")
                    }
                }
                
                // Animate camera to show all routes
                if (allCoordinates.isNotEmpty()) {
                    try {
                        val boundsBuilder = LatLngBounds.Builder()
                        allCoordinates.forEach { boundsBuilder.include(it) }
                        val bounds = boundsBuilder.build()
                        
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 100),
                            1000
                        )
                    } catch (e: Exception) {
                        println("Error animating camera: ${e.message}")
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
                
                if (featureType == "route_path" || 
                    featureType == "Line" ||
                   (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))) {
                    return """{"type":"FeatureCollection","features":[$feature]}"""
                }
            }
        }
        "{}"
    } catch (e: Exception) {
        e.printStackTrace()
        "{}"
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun loadGeoJsonFromResources(path: String): String {
    return try {
        val bytes = Res.readBytes(path)
        bytes.decodeToString()
    } catch (e: Exception) {
        println("Failed to load GeoJSON from composeResources path: $path")
        e.printStackTrace()
        "{}"
    }
}

/**
 * Create a pin bitmap for stop markers
 */
private fun createPinBitmap(color: Int): Bitmap {
    val size = 64
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    paint.color = color
    paint.style = Paint.Style.FILL
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
    
    paint.color = Color.WHITE
    canvas.drawCircle(size / 2f, size / 2f, size / 5f, paint)
    
    return bitmap
}
