#!/usr/bin/env python3
"""
Combines all transit line GeoJSON files into category-specific combined files
and generates a single all-stations GeoJSON file, plus a final_all_lines file with all lines.

Usage:
    python scripts/combine_transit_lines.py

Output files:
    - composeApp/src/commonMain/composeResources/files/geojson/combined_metro_lines.geojson
    - composeApp/src/commonMain/composeResources/files/geojson/combined_tram_lines.geojson
    - composeApp/src/commonMain/composeResources/files/geojson/combined_suburban_lines.geojson
    - composeApp/src/commonMain/composeResources/files/geojson/all_transit_stations.geojson
    - composeApp/src/commonMain/composeResources/files/geojson/final_all_lines.geojson
"""

import json
import os
from pathlib import Path
from typing import List, Dict, Set, Tuple
import sys

# Define paths
SCRIPT_DIR = Path(__file__).parent
PROJECT_ROOT = SCRIPT_DIR.parent
GEOJSON_DIR = PROJECT_ROOT / "composeApp/src/commonMain/composeResources/files/geojson"
OUTPUT_DIR = GEOJSON_DIR

# Categories to process
CATEGORIES = {
    "Metro lines": "metro",
    "Tram lines": "tram",
    "Suburban Railway lines": "suburban"
}

# Official line colors (matching LineColors.kt)
LINE_COLORS = {
    # Metro
    "M1": "#00A651",  # Green
    "1": "#00A651",
    "M2": "#ED1C24",  # Red
    "2": "#ED1C24",
    "M3": "#0066B3",  # Blue
    "3": "#0066B3",
    "M4": "#FFC107",  # Yellow
    "4": "#FFC107",
    # Tram
    "T6": "#00A651",  # Green
    "6": "#00A651",
    "T7": "#00A651",  # Green
    "7": "#00A651",
    # Suburban
    "A1": "#FFD600",  # Yellow
    "A2": "#9C27B0",  # Purple
    "A3": "#8BC34A",  # Lime Green
    "A4": "#87CEEB",  # Sky Blue
}

# Default category colors
CATEGORY_COLORS = {
    "metro": "#00A651",
    "tram": "#00A651",
    "suburban": "#FFD600",
    "trolleys": "#F27C02",
    "buses": "#009EC6"
}


def get_line_color(line_number: str, category: str) -> str:
    """Get the official color for a line."""
    # Try with line number directly
    if line_number in LINE_COLORS:
        return LINE_COLORS[line_number]
    
    # Try with category prefix
    if category == "metro":
        key = f"M{line_number}"
    elif category == "tram":
        key = f"T{line_number}"
    else:
        key = line_number
    
    if key in LINE_COLORS:
        return LINE_COLORS[key]
    
    # Fall back to category color
    return CATEGORY_COLORS.get(category, "#666666")


def load_geojson(file_path: Path) -> Dict:
    """Load and parse a GeoJSON file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    except Exception as e:
        print(f"Error loading {file_path}: {e}", file=sys.stderr)
        return None


def extract_line_features(geojson: Dict, line_number: str = None, category: str = None) -> List[Dict]:
    """Extract only the LineString features (not stations)."""
    if not geojson or 'features' not in geojson:
        return []
    
    line_features = []
    for feature in geojson['features']:
        if feature.get('geometry', {}).get('type') == 'LineString':
            # Add line metadata to properties
            if line_number and category:
                if 'properties' not in feature:
                    feature['properties'] = {}
                feature['properties']['lineNumber'] = line_number
                feature['properties']['category'] = category
                feature['properties']['lineColor'] = get_line_color(line_number, category)
            line_features.append(feature)
    
    return line_features


def extract_station_features(geojson: Dict, line_number: str, category: str) -> List[Dict]:
    """Extract station Point features and add line metadata."""
    if not geojson or 'features' not in geojson:
        return []
    
    station_features = []
    for feature in geojson['features']:
        props = feature.get('properties', {})
        geom = feature.get('geometry', {})
        
        # Only process Point features that are stations
        if geom.get('type') != 'Point':
            continue
        
        # Skip if it's a railway crossing
        railway_type = props.get('railway', '')
        if railway_type in ['railway_crossing', 'crossing']:
            continue
        
        # Check if it's a station
        feature_type = props.get('type', '')
        if feature_type != 'Station':
            continue
        
        # Add line metadata to properties
        if 'lines' not in props:
            props['lines'] = []
        
        props['lines'].append({
            'lineNumber': line_number,
            'category': category
        })
        
        station_features.append(feature)
    
    return station_features


def combine_lines_for_category(category_dir: str, category_name: str) -> Dict:
    """Combine all line files for a specific category."""
    category_path = GEOJSON_DIR / category_dir
    
    if not category_path.exists():
        print(f"Warning: Category directory not found: {category_path}")
        return None
    
    combined_features = []
    
    # Iterate through line number directories
    for line_dir in sorted(category_path.iterdir()):
        if not line_dir.is_dir():
            continue
        
        line_number = line_dir.name
        print(f"  Processing {category_name} line {line_number}...")
        
        # Process all GeoJSON files in this line directory
        for geojson_file in sorted(line_dir.glob('*.geojson')):
            geojson = load_geojson(geojson_file)
            if geojson:
                line_features = extract_line_features(geojson, line_number, category_name)
                combined_features.extend(line_features)
                print(f"    Added {len(line_features)} line segments from {geojson_file.name}")
    
    return {
        "type": "FeatureCollection",
        "features": combined_features
    }


def combine_all_lines() -> Dict:
    """Combine all transit lines from all categories into one file."""
    all_features = []
    
    for category_dir, category_name in CATEGORIES.items():
        category_path = GEOJSON_DIR / category_dir
        
        if not category_path.exists():
            continue
        
        print(f"  Adding {category_name} lines to final_all_lines...")
        
        for line_dir in sorted(category_path.iterdir()):
            if not line_dir.is_dir():
                continue
            
            line_number = line_dir.name
            
            for geojson_file in sorted(line_dir.glob('*.geojson')):
                geojson = load_geojson(geojson_file)
                if geojson:
                    line_features = extract_line_features(geojson, line_number, category_name)
                    all_features.extend(line_features)
        
        print(f"    Total features so far: {len(all_features)}")
    
    return {
        "type": "FeatureCollection",
        "features": all_features
    }


def extract_all_stations() -> Dict:
    """Extract all unique stations from all transit categories."""
    all_stations = {}
    
    for category_dir, category_name in CATEGORIES.items():
        category_path = GEOJSON_DIR / category_dir
        
        if not category_path.exists():
            continue
        
        print(f"  Extracting stations from {category_name}...")
        
        for line_dir in sorted(category_path.iterdir()):
            if not line_dir.is_dir():
                continue
            
            line_number = line_dir.name
            
            for geojson_file in sorted(line_dir.glob('*.geojson')):
                geojson = load_geojson(geojson_file)
                if geojson:
                    stations = extract_station_features(geojson, line_number, category_name)
                    
                    for station in stations:
                        station_id = station['properties'].get('@id', '')
                        if not station_id:
                            continue
                        
                        if station_id in all_stations:
                            # Merge line information
                            existing_lines = all_stations[station_id]['properties'].get('lines', [])
                            new_lines = station['properties'].get('lines', [])
                            
                            # Combine and deduplicate lines
                            all_lines = existing_lines + new_lines
                            unique_lines = []
                            seen = set()
                            for line in all_lines:
                                key = (line['lineNumber'], line['category'])
                                if key not in seen:
                                    seen.add(key)
                                    unique_lines.append(line)
                            
                            all_stations[station_id]['properties']['lines'] = unique_lines
                        else:
                            all_stations[station_id] = station
        
        print(f"    Total unique stations so far: {len(all_stations)}")
    
    return {
        "type": "FeatureCollection",
        "features": list(all_stations.values())
    }


def save_geojson(data: Dict, output_path: Path):
    """Save GeoJSON data to file."""
    try:
        output_path.parent.mkdir(parents=True, exist_ok=True)
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        print(f"  Saved to {output_path}")
    except Exception as e:
        print(f"Error saving {output_path}: {e}", file=sys.stderr)


def main():
    print("\n" + "="*60)
    print("Transit Lines and Stations Combiner")
    print("="*60 + "\n")
    
    # Ensure output directory exists
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    
    # Combine lines for each category
    for category_dir, category_name in CATEGORIES.items():
        print(f"Combining {category_name} lines...")
        combined = combine_lines_for_category(category_dir, category_name)
        
        if combined and combined['features']:
            output_file = OUTPUT_DIR / f"combined_{category_name}_lines.geojson"
            save_geojson(combined, output_file)
            print(f"  Total features: {len(combined['features'])}\n")
        else:
            print(f"  No features found for {category_name}\n")
    
    # Combine ALL lines into final_all_lines.geojson
    print("Combining all transit lines into final_all_lines.geojson...")
    all_lines = combine_all_lines()
    
    if all_lines and all_lines['features']:
        output_file = OUTPUT_DIR / "final_all_lines.geojson"
        save_geojson(all_lines, output_file)
        print(f"  Total line features: {len(all_lines['features'])}\n")
    else:
        print("  No lines found\n")
    
    # Extract all stations
    print("Extracting all transit stations...")
    all_stations = extract_all_stations()
    
    if all_stations and all_stations['features']:
        output_file = OUTPUT_DIR / "all_transit_stations.geojson"
        save_geojson(all_stations, output_file)
        print(f"  Total unique stations: {len(all_stations['features'])}\n")
    else:
        print("  No stations found\n")
    
    print("="*60)
    print("Done!")
    print("="*60)


if __name__ == "__main__":
    main()
