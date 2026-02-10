# Station Cards Integration Guide

This guide explains how to integrate the station card feature into the map view.

## Overview

The station system consists of:
1. **TransitStation** - Data model for stations with multiple line support
2. **StationParser** - Utilities to parse stations from GeoJSON and cluster nearby stations
3. **StationCard** - UI component to display station information

## Quick Start

### 1. Generate Combined GeoJSON Files

First, run the Python script to generate combined line and station files:

```bash
python scripts/combine_transit_lines.py
```

This creates:
- `combined_metro_lines.geojson`
- `combined_tram_lines.geojson`
- `combined_suburban_lines.geojson`
- `all_transit_stations.geojson`

### 2. Load Stations in Your Map

Add station loading to your MapViewModel or MapView:

```kotlin
import com.example.newoasa.model.TransitStation
import com.example.newoasa.utils.StationParser
import newoasa.composeapp.generated.resources.Res

class MapViewModel {
    var allStations by mutableStateOf<List<TransitStation>>(emptyList())
        private set
    
    var selectedStation by mutableStateOf<TransitStation?>(null)
        private set
    
    suspend fun loadStations() {
        try {
            // Load the all_transit_stations.geojson file
            val stationsJson = Res.readBytes(
                "files/geojson/all_transit_stations.geojson"
            ).decodeToString()
            
            // Parse stations - they already have line information
            val stations = parseStationsFromCombinedFile(stationsJson)
            
            // Optionally cluster nearby stations (if not already done)
            allStations = StationParser.clusterStations(stations)
            
        } catch (e: Exception) {
            println("Error loading stations: ${e.message}")
        }
    }
    
    private fun parseStationsFromCombinedFile(json: String): List<TransitStation> {
        val stations = mutableListOf<TransitStation>()
        try {
            val jsonObj = Json.parseToJsonElement(json).jsonObject
            val features = jsonObj["features"]?.jsonArray ?: return emptyList()
            
            features.forEach { feature ->
                val props = feature.jsonObject["properties"]?.jsonObject
                val geom = feature.jsonObject["geometry"]?.jsonObject
                
                val name = props?.get("name")?.jsonPrimitive?.content ?: return@forEach
                val nameEn = props["int_name"]?.jsonPrimitive?.content
                val id = props["@id"]?.jsonPrimitive?.content ?: return@forEach
                
                val coords = geom?.get("coordinates")?.jsonArray ?: return@forEach
                val lon = coords[0].jsonPrimitive.double
                val lat = coords[1].jsonPrimitive.double
                
                // Parse lines array
                val lines = mutableSetOf<TransitStation.StationLine>()
                props["lines"]?.jsonArray?.forEach { lineObj ->
                    val lineProps = lineObj.jsonObject
                    val lineNum = lineProps["lineNumber"]?.jsonPrimitive?.content ?: return@forEach
                    val category = lineProps["category"]?.jsonPrimitive?.content ?: return@forEach
                    lines.add(TransitStation.StationLine(lineNum, category))
                }
                
                stations.add(TransitStation(id, name, nameEn, lat, lon, lines))
            }
        } catch (e: Exception) {
            println("Error parsing stations: ${e.message}")
        }
        return stations
    }
    
    fun selectStation(station: TransitStation) {
        selectedStation = station
    }
    
    fun clearSelection() {
        selectedStation = null
    }
}
```

### 3. Add Station Markers to Map

In your MapView composable:

```kotlin
import com.example.newoasa.ui.components.StationCard

@Composable
fun MapView(viewModel: MapViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadStations()
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        MapLibreMap(
            // ... your map configuration
        ) {
            // Add station markers
            viewModel.allStations.forEach { station ->
                CircleLayer(
                    id = "station-${station.id}",
                    source = PointSource(
                        id = "station-source-${station.id}",
                        latitude = station.latitude,
                        longitude = station.longitude
                    )
                ) {
                    circleRadius = 6.0
                    circleColor = Color.White
                    circleStrokeColor = Color.Black
                    circleStrokeWidth = 2.0
                    
                    onClick = {
                        viewModel.selectStation(station)
                        true // Consume the click event
                    }
                }
            }
        }
        
        // Show station card when station is selected
        viewModel.selectedStation?.let { station ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            ) {
                StationCard(
                    station = station,
                    onDismiss = { viewModel.clearSelection() }
                )
            }
        }
    }
}
```

### 4. Always Show Metro/Tram/Suburban Lines

To make these lines always visible:

```kotlin
@Composable
fun MapView(viewModel: MapViewModel) {
    MapLibreMap(
        // ... config
    ) {
        // Metro lines (always visible)
        LineLayer(
            id = "metro-lines",
            source = GeoJsonSource(
                id = "metro-source",
                data = Res.readBytes("files/geojson/combined_metro_lines.geojson")
                    .decodeToString()
            )
        ) {
            lineColor = Color(0xFF007A33)
            lineWidth = 3.0
        }
        
        // Tram lines (always visible)
        LineLayer(
            id = "tram-lines",
            source = GeoJsonSource(
                id = "tram-source",
                data = Res.readBytes("files/geojson/combined_tram_lines.geojson")
                    .decodeToString()
            )
        ) {
            lineColor = Color(0xFFFFD100)
            lineWidth = 3.0
        }
        
        // Suburban lines (always visible)
        LineLayer(
            id = "suburban-lines",
            source = GeoJsonSource(
                id = "suburban-source",
                data = Res.readBytes("files/geojson/combined_suburban_lines.geojson")
                    .decodeToString()
            )
        ) {
            lineColor = Color(0xFF0066CC)
            lineWidth = 3.0
        }
        
        // Then add stations on top
        // ...
    }
}
```

## Station Clustering

Stations at the same physical location (transfer stations) are automatically clustered:

```kotlin
// Manual clustering if needed
val clustered = StationParser.clusterStations(
    stations = allStations,
    thresholdDegrees = 0.0001 // ~11 meters
)
```

## Station Data Model

```kotlin
data class TransitStation(
    val id: String,              // OpenStreetMap node ID
    val name: String,            // Greek name
    val nameEn: String?,         // English/transliterated name
    val latitude: Double,
    val longitude: Double,
    val lines: MutableSet<StationLine>
)

data class StationLine(
    val lineNumber: String,      // e.g., "1", "2", "3"
    val category: String         // "metro", "tram", "suburban"
)
```

## Styling Recommendations

### Station Markers by Category

```kotlin
val markerColor = when {
    station.lines.any { it.category == "metro" } -> Color(0xFF007A33)
    station.lines.any { it.category == "tram" } -> Color(0xFFFFD100)
    station.lines.any { it.category == "suburban" } -> Color(0xFF0066CC)
    else -> Color.White
}
```

### Multi-line Stations

For stations serving multiple lines, use a multi-color ring or show the primary line color.

## Performance Considerations

1. **Load once**: Load stations once on app start
2. **Clustering**: Pre-cluster stations to reduce marker count
3. **Viewport filtering**: Only render stations in the visible map area
4. **Zoom levels**: Show/hide stations based on zoom level

```kotlin
CircleLayer(...) {
    // Only show at certain zoom levels
    minZoom = 11.0
    maxZoom = 22.0
}
```

## Troubleshooting

### Stations Not Appearing

1. Check that `all_transit_stations.geojson` exists
2. Verify the file path in `Res.readBytes()`
3. Check console for parsing errors
4. Ensure zoom level is appropriate (> 11)

### Station Names Not Showing

Ensure Greek text encoding is properly handled:

```kotlin
val content = byteArray.decodeToString() // UTF-8 by default
```

### Click Not Working

Make sure onClick returns `true` to consume the event:

```kotlin
onClick = {
    viewModel.selectStation(station)
    true // Important!
}
```
