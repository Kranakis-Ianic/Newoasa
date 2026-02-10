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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.newoasa.data.Stop
import com.example.newoasa.data.StopInfoState
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
                                                .zoom(if (currentZoom < 14) 14.0 else currentZoom)
                                                .build()
                                            
                                            map.animateCamera(
                                                CameraUpdateFactory.newCameraPosition(newCameraPosition),
                                                300
                                            )
                                            
                                            // Show info window after animation completes
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
        val loadedGeoJsonData = mutableListOf<Pair<Int, String>>()
        
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
                                         (featureType.isNullOrEmpty() && (geometryType == "LineString" || geometryType == "MultiLineString"))
                            
                            // Check for stop
                            val isStop = featureType == "stop" || 
                                        (featureType.isNullOrEmpty() && (geometryType == "Point" || geometryType == "MultiPoint"))

                            if (isRoute) {
                                // Extract route coordinates for bounds calculation
                                val coords = geometry?.optJSONArray("coordinates")
                                coords?.let {
                                    if (geometryType == "LineString") {
                                        for (j in 0 until it.length()) {
                                            val coord = it.getJSONArray(j)
                                            val lng = coord.getDouble(0)
                                            val lat = coord.getDouble(1)
                                            allCoordinates.add(LatLng(lat, lng))
                                        }
                                    }
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
                                            latitude = lat,
                                            longitude = lng
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    // Store loaded data (only the route LineString, not stops)
                    val routeOnlyGeoJson = extractRoutePathOnly(geoJson)
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
            stopsMap = allStops.associateBy { "${it.latitude},${it.longitude}" }
            
            // Now add all sources and layers on main thread sequentially
            withContext(Dispatchers.Main) {
                // Use LineColors from commonMain to get consistent color
                val lineColor = LineColors.getHexColorForCategory(line.category, line.isBus)
                
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
                        
                        // Add stops source
                        val stopsSource = GeoJsonSource("transit-stops-source", featureCollection)
                        style.addSource(stopsSource)
                        
                        // 1. Add invisible hit area layer (for clicking)
                        val hitAreaLayer = CircleLayer("transit-stops-hit-area", "transit-stops-source")
                            .withProperties(
                                PropertyFactory.circleRadius(15f),
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
                                PropertyFactory.textColor(if (line.category == "metro") Color.parseColor(lineColor) else Color.BLACK), 
                                PropertyFactory.textHaloColor(Color.WHITE),
                                PropertyFactory.textHaloWidth(1f)
                            )
                        labelsLayer.minZoom = 14f
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
                        
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 100),
                            1000
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
                
                if (featureType == "route_path" || 
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
