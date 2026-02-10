# Transit Data Processing Scripts

This directory contains Python scripts for processing and combining transit data.

## Scripts

### `combine_transit_lines.py`

Combines all transit line GeoJSON files into category-specific files and extracts all stations.

**Usage:**
```bash
python scripts/combine_transit_lines.py
```

**Output Files:**
- `combined_metro_lines.geojson` - All metro line segments combined
- `combined_tram_lines.geojson` - All tram line segments combined
- `combined_suburban_lines.geojson` - All suburban line segments combined
- `all_transit_stations.geojson` - All unique transit stations with line information

**Features:**
- Combines multiple route files per line into single files
- Removes duplicate line segments
- Extracts and deduplicates stations
- Merges station information when the same station serves multiple lines
- Adds line metadata to station properties

### `generate_transit_repository.py` (existing)

Generates the Kotlin TransitLineRepository class from GeoJSON files.

**Usage:**
```bash
python scripts/generate_transit_repository.py
```

## Requirements

Python 3.7 or higher with standard library only (no external dependencies).

## Directory Structure

```
composeApp/src/commonMain/composeResources/files/geojson/
├── Metro lines/
│   ├── 1/
│   │   ├── metro_1_Kifissia → Piraeus.geojson
│   │   └── metro_1_Piraeus → Kifissia.geojson
│   ├── 2/
│   └── 3/
├── Tram lines/
├── Suburban lines/
├── combined_metro_lines.geojson (generated)
├── combined_tram_lines.geojson (generated)
├── combined_suburban_lines.geojson (generated)
└── all_transit_stations.geojson (generated)
```

## Notes

- The scripts automatically handle Greek and English station names
- Railway crossings are automatically filtered out
- Stations serving multiple lines are merged with all line information preserved
- GeoJSON output uses UTF-8 encoding to properly handle Greek characters
