package com.example.newoasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
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
import org.maplibre.android.style.expressions.Expression
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.layers.SymbolLayer
import newoasa.composeapp.generated.resources.Res
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Stop(
    val name: String,
    val stopCode: String,
    val order: String,
    val coordinate: LatLng
)

data class StopInfoState(
    val stop: Stop,
    val lines: List<String> = emptyList()  // Lines that pass through this stop
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
                    // Load all metro lines
                    try {
                        val metroLines = TransitLineRepository.getMetroLines()
                        metroLines.forEach { metroLine ->
                            displayPersistentTransitLine(map, metroLine, context, "metro")
                        }
                    } catch (e: Exception) { println("Error loading metro lines: ${e.message}") }
                    
                    // Load all tram lines
                    try {
                        val tramLines = TransitLineRepository.getTramLines()
                        tramLines.forEach { tramLine ->
                            displayPersistentTransitLine(map, tramLine, context, "tram")
                        }
                    } catch (e: Exception) { println("Error loading tram lines: ${e.message}") }
                    
                    // Load all suburban lines
                    try {
                        val suburbanLines = TransitLineRepository.getSuburbanLines()
                        suburbanLines.forEach { suburbanLine ->
                            displayPersistentTransitLine(map, suburbanLine, context, "suburban")
                        }
                    } catch (e: Exception) { println("Error loading suburban lines: ${e.message}") }
                    
                    // Load combined stations AFTER lines so dots appear on top
                    displayCombinedStations(map, context)
                    
                    baseLinesLoaded = true
                    println("Base transit lines and stations loaded")
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
                                                        stopCode = "",
                                                        order = "",
                                                        coordinate = stopCoord
                                                    ),
                                                    lines = lines
                                                )
                                            }
                                        }
                                        
                                        handled = true
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
                stopInfo = info,
                onClose = { selectedStopInfo = null }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StopInfoWindow(
    stopInfo: StopInfoState,
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
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .background(
                    color = ComposeColor(0xFF1565C0), // Nice blue background
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
                .clickable(onClick = {}),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Station name at top
            Text(
                text = stopInfo.stop.name,
                style = MaterialTheme.typography.headlineSmall,
                color = ComposeColor.White,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Show line badges if available
            if (stopInfo.lines.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stopInfo.lines.forEach { lineId ->
                        LineBadge(lineId = lineId)
                    }
                }
            }
        }
    }
}

@Composable
fun LineBadge(lineId: String) {
    val displayText = when {
        lineId.toIntOrNull() != null -> "M$lineId"  // Metro lines
        lineId.startsWith("T", ignoreCase = true) -> lineId.uppercase()  // Tram lines
        lineId.startsWith("A", ignoreCase = true) -> lineId.uppercase()  // Suburban lines
        else -> lineId
    }
    
    val backgroundColor = getLineComposeColor(lineId)
    
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = ComposeColor.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Display combined metro and tram stations
 * Station appearance:
 * - Single-line stations: White circle with black border
 * - Connection stations (2+ lines): White circle + black center dot + black border
 * Uses data-driven styling based on line_count property
 */
@OptIn(ExperimentalResourceApi::class)
private suspend fun displayCombinedStations(
    map: MapLibreMap,
    context: android.content.Context
) = withContext(Dispatchers.Main) {
    map.getStyle { style ->
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            try {
                // Try to load combined stations file
                val combinedStationsJson = try {
                    val bytes = Res.readBytes("files/combined_metro_tram_stations.json")
                    bytes.decodeToString()
                } catch (e: Exception) {
                    println("Combined stations file not found. Run combine_metro_tram_stations.py to generate it.")
                    return@launch
                }
                
                if (combinedStationsJson.isBlank()) {
                    println("Combined stations file is empty")
                    return@launch
                }
                
                val geoJson = JSONObject(combinedStationsJson)
                val features = geoJson.optJSONArray("features")
                
                if (features == null || features.length() == 0) {
                    println("No features in combined stations file")
                    return@launch
                }
                
                withContext(Dispatchers.Main) {
                    try {
                        val sourceId = "combined-stations-source"
                        
                        // Remove existing station layers if present (to re-add on top)
                        listOf(
                            "combined-stations-labels",
                            "combined-stations-center",
                            "combined-stations-outer",
                            "combined-stations-hit-area"
                        ).forEach { layerId ->
                            try {
                                style.getLayer(layerId)?.let { style.removeLayer(it) }
                            } catch (e: Exception) {}
                        }
                        
                        // Remove source if exists
                        try {
                            style.getSource(sourceId)?.let { style.removeSource(it) }
                        } catch (e: Exception) {}
                        
                        // Create GeoJSON source
                        val source = GeoJsonSource(sourceId, combinedStationsJson)
                        style.addSource(source)
                        
                        // Add layers (they will be on top because added last)
                        // 1. Hit area layer (invisible, for clicking)
                        val hitAreaLayer = CircleLayer(
                            "combined-stations-hit-area",
                            sourceId
                        ).withProperties(
                            PropertyFactory.circleRadius(20f),
                            PropertyFactory.circleColor(Color.TRANSPARENT),
                            PropertyFactory.circleOpacity(0f)
                        )
                        style.addLayer(hitAreaLayer)
                        
                        // 2. Outer white circle with black stroke (for all stations)
                        val outerLayer = CircleLayer(
                            "combined-stations-outer",
                            sourceId
                        ).withProperties(
                            PropertyFactory.circleRadius(7f),  // Bigger radius
                            PropertyFactory.circleColor(Color.WHITE),
                            PropertyFactory.circleStrokeWidth(3f),
                            PropertyFactory.circleStrokeColor("#000000")
                        )
                        style.addLayer(outerLayer)
                        
                        // 3. Center black dot (ONLY for connection stations with 2+ lines)
                        // Use data-driven styling based on line_count property
                        val centerDotLayer = CircleLayer(
                            "combined-stations-center",
                            sourceId
                        ).withProperties(
                            PropertyFactory.circleRadius(
                                Expression.step(
                                    Expression.get("line_count"),
                                    Expression.literal(0f),  // 0 radius for single-line stations
                                    Expression.stop(2, 3f)   // 3f radius for 2+ lines (connections)
                                )
                            ),
                            PropertyFactory.circleColor("#000000")
                        )
                        style.addLayer(centerDotLayer)
                        
                        // 4. Labels layer (only visible when zoomed in)
                        val labelsLayer = SymbolLayer(
                            "combined-stations-labels",
                            sourceId
                        ).withProperties(
                            PropertyFactory.textField("{name}"),
                            PropertyFactory.textSize(12f),
                            PropertyFactory.textOffset(arrayOf(0f, 2.0f)),
                            PropertyFactory.textAnchor("top"),
                            PropertyFactory.textColor(Color.BLACK),
                            PropertyFactory.textHaloColor(Color.WHITE),
                            PropertyFactory.textHaloWidth(2f)
                        )
                        labelsLayer.minZoom = 14f
                        style.addLayer(labelsLayer)
                        
                        println("Added ${features.length()} combined stations (on top, with connection styling)")
                    } catch (e: Exception) {
                        println("Error adding combined stations to map: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                println("Error loading combined stations: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

/**
 * Display a persistent transit line (metro, tram, or suburban) that stays on the map
 * Does NOT display individual stops - those are handled by combined stations
 * Applies different styling based on line type:
 * - Metro: 4px solid lines
 * - Tram: 2.5px solid lines (thinner)
 * - Suburban: 4px dashed lines (alternating line color and white)
 */
@OptIn(ExperimentalResourceApi::class)
private suspend fun displayPersistentTransitLine(
    map: MapLibreMap,
    line: TransitLine,
    context: android.content.Context,
    prefix: String
) = withContext(Dispatchers.Main) {
    map.getStyle { style ->
        val loadedGeoJsonData = mutableListOf<Pair<String, String>>()
        
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            line.routePaths.forEachIndexed { index, path ->
                try {
                    val geoJsonString = loadGeoJsonFromResources(path)
                    
                    if (geoJsonString.isBlank() || geoJsonString == "{}") {
                        return@forEachIndexed
                    }
                    
                    val geoJson = JSONObject(geoJsonString)
                    
                    // Extract only the ROUTE geometry (ignore stops)
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
                    
                    if (routeOnlyGeoJson != "{}" && routeOnlyGeoJson != """{"type":"FeatureCollection","features":[]}""") {
                        val layerId = "${prefix}-${line.lineNumber}-$index"
                        loadedGeoJsonData.add(layerId to routeOnlyGeoJson)
                    }
                } catch (e: Exception) {
                    println("Error loading persistent route $path: ${e.message}")
                }
            }
            
            withContext(Dispatchers.Main) {
                val lineColor = getTransitLineColor(line)
                
                // Determine line width and pattern based on type
                val lineWidth = when {
                    line.isTram -> 2.5f      // Thinner for trams
                    line.isSuburban -> 4f    // Standard width for suburban
                    else -> 4f               // Standard width for metro
                }
                
                // Add route lines with appropriate styling
                loadedGeoJsonData.forEach { (layerId, geoJsonString) ->
                    try {
                        val sourceId = "source-$layerId"
                        
                        if (style.getSource(sourceId) == null) {
                            val source = GeoJsonSource(sourceId, geoJsonString)
                            style.addSource(source)
                            
                            // For suburban lines: create dashed pattern with line color and white
                            if (line.isSuburban) {
                                // Add white background layer first (below)
                                val whiteLayerId = "${layerId}-white"
                                val whiteLayer = LineLayer(whiteLayerId, sourceId).withProperties(
                                    PropertyFactory.lineColor("#FFFFFF"),
                                    PropertyFactory.lineWidth(lineWidth),
                                    PropertyFactory.lineOpacity(0.9f),
                                    PropertyFactory.lineDasharray(arrayOf(1f, 2f))  // Inverse pattern
                                )
                                style.addLayer(whiteLayer)
                                
                                // Add colored layer on top
                                val lineLayer = LineLayer(layerId, sourceId).withProperties(
                                    PropertyFactory.lineColor(lineColor),
                                    PropertyFactory.lineWidth(lineWidth),
                                    PropertyFactory.lineOpacity(0.9f),
                                    PropertyFactory.lineDasharray(arrayOf(2f, 1f))  // Dash pattern
                                )
                                style.addLayer(lineLayer)
                            } else {
                                // Solid lines for metro and tram
                                val lineLayer = LineLayer(layerId, sourceId).withProperties(
                                    PropertyFactory.lineColor(lineColor),
                                    PropertyFactory.lineWidth(lineWidth),
                                    PropertyFactory.lineOpacity(0.8f)
                                )
                                style.addLayer(lineLayer)
                            }
                        }
                    } catch (e: Exception) {
                        println("Error adding persistent layer: ${e.message}")
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
                    val features = geoJson.optJSONArray("features")
                    if (features != null) {
                        for (i in 0 until features.length()) {
                            val feature = features.getJSONObject(i)
                            val properties = feature.optJSONObject("properties")
                            val geometry = feature.optJSONObject("geometry")
                            
                            val featureType = properties?.optString("type")
                            val geometryType = geometry?.optString("type")
                            
                            val isRoute = featureType == "route_path" || 
                                         featureType == "Line" ||
                                         (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))
                            
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
                                            coordinate = LatLng(coordinates.getDouble(1), coordinates.getDouble(0))
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
            
            stopsMap = allStops.associateBy { "${it.coordinate.latitude},${it.coordinate.longitude}" }
            
            withContext(Dispatchers.Main) {
                val lineColor = getTransitLineColor(line)
                
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
                                PropertyFactory.circleColor(Color.WHITE),
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
                                PropertyFactory.textColor(Color.parseColor(lineColor)), 
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
