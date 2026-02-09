package com.example.newoasa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.TransitLine
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

data class Stop(
    val name: String,
    val stopCode: String,
    val order: String,
    val coordinate: LatLng
)

data class StopInfoState(
    val stop: Stop
)

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
                                
                                // Check the hit area layer
                                val hitAreaFeatures = map.queryRenderedFeatures(
                                    screenPoint, 
                                    "transit-stops-hit-area"
                                )
                                
                                if (hitAreaFeatures.isNotEmpty()) {
                                    val feature = hitAreaFeatures[0]
                                    val properties = feature.properties()
                                    
                                    if (properties != null) {
                                        val stopName = properties.get("name")?.asString ?: "Unknown"
                                        val stopCode = properties.get("stop_code")?.asString ?: ""
                                        val order = properties.get("order")?.asString ?: ""
                                        
                                        // Get the geometry to find screen position
                                        val geometry = feature.geometry()
                                        if (geometry is Point) {
                                            val stopCoord = LatLng(geometry.latitude(), geometry.longitude())
                                            
                                            // Center map on the clicked pin
                                            val currentZoom = map.cameraPosition.zoom
                                            val newCameraPosition = CameraPosition.Builder()
                                                .target(stopCoord)
                                                .zoom(currentZoom)
                                                .build()
                                            
                                            map.animateCamera(
                                                CameraUpdateFactory.newCameraPosition(newCameraPosition),
                                                300 // Quick 300ms animation
                                            )
                                            
                                            // Show info window after animation completes
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
                                        
                                        return@addOnMapClickListener true // Consume the event
                                    }
                                } else {
                                    // Clicked outside of stops, close info window
                                    selectedStopInfo = null
                                }
                                
                                false
                            }
                        }
                        
                        // Set initial position (Athens, Greece)
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(37.9838, 23.7275))
                            .zoom(12.0)
                            .build()
                            
                        // Disable attribution and logo, Enable Compass, Disable Tilt
                        map.uiSettings.isAttributionEnabled = false
                        map.uiSettings.isLogoEnabled = false
                        map.uiSettings.isCompassEnabled = true
                        map.uiSettings.compassGravity = Gravity.TOP or Gravity.START // Move compass to top left
                        val density = context.resources.displayMetrics.density
                        val marginPx = (16 * density).toInt()
                        map.uiSettings.setCompassMargins(marginPx, marginPx, 0, 0)
                        map.uiSettings.isTiltGesturesEnabled = false
                    }
                }
            }
        )
        
        // Zoom Controls - Connected, Bottom Right
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 16.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Zoom In
            IconButton(
                onClick = { mapLibreInstance?.animateCamera(CameraUpdateFactory.zoomIn()) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Zoom In", 
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Divider
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .width(20.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
            
            // Zoom Out
            IconButton(
                onClick = { mapLibreInstance?.animateCamera(CameraUpdateFactory.zoomOut()) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Remove, 
                    contentDescription = "Zoom Out", 
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        // Info window overlay - centered on screen, above center point
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
    // Position the window centered horizontally and in the upper portion of screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClose), // Close when clicking outside
        contentAlignment = Alignment.Center // Center the content
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 200.dp) // Offset upward so it appears above the centered pin
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .clickable(onClick = {}), // Prevent closing when clicking inside
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with close button
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
            
            // Stop details
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

@OptIn(ExperimentalResourceApi::class)
private suspend fun displayTransitLine(
    map: MapLibreMap,
    line: TransitLine,
    context: android.content.Context
): Map<String, Stop> = withContext(Dispatchers.Main) {
    var stopsMap = emptyMap<String, Stop>()
    
    map.getStyle { style ->
        // Remove ALL previous transit line layers and sources
        for (i in 0 until 50) {
            try {
                style.getLayer("transit-line-layer-$i")?.let { 
                    style.removeLayer(it)
                }
            } catch (e: Exception) {
                // Ignore if layer doesn't exist
            }
            
            try {
                style.getSource("transit-line-source-$i")?.let { 
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
            style.getLayer("transit-stops-layer")?.let { 
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
        
        // Remove the stop pin image if it exists
        try {
            style.removeImage("stop-pin")
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
                    
                    // Validate that it has required properties
                    if (!geoJson.has("type")) {
                        println("Invalid GeoJSON - missing 'type' property for path: $path")
                        return@forEachIndexed
                    }
                    
                    // Extract route coordinates and stops from the FeatureCollection
                    val features = geoJson.optJSONArray("features")
                    if (features != null) {
                        for (i in 0 until features.length()) {
                            val feature = features.getJSONObject(i)
                            val properties = feature.optJSONObject("properties")
                            val featureType = properties?.optString("type")
                            
                            when (featureType) {
                                "route_path" -> {
                                    // Extract route coordinates for bounds calculation
                                    val geometry = feature.optJSONObject("geometry")
                                    val coords = geometry?.optJSONArray("coordinates")
                                    coords?.let {
                                        for (j in 0 until it.length()) {
                                            val coord = it.getJSONArray(j)
                                            val lng = coord.getDouble(0)
                                            val lat = coord.getDouble(1)
                                            allCoordinates.add(LatLng(lat, lng))
                                        }
                                    }
                                }
                                "stop" -> {
                                    // Extract stop information
                                    val geometry = feature.optJSONObject("geometry")
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
                    }
                    
                    // Store loaded data (only the route LineString, not stops)
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
                    loadedGeoJsonData.add(index to routeOnlyGeoJson)
                    println("Successfully loaded GeoJSON for route: $path")
                    
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
                // Add line color based on type
                val lineColor = if (line.isBus) "#2196F3" else "#9C27B0" // Blue for buses, Purple for trolleys
                
                // Add route lines
                loadedGeoJsonData.forEach { (index, geoJsonString) ->
                    try {
                        val sourceId = "transit-line-source-$index"
                        val layerId = "transit-line-layer-$index"
                        
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
                        
                    } catch (e: Exception) {
                        println("Error adding source/layer for index $index: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                // Add stops as markers
                if (allStops.isNotEmpty()) {
                    try {
                        // Create pin marker bitmap (bigger size)
                        val pinBitmap = createPinBitmap(
                            if (line.isBus) Color.parseColor("#2196F3") else Color.parseColor("#9C27B0")
                        )
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
                        println("Added stops source with ${allStops.size} stops")
                        
                        // Add invisible hit area layer FIRST (larger clickable area)
                        val hitAreaLayer = CircleLayer("transit-stops-hit-area", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(80f), // Very large hit area (80 pixels radius)
                                PropertyFactory.circleColor(Color.TRANSPARENT), // Invisible
                                PropertyFactory.circleOpacity(0f) // Completely transparent
                            )
                        style.addLayer(hitAreaLayer)
                        println("Added hit area layer with 80px radius")
                        
                        // Add visible stops layer SECOND (on top of hit area)
                        val stopsLayer = SymbolLayer("transit-stops-layer", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.iconImage("stop-pin"),
                                PropertyFactory.iconSize(0.8f),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true),
                                PropertyFactory.iconAnchor("bottom")
                            )
                        style.addLayer(stopsLayer)
                        println("Added stops layer with ${allStops.size} stops")
                        
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

                        // Set reasonable zoom limits
                        // Lower min zoom to ensure large routes fit completely
                        map.setMinZoomPreference(1.0)
                        map.setMaxZoomPreference(18.0)
                        
                        // Calculate padding in pixels based on screen density
                        val density = context.resources.displayMetrics.density
                        val horizontalPadding = (50 * density).toInt() // Increased from 24dp to 50dp
                        val verticalPadding = (150 * density).toInt()  // Increased from 32dp to 150dp

                        // Move camera immediately to bounds with asymmetrical padding
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds, 
                                horizontalPadding, 
                                verticalPadding, 
                                horizontalPadding, 
                                verticalPadding
                            )
                        )
                        println("Camera moved to show all routes")
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
                val featureType = properties?.optString("type")
                
                if (featureType == "route_path") {
                    // Return just this feature as a FeatureCollection
                    return """{"type":"FeatureCollection","features":[${feature}]}"""
                }
            }
        }
        "{}" // Return empty if no route_path found
    } catch (e: Exception) {
        e.printStackTrace()
        "{}"
    }
}

/**
 * Create a custom pin marker bitmap
 */
private fun createPinBitmap(color: Int): Bitmap {
    val size = 120
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
