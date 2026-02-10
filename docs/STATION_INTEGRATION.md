# Station Cards Integration Guide

This guide explains how to integrate the station card feature into the map view.

## Overview

The station system consists of:
1. **TransitStation** - Data model for stations with multiple line support
2. **StationParser** - Utilities to parse stations from GeoJSON and cluster nearby stations
3. **StationCard** - UI component to display station information with official line colors
4. **LineColors** - Official Athens public transport line colors

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
import com.example.newoasa.theme.LineColors
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
import com.example.newoasa.theme.LineColors

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
                // Determine marker color based on station's primary line
                val markerColor = getStationMarkerColor(station)
                
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
                    circleStrokeColor = markerColor
                    circleStrokeWidth = 2.5
                    
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

// Helper function to get station marker color
fun getStationMarkerColor(station: TransitStation): Color {
    // Use the color of the first line, or category-based color
    val firstLine = station.lines.firstOrNull() ?: return LineColors.Bus
    
    val lineCode = when (firstLine.category.lowercase()) {
        "metro" -> "M${firstLine.lineNumber}"
        "tram" -> "T${firstLine.lineNumber}"
        "suburban" -> firstLine.lineNumber
        else -> firstLine.lineNumber
    }
    
    return LineColors.getColorForLine(lineCode)
}
```

### 4. Always Show Metro/Tram/Suburban Lines

To make these lines always visible with official colors:

```kotlin
import com.example.newoasa.theme.LineColors

@Composable
fun MapView(viewModel: MapViewModel) {
    MapLibreMap(
        // ... config
    ) {
        // Metro Line 1 (Green)
        LineLayer(
            id = "metro-1",
            source = GeoJsonSource(
                id = "metro-1-source",
                data = Res.readBytes("files/geojson/Metro lines/1/...").decodeToString()
            )
        ) {
            lineColor = LineColors.Metro1
            lineWidth = 3.0
        }
        
        // Metro Line 2 (Red)
        LineLayer(
            id = "metro-2",
            source = GeoJsonSource(
                id = "metro-2-source",
                data = Res.readBytes("files/geojson/Metro lines/2/...").decodeToString()
            )
        ) {
            lineColor = LineColors.Metro2
            lineWidth = 3.0
        }
        
        // Metro Line 3 (Blue)
        LineLayer(
            id = "metro-3",
            source = GeoJsonSource(
                id = "metro-3-source",
                data = Res.readBytes("files/geojson/Metro lines/3/...").decodeToString()
            )
        ) {
            lineColor = LineColors.Metro3
            lineWidth = 3.0
        }
        
        // Tram lines (Green - same as Metro 1)
        LineLayer(
            id = "tram-lines",
            source = GeoJsonSource(
                id = "tram-source",
                data = Res.readBytes("files/geojson/combined_tram_lines.geojson")
                    .decodeToString()
            )
        ) {
            lineColor = LineColors.Tram6 // Green
            lineWidth = 3.0
        }
        
        // Suburban Railway (Yellow for A1 as default)
        LineLayer(
            id = "suburban-lines",
            source = GeoJsonSource(
                id = "suburban-source",
                data = Res.readBytes("files/geojson/combined_suburban_lines.geojson")
                    .decodeToString()
            )
        ) {
            lineColor = LineColors.SuburbanA1 // Yellow
            lineWidth = 3.0
        }
        
        // Then add stations on top
        // ...
    }
}
```

## Official Line Colors

The app uses official Athens public transport colors defined in `LineColors.kt`:

### Metro Lines
- **M1** (Green Line): `#00A651` - Piraeus to Kifissia
- **M2** (Red Line): `#ED1C24` - Anthoupoli to Elliniko
- **M3** (Blue Line): `#0066B3` - Dimotiko Theatro to Airport
- **M4** (Yellow - planned): `#FFC107`

### Tram Lines
- **T6** (Green): `#00A651`
- **T7** (Green): `#00A651`

### Suburban Railway
- **A1** (Yellow): `#FFD600` - Piraeus to Airport
- **A2** (Purple): `#9C27B0` - Ano Liosia to Airport
- **A3** (Lime Green): `#8BC34A` - Athens to Chalcis
- **A4** (Sky Blue): `#87CEEB` - Piraeus to Kiato

### Other Transit
- **Trolleys**: `#F27C02` (Orange)
- **Buses**: `#009EC6` (Cyan Blue)

## Using LineColors

```kotlin
import com.example.newoasa.theme.LineColors

// Get color by line code
val metro1Color = LineColors.getColorForLine("M1")  // or "1"
val tram6Color = LineColors.getColorForLine("T6")   // or "6"
val suburbanA1 = LineColors.getColorForLine("A1")

// Get color by category (uses default line for category)
val metroColor = LineColors.getColorForCategory("metro")      // Green (M1)
val tramColor = LineColors.getColorForCategory("tram")        // Green (T6)
val suburbanColor = LineColors.getColorForCategory("suburban") // Yellow (A1)

// Get hex string (useful for MapLibre style JSON)
val hexColor = LineColors.getHexColorForLine("M2")  // "#ED1C24"
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
    val lineNumber: String,      // e.g., "1", "2", "3", "A1", "A2"
    val category: String         // "metro", "tram", "suburban"
)
```

## Styling Recommendations

### Station Markers by Line

```kotlin
fun getStationMarkerColor(station: TransitStation): Color {
    // Get color from first line
    val firstLine = station.lines.firstOrNull() ?: return Color.Gray
    
    val lineCode = when (firstLine.category.lowercase()) {
        "metro" -> "M${firstLine.lineNumber}"
        "tram" -> "T${firstLine.lineNumber}"
        "suburban" -> firstLine.lineNumber  // Already has A prefix
        else -> firstLine.lineNumber
    }
    
    return LineColors.getColorForLine(lineCode)
}
```

### Multi-line Transfer Stations

For stations serving multiple lines, you can:

1. **Use primary line color** (first line in the list)
2. **Use white marker with colored ring**
3. **Create multi-color ring marker**

```kotlin
// Example: White center with colored stroke
CircleLayer(...) {
    circleRadius = 6.0
    circleColor = Color.White
    circleStrokeColor = getStationMarkerColor(station)
    circleStrokeWidth = 2.5
}
```

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

### Wrong Line Colors

1. Ensure you're using `LineColors.getColorForLine(lineCode)`
2. Verify line codes follow the format: M1, M2, T6, A1, etc.
3. Check that category names match: "metro", "tram", "suburban"

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
