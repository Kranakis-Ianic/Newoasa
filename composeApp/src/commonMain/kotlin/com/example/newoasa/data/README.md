# Transit Line Repository

This package contains the auto-generated `TransitLineRepository` that provides access to all bus and trolley transit lines in the application.

## Overview

The repository is automatically generated from the GeoJSON folder structure at:
```
composeApp/src/commonMain/composeResources/files/geojson/
├── buses/
│   ├── 022/
│   │   ├── route_3494.geojson
│   │   └── route_4213.geojson
│   ├── 026/
│   └── ...
└── trolleys/
    ├── 1/
    ├── 2/
    └── ...
```

## Usage

### Get All Lines
```kotlin
val allLines = TransitLineRepository.getAllLines()
println("Total lines: ${allLines.size}")
```

### Get a Specific Line
```kotlin
val line022 = TransitLineRepository.getLineByNumber("022")
line022?.let {
    println("Line: ${it.displayName}")
    println("Category: ${it.category}")
    println("Routes: ${it.routeIds.size}")
}
```

### Get Lines by Category
```kotlin
// Get all bus lines
val busLines = TransitLineRepository.getBusLines()
println("Total bus lines: ${busLines.size}")

// Get all trolley lines
val trolleyLines = TransitLineRepository.getTrolleyLines()
println("Total trolley lines: ${trolleyLines.size}")
```

### Search Lines
```kotlin
// Search for lines containing "30"
val searchResults = TransitLineRepository.searchLines("30")
searchResults.forEach { line ->
    println("Found: ${line.displayName}")
}
```

### Get Statistics
```kotlin
val stats = TransitLineRepository.getStats()
println("""
    Total Lines: ${stats.totalLines}
    Bus Lines: ${stats.totalBusLines}
    Trolley Lines: ${stats.totalTrolleyLines}
    Total Routes: ${stats.totalRoutes}
""")
```

## Data Model

### TransitLine
```kotlin
data class TransitLine(
    val lineNumber: String,      // e.g., "022", "1", "309Β"
    val category: String,         // "buses" or "trolleys"
    val routeIds: List<String>,   // List of route IDs
    val routePaths: List<String>  // Resource paths to GeoJSON files
)
```

### Properties
- **displayName**: Human-readable name (e.g., "Bus 022", "Trolley 1")
- **isBus**: Boolean indicating if this is a bus line
- **isTrolley**: Boolean indicating if this is a trolley line
- **basePath**: Base path to the line's GeoJSON folder

## Regenerating the Repository

When you add new transit lines to the `geojson` folder:

1. **Navigate to the scripts directory:**
   ```bash
   cd scripts
   ```

2. **Run the generator script:**
   ```bash
   kotlinc -script GenerateTransitLineRepository.kt
   ```

The script will:
- Scan all folders in `geojson/buses/` and `geojson/trolleys/`
- Extract route information from each line folder
- Generate a new `TransitLineRepository.kt` file with all the data

## Current Statistics

- **Total Lines**: 201
  - **Bus Lines**: 181
  - **Trolley Lines**: 20

## File Structure

```
composeApp/src/commonMain/kotlin/com/example/newoasa/data/
├── TransitLineRepository.kt  # Auto-generated repository
└── README.md                # This file

scripts/
└── GenerateTransitLineRepository.kt  # Generator script
```

## Notes

- ⚠️ **DO NOT EDIT** `TransitLineRepository.kt` manually!
- Always use the generator script to update the repository
- The repository is a singleton object for efficient memory usage
- All queries are case-insensitive for better user experience
- Greek characters in line numbers (e.g., Α, Β) are preserved

## Examples

### Display All Bus Lines in a UI
```kotlin
@Composable
fun BusLinesList() {
    val busLines = TransitLineRepository.getBusLines()
    
    LazyColumn {
        items(busLines) { line ->
            Text(text = line.displayName)
        }
    }
}
```

### Load Route GeoJSON
```kotlin
suspend fun loadRouteData(lineNumber: String) {
    val line = TransitLineRepository.getLineByNumber(lineNumber)
    line?.routePaths?.forEach { path ->
        // Load GeoJSON from path
        val geoJson = loadResource(path)
        // Process the route data
    }
}
```

### Filter by Line Number Prefix
```kotlin
// Get all 700-series buses
val series700 = TransitLineRepository.getBusLines()
    .filter { it.lineNumber.startsWith("7") }
```
