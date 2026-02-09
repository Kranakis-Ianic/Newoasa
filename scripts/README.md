# Scripts Directory

This directory contains Python scripts for processing and generating transit data for the Newoasa app.

## Scripts

### `generate_transit_repository.py`
Generates the `TransitLineRepository.kt` file from GeoJSON files.

**Usage:**
```bash
python scripts/generate_transit_repository.py
```

### `combine_metro_tram_stations.py`
Combines nearby metro and tram stations into unified interchange stations.

**What it does:**
- Scans all metro and tram GeoJSON files
- Identifies stations within 200 meters of each other
- Combines them into single stations showing all lines that pass through
- Generates both a GeoJSON file and Kotlin data class

**Usage:**
```bash
python scripts/combine_metro_tram_stations.py
```

**Outputs:**
- `composeApp/src/commonMain/composeResources/files/combined_metro_tram_stations.json`
- `composeApp/src/commonMain/kotlin/com/example/newoasa/data/CombinedStations.kt`

**Configuration:**
- Combination radius: 200 meters (adjustable in script)
- Metro lines: 1, 2, 3
- Tram lines: T6, T7

## Requirements

Python 3.6 or higher (no external dependencies required)

## When to Run

Run these scripts whenever:
- GeoJSON files are updated
- New transit lines are added
- Station data needs to be regenerated
