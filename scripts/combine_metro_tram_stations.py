#!/usr/bin/env python3
"""
Combine Metro, Tram, and Suburban stations into a single GeoJSON file.
This creates "combined stations" where lines intersect (e.g., Syntagma).

New structure scanned:
- files/geojson/Metro lines/{1,2,3}/*.geojson
- files/geojson/Tram lines/{T6,T7}/*.geojson
- files/geojson/Suburban Railway lines/{A1,A2,A3,A4}/*.geojson
"""

import json
import math
from pathlib import Path
from typing import List, Dict, Set

# Base configuration
SCRIPT_DIR = Path(__file__).parent.absolute()
PROJECT_ROOT = SCRIPT_DIR.parent
GEOJSON_BASE = PROJECT_ROOT / "composeApp/src/commonMain/composeResources/files/geojson"
OUTPUT_FILE = PROJECT_ROOT / "composeApp/src/commonMain/composeResources/files/combined_metro_tram_stations.json"

# Distance threshold to consider stops as the same station (in meters)
COMBINATION_RADIUS_METERS = 200

def haversine_distance(lat1, lon1, lat2, lon2):
    """Calculate distance between two points in meters."""
    R = 6371000  # Earth radius in meters
    phi1, phi2 = math.radians(lat1), math.radians(lat2)
    dphi = math.radians(lat2 - lat1)
    dlambda = math.radians(lon2 - lon1)
    
    a = math.sin(dphi/2)**2 + math.cos(phi1) * math.cos(phi2) * math.sin(dlambda/2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    
    return R * c

def get_geojson_files_recursive(folder: Path) -> List[Path]:
    """Get all .geojson files in a folder and subfolders."""
    if not folder.exists():
        return []
    return list(folder.glob("**/*.geojson"))

def extract_stops(file_path: Path, line_id: str) -> List[Dict]:
    """Extract stops from a GeoJSON file."""
    stops = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
            
        features = data.get('features', [])
        for feature in features:
            props = feature.get('properties', {})
            geom = feature.get('geometry', {})
            
            # Check if it's a stop
            is_stop = props.get('type') == 'stop' or \
                      (geom.get('type') == 'Point' and 'name' in props)
            
            if is_stop and geom.get('coordinates'):
                coords = geom['coordinates']
                stops.append({
                    'name': props.get('name', 'Unknown'),
                    'lat': coords[1],
                    'lon': coords[0],
                    'lines': {line_id}
                })
    except Exception as e:
        print(f"Error reading {file_path.name}: {e}")
    
    return stops

def main():
    print("=" * 60)
    print("Combining Metro, Tram, and Suburban Stations")
    print("=" * 60)
    print(f"Scanning directory: {GEOJSON_BASE}")

    if not GEOJSON_BASE.exists():
        print(f"❌ Error: Base directory not found!")
        exit(1)

    all_stops = []

    # 1. Scan Metro Lines
    metro_base = GEOJSON_BASE / "Metro lines"
    if metro_base.exists():
        for line_dir in metro_base.iterdir():
            if line_dir.is_dir():
                line_id = line_dir.name  # "1", "2", "3"
                files = get_geojson_files_recursive(line_dir)
                print(f"Scanning Metro Line {line_id} ({len(files)} files)...")
                for f in files:
                    all_stops.extend(extract_stops(f, line_id))

    # 2. Scan Tram Lines
    tram_base = GEOJSON_BASE / "Tram lines"
    if tram_base.exists():
        for line_dir in tram_base.iterdir():
            if line_dir.is_dir():
                line_id = line_dir.name  # "T6", "T7"
                files = get_geojson_files_recursive(line_dir)
                print(f"Scanning Tram Line {line_id} ({len(files)} files)...")
                for f in files:
                    all_stops.extend(extract_stops(f, line_id))

    # 3. Scan Suburban Lines
    suburban_base = GEOJSON_BASE / "Suburban Railway lines"
    if suburban_base.exists():
        for line_dir in suburban_base.iterdir():
            if line_dir.is_dir():
                line_id = line_dir.name  # "A1", "A2", "A3", "A4"
                files = get_geojson_files_recursive(line_dir)
                print(f"Scanning Suburban Line {line_id} ({len(files)} files)...")
                for f in files:
                    all_stops.extend(extract_stops(f, line_id))

    print(f"\nFound {len(all_stops)} total raw stops.")
    
    if not all_stops:
        print("❌ No stops found to combine!")
        exit(1)

    # Combine nearby stops
    combined_stations = []
    
    # Simple clustering
    processed_indices = set()
    
    for i, stop1 in enumerate(all_stops):
        if i in processed_indices:
            continue
            
        current_cluster = [stop1]
        processed_indices.add(i)
        
        # Find all other stops close to this one
        for j, stop2 in enumerate(all_stops):
            if j in processed_indices:
                continue
                
            dist = haversine_distance(stop1['lat'], stop1['lon'], stop2['lat'], stop2['lon'])
            
            # Same name is a strong indicator, otherwise check distance
            same_name = stop1['name'].lower() == stop2['name'].lower()
            if (dist < COMBINATION_RADIUS_METERS) or (same_name and dist < 500):
                current_cluster.append(stop2)
                processed_indices.add(j)
        
        # Create combined station from cluster
        # Use average coordinates
        avg_lat = sum(s['lat'] for s in current_cluster) / len(current_cluster)
        avg_lon = sum(s['lon'] for s in current_cluster) / len(current_cluster)
        
        # Merge lines
        all_lines = set()
        for s in current_cluster:
            all_lines.update(s['lines'])
            
        # Use the most common name or the first one
        name = current_cluster[0]['name']
        
        combined_stations.append({
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [avg_lon, avg_lat]
            },
            "properties": {
                "name": name,
                "lines": sorted(list(all_lines)),
                "type": "combined_station"
            }
        })

    # Create FeatureCollection
    output_geojson = {
        "type": "FeatureCollection",
        "features": combined_stations
    }

    # Write output
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        json.dump(output_geojson, f, indent=2, ensure_ascii=False)

    print(f"\n✓ Created combined stations file with {len(combined_stations)} unique stations.")
    print(f"  Output: {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
