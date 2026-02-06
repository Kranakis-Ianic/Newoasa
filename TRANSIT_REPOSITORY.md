# 🚌 Transit Line Repository - Quick Start Guide

This guide will help you use and maintain the auto-generated `TransitLineRepository`.

## 🚀 Quick Start

### Using the Repository

```kotlin
import com.example.newoasa.data.TransitLineRepository

// Get all lines
val allLines = TransitLineRepository.getAllLines()

// Find a specific line
val line = TransitLineRepository.getLineByNumber("022")

// Get all buses
val buses = TransitLineRepository.getBusLines()

// Get all trolleys
val trolleys = TransitLineRepository.getTrolleyLines()

// Search
val results = TransitLineRepository.searchLines("30")

// Statistics
val stats = TransitLineRepository.getStats()
```

## 🔄 Regenerating After Adding New Lines

When you add new transit lines to the GeoJSON folder:

```bash
# From project root
python3 scripts/generate_transit_repository.py
```

That's it! The repository will be automatically updated.

## 📊 Current Statistics

- **Total Lines**: 201
- **Bus Lines**: 181
- **Trolley Lines**: 20

## 📁 Folder Structure

Transit lines are organized in:
```
composeApp/src/commonMain/composeResources/files/geojson/
├── buses/
│   ├── 022/
│   │   ├── route_3494.geojson
│   │   └── route_4213.geojson
│   └── ...
└── trolleys/
    ├── 1/
    └── ...
```

## 📝 API Reference

### Get Methods

| Method | Description | Returns |
|--------|-------------|----------|
| `getAllLines()` | Get all transit lines | `List<TransitLine>` |
| `getLineByNumber("022")` | Get specific line | `TransitLine?` |
| `getBusLines()` | Get all bus lines | `List<TransitLine>` |
| `getTrolleyLines()` | Get all trolley lines | `List<TransitLine>` |
| `searchLines("30")` | Search by line number | `List<TransitLine>` |
| `getStats()` | Get repository stats | `RepositoryStats` |

### TransitLine Properties

| Property | Type | Description |
|----------|------|-------------|
| `lineNumber` | `String` | Line identifier (e.g., "022", "309Β") |
| `category` | `String` | "buses" or "trolleys" |
| `routeIds` | `List<String>` | Route IDs for this line |
| `routePaths` | `List<String>` | GeoJSON file paths |
| `displayName` | `String` | Human-readable name |
| `isBus` | `Boolean` | True if bus line |
| `isTrolley` | `Boolean` | True if trolley line |
| `basePath` | `String` | Base GeoJSON folder path |

## ✨ Examples

### Example 1: Display All Buses
```kotlin
@Composable
fun BusLinesList() {
    val busLines = remember { TransitLineRepository.getBusLines() }
    
    LazyColumn {
        items(busLines) { line ->
            ListItem(
                headlineContent = { Text(line.displayName) },
                supportingContent = { Text("${line.routeIds.size} routes") }
            )
        }
    }
}
```

### Example 2: Load Route Data
```kotlin
suspend fun loadLineRoutes(lineNumber: String): List<GeoJson> {
    val line = TransitLineRepository.getLineByNumber(lineNumber)
        ?: return emptyList()
    
    return line.routePaths.map { path ->
        loadGeoJsonFromPath(path)
    }
}
```

### Example 3: Filter by Series
```kotlin
// Get all 300-series buses
val series300 = TransitLineRepository.getBusLines()
    .filter { it.lineNumber.startsWith("3") }
```

### Example 4: Search with Autocomplete
```kotlin
@Composable
fun LineSearchField() {
    var query by remember { mutableStateOf("") }
    val results = remember(query) {
        if (query.isNotEmpty()) {
            TransitLineRepository.searchLines(query)
        } else {
            emptyList()
        }
    }
    
    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search lines") }
        )
        
        results.forEach { line ->
            Text(line.displayName)
        }
    }
}
```

## 🛠️ Adding New Transit Lines

1. **Add GeoJSON files** to the appropriate folder:
   ```
   composeApp/src/commonMain/composeResources/files/geojson/buses/NEW_LINE/
   ├── route_1234.geojson
   └── route_5678.geojson
   ```

2. **Regenerate the repository**:
   ```bash
   python3 scripts/generate_transit_repository.py
   ```

3. **Commit the changes**:
   ```bash
   git add .
   git commit -m "Add new transit line"
   ```

## 📄 Documentation

For more detailed information, see:
- [Full Documentation](composeApp/src/commonMain/kotlin/com/example/newoasa/data/README.md)
- [Usage Examples](composeApp/src/commonMain/kotlin/com/example/newoasa/data/TransitLineRepositoryExample.kt)
- [Python Generator](scripts/generate_transit_repository.py)
- [Kotlin Generator](scripts/GenerateTransitLineRepository.kt)

## ⚠️ Important Notes

- **Never edit** `TransitLineRepository.kt` manually
- Always use the generator script to update
- The repository is thread-safe (singleton object)
- All searches are case-insensitive
- Greek characters (Α, Β, etc.) are preserved

## 🐛 Troubleshooting

### Generator script not found?
```bash
# Make sure you're in the project root
ls scripts/generate_transit_repository.py
```

### Python not installed?
```bash
# Check Python version (requires 3.6+)
python3 --version

# Or use Python 2 compatible version
python --version
```

### No output from generator?
Make sure the GeoJSON folder exists:
```bash
ls composeApp/src/commonMain/composeResources/files/geojson/
```

---

🚀 **Ready to use!** The repository is already populated with 201 transit lines.
