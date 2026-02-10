# Python Scripts Usage Guide

## Overview

This project includes Python scripts to process and combine transit data from GeoJSON files.

## Prerequisites

- Python 3.7 or higher
- No external dependencies required (uses only standard library)

## Scripts

### 1. Generate Transit Repository

**Script:** `scripts/generate_transit_repository.py`

**Purpose:** Generates the Kotlin `TransitLineRepository.kt` file that contains references to all transit line GeoJSON files.

**Usage:**
```bash
python scripts/generate_transit_repository.py
```

**Output:** `composeApp/src/commonMain/kotlin/com/example/newoasa/data/TransitLineRepository.kt`

**What it does:**
- Scans all GeoJSON files in the transit categories
- Extracts line numbers, categories, and file paths
- Generates a Kotlin data class and repository
- Creates type-safe access to all transit lines

---

### 2. Combine Transit Lines

**Script:** `scripts/combine_transit_lines.py`

**Purpose:** Combines all transit line files into single category files and extracts all stations.

**Usage:**
```bash
python scripts/combine_transit_lines.py
```

**Output Files:**
```
composeApp/src/commonMain/composeResources/files/geojson/
├── combined_metro_lines.geojson      # All metro line segments
├── combined_tram_lines.geojson       # All tram line segments
├── combined_suburban_lines.geojson   # All suburban line segments
└── all_transit_stations.geojson      # All unique stations with lines
```

**What it does:**
- Combines multiple route files per line (e.g., both directions)
- Extracts LineString features (the actual line paths)
- Removes duplicate line segments
- Extracts all station points
- Merges stations serving multiple lines
- Adds line metadata to each station

**Station Output Format:**
```json
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "@id": "node/97819323",
        "name": "Μοναστηράκι",
        "int_name": "Monastiraki",
        "lines": [
          {"lineNumber": "1", "category": "metro"},
          {"lineNumber": "3", "category": "metro"}
        ]
      },
      "geometry": {
        "type": "Point",
        "coordinates": [23.7249306, 37.9758884]
      }
    }
  ]
}
```

---

## Workflow

### Initial Setup

1. Place your GeoJSON files in the correct directory structure:
```
composeApp/src/commonMain/composeResources/files/geojson/
├── Metro lines/
│   ├── 1/
│   │   ├── metro_1_Kifissia → Piraeus.geojson
│   │   └── metro_1_Piraeus → Kifissia.geojson
│   ├── 2/
│   └── 3/
├── Tram lines/
│   ├── 6/
│   ├── 7/
│   └── 7A/
├── Suburban lines/
│   ├── S1/
│   └── S2/
└── Bus lines/ (optional)
```

2. Generate the repository:
```bash
python scripts/generate_transit_repository.py
```

3. Combine the lines and extract stations:
```bash
python scripts/combine_transit_lines.py
```

### When to Re-run Scripts

**Re-run `generate_transit_repository.py` when:**
- Adding new transit line GeoJSON files
- Removing or renaming line files
- Changing directory structure

**Re-run `combine_transit_lines.py` when:**
- Updating line route data
- Adding new stations
- Modifying station names or properties
- Changing line assignments

---

## Advanced Usage

### Customizing Categories

Edit `combine_transit_lines.py` to add more categories:

```python
CATEGORIES = {
    "Metro lines": "metro",
    "Tram lines": "tram",
    "Suburban lines": "suburban",
    "Bus lines": "bus",  # Add this
}
```

### Filtering Stations

Modify the `extract_station_features` function to filter specific stations:

```python
def extract_station_features(geojson, line_number, category):
    # ... existing code ...
    
    # Add custom filtering
    station_name = props.get('name', '')
    if 'Depot' in station_name:  # Skip depot stations
        continue
    
    # ... rest of code ...
```

### Adjusting Station Clustering

Modify the clustering threshold in `StationParser.kt`:

```kotlin
fun clusterStations(
    stations: List<TransitStation>,
    thresholdDegrees: Double = 0.0002  // Increase for more aggressive clustering
): List<TransitStation>
```

---

## Validation

After running the scripts, validate the output:

### Check Combined Line Files

```bash
# Count features in combined files
jq '.features | length' composeApp/src/commonMain/composeResources/files/geojson/combined_metro_lines.geojson
```

### Check Station File

```bash
# Count unique stations
jq '.features | length' composeApp/src/commonMain/composeResources/files/geojson/all_transit_stations.geojson

# List stations with multiple lines
jq '.features[] | select(.properties.lines | length > 1) | .properties.name' all_transit_stations.geojson
```

### Verify Line Assignments

```bash
# Show all lines at a specific station
jq '.features[] | select(.properties.name == "Μοναστηράκι") | .properties.lines' all_transit_stations.geojson
```

---

## Troubleshooting

### Script Fails to Find Files

**Error:** `Warning: Category directory not found`

**Solution:** Verify the GeoJSON directory structure matches the expected layout.

### Unicode/Greek Characters Issue

**Error:** `UnicodeDecodeError`

**Solution:** Ensure your GeoJSON files are UTF-8 encoded:
```bash
file -i your_file.geojson  # Should show charset=utf-8
```

### Empty Output Files

**Problem:** Generated files have no features

**Check:**
1. Source GeoJSON files are valid
2. Directory structure is correct
3. Line files contain the expected feature types

### Station Duplicates

**Problem:** Same station appears multiple times

**Cause:** Stations don't have consistent `@id` fields

**Solution:** Ensure all stations have unique OSM node IDs in the `@id` property.

---

## File Size Considerations

The combined files can be large:

- **Metro lines**: ~2-5 MB
- **Tram lines**: ~1-3 MB  
- **All stations**: ~500 KB - 2 MB

For mobile apps, consider:
1. Compressing GeoJSON files (gzip)
2. Loading files on-demand
3. Using vector tiles instead of GeoJSON
4. Simplifying geometries for lower zoom levels
