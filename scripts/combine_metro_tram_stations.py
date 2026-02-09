#!/usr/bin/env python3
"""
Combine Metro and Tram Stations Script

This script processes GeoJSON files for metro and tram lines, identifies stations
within 200 meters of each other, and combines them into single stations that show
all lines passing through.

Usage:
    python scripts/combine_metro_tram_stations.py

Outputs:
    - composeApp/src/commonMain/composeResources/files/combined_metro_tram_stations.json
    - composeApp/src/commonMain/kotlin/com/example/newoasa/data/CombinedStations.kt
"""

import json
import os
import math
from pathlib import Path
from datetime import datetime
from typing import List, Dict, Set, Tuple

# Constants
COMBINE_RADIUS_METERS = 200
GEOJSON_BASE_PATH = Path("composeApp/src/commonMain/composeResources/files/geojson")
OUTPUT_JSON_PATH = Path("composeApp/src/commonMain/composeResources/files/combined_metro_tram_stations.json")
OUTPUT_KOTLIN_PATH = Path("composeApp/src/commonMain/kotlin/com/example/newoasa/data/CombinedStations.kt")

def haversine_distance(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
    """
    Calculate the great circle distance between two points on Earth in meters.
    """
    R = 6371000  # Earth's radius in meters
    
    phi1 = math.radians(lat1)
    phi2 = math.radians(lat2)
    delta_phi = math.radians(lat2 - lat1)
    delta_lambda = math.radians(lon2 - lon1)
    
    a = math.sin(delta_phi/2)**2 + math.cos(phi1) * math.cos(phi2) * math.sin(delta_lambda/2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    
    return R * c

def load_stops_from_geojson(file_path: Path, line_id: str, category: str) -> List[Dict]:
    """
    Load stops from a GeoJSON file and tag them with line information.
    """
    stops = []
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
            
        features = data.get('features', [])
        
        for feature in features:
            geometry = feature.get('geometry', {})
            properties = feature.get('properties', {})
            
            # Check if this is a stop (Point geometry)
            if geometry.get('type') in ['Point', 'MultiPoint']:
                coords = geometry.get('coordinates', [])
                if len(coords) >= 2:
                    stop = {
                        'name': properties.get('name', 'Unknown'),
                        'stop_code': properties.get('stop_code', ''),
                        'order': properties.get('order', ''),
                        'longitude': coords[0],
                        'latitude': coords[1],
                        'line_id': line_id,
                        'category': category
                    }
                    stops.append(stop)
    
    except Exception as e:
        print(f"Error loading {file_path}: {e}")
    
    return stops

def find_all_metro_tram_stops() -> List[Dict]:
    """
    Find all stops from metro and tram GeoJSON files.
    """
    all_stops = []
    
    # Metro lines (flat structure)
    metro_path = GEOJSON_BASE_PATH / "Metro"
    if metro_path.exists():
        for geojson_file in metro_path.glob("*.geojson"):
            # Extract line number from filename (e.g., route_5646.geojson)
            # We need to determine which metro line this belongs to
            # Let's read the file and check
            try:
                with open(geojson_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    # Try to infer line from properties or filename
                    # For now, we'll process all metro files
                    stops = load_stops_from_geojson(geojson_file, geojson_file.stem, 'metro')
                    all_stops.extend(stops)
            except Exception as e:
                print(f"Error processing {geojson_file}: {e}")
    
    # Better approach: Use the known metro line structure
    metro_lines = {
        '1': ['route_5646.geojson', 'route_5647.geojson'],
        '2': ['route_5648.geojson', 'route_5649.geojson'],
        '3': ['route_5650.geojson', 'route_5651.geojson', 'route_5652.geojson']
    }
    
    all_stops = []  # Reset
    
    for line_num, routes in metro_lines.items():
        for route_file in routes:
            file_path = GEOJSON_BASE_PATH / "Metro" / route_file
            if file_path.exists():
                stops = load_stops_from_geojson(file_path, line_num, 'metro')
                all_stops.extend(stops)
    
    # Tram lines (flat structure)
    tram_lines = {
        'T6': ['route_5653.geojson', 'route_5654.geojson'],
        'T7': ['route_5655.geojson', 'route_5656.geojson']
    }
    
    for line_num, routes in tram_lines.items():
        for route_file in routes:
            file_path = GEOJSON_BASE_PATH / "Tram" / route_file
            if file_path.exists():
                stops = load_stops_from_geojson(file_path, line_num, 'tram')
                all_stops.extend(stops)
    
    print(f"Found {len(all_stops)} total stops from metro and tram lines")
    return all_stops

def combine_nearby_stations(stops: List[Dict]) -> List[Dict]:
    """
    Combine stations that are within COMBINE_RADIUS_METERS of each other.
    Returns list of combined stations with all lines that pass through.
    """
    combined_stations = []
    used_indices = set()
    
    for i, stop1 in enumerate(stops):
        if i in used_indices:
            continue
        
        # Start a new combined station
        station = {
            'name': stop1['name'],
            'stop_codes': {stop1['stop_code']} if stop1['stop_code'] else set(),
            'latitude': stop1['latitude'],
            'longitude': stop1['longitude'],
            'lines': {stop1['line_id']},
            'categories': {stop1['category']}
        }
        
        used_indices.add(i)
        coords_sum_lat = stop1['latitude']
        coords_sum_lon = stop1['longitude']
        count = 1
        
        # Find all nearby stops
        for j, stop2 in enumerate(stops):
            if j in used_indices:
                continue
            
            distance = haversine_distance(
                stop1['latitude'], stop1['longitude'],
                stop2['latitude'], stop2['longitude']
            )
            
            if distance <= COMBINE_RADIUS_METERS:
                used_indices.add(j)
                station['lines'].add(stop2['line_id'])
                station['categories'].add(stop2['category'])
                if stop2['stop_code']:
                    station['stop_codes'].add(stop2['stop_code'])
                
                coords_sum_lat += stop2['latitude']
                coords_sum_lon += stop2['longitude']
                count += 1
                
                # Update name to the most common or longest
                if len(stop2['name']) > len(station['name']):
                    station['name'] = stop2['name']
        
        # Average the coordinates for combined station
        station['latitude'] = coords_sum_lat / count
        station['longitude'] = coords_sum_lon / count
        
        # Convert sets to sorted lists
        station['lines'] = sorted(list(station['lines']))
        station['categories'] = sorted(list(station['categories']))
        station['stop_codes'] = sorted(list(station['stop_codes']))
        
        combined_stations.append(station)
    
    print(f"Combined {len(stops)} stops into {len(combined_stations)} stations")
    return combined_stations

def generate_geojson_output(stations: List[Dict]) -> Dict:
    """
    Generate GeoJSON FeatureCollection from combined stations.
    """
    features = []
    
    for station in stations:
        feature = {
            'type': 'Feature',
            'geometry': {
                'type': 'Point',
                'coordinates': [station['longitude'], station['latitude']]
            },
            'properties': {
                'name': station['name'],
                'stop_codes': station['stop_codes'],
                'lines': station['lines'],
                'categories': station['categories']
            }
        }
        features.append(feature)
    
    return {
        'type': 'FeatureCollection',
        'features': features
    }

def generate_kotlin_output(stations: List[Dict]) -> str:
    """
    Generate Kotlin data class file for combined stations.
    """
    timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    
    kotlin_code = f'''package com.example.newoasa.data

import org.maplibre.android.geometry.LatLng

/**
 * Combined Metro and Tram Stations
 * Auto-generated from GeoJSON files
 * 
 * DO NOT EDIT MANUALLY!
 * Run scripts/combine_metro_tram_stations.py to regenerate this file
 * 
 * Generated: {timestamp}
 * Total Combined Stations: {len(stations)}
 * Combination Radius: {COMBINE_RADIUS_METERS}m
 */

/**
 * Represents a combined transit station (metro/tram) with all lines passing through
 */
data class CombinedStation(
    val name: String,
    val stopCodes: List<String>,
    val coordinate: LatLng,
    val lines: List<String>,  // e.g., ["1", "2"] for metro lines or ["T6", "T7"] for tram
    val categories: List<String>  // e.g., ["metro", "tram"]
) {{
    /**
     * Returns a display string of all lines (e.g., "M1, M2" or "T6")
     */
    val linesDisplay: String
        get() {{
            val formatted = lines.map {{ line ->
                when {{
                    categories.contains("metro") && line.toIntOrNull() != null -> "M$line"
                    categories.contains("tram") && line.startsWith("T") -> line
                    else -> line
                }}
            }}
            return formatted.joinToString(", ")
        }}
}}

/**
 * Repository providing access to all combined metro and tram stations
 */
object CombinedStationsRepository {{
    val stations: List<CombinedStation> = listOf(
'''
    
    # Add each station
    for station in stations:
        stop_codes_str = '[' + ', '.join(f'"{code}"' for code in station['stop_codes']) + ']'
        lines_str = '[' + ', '.join(f'"{line}"' for line in station['lines']) + ']'
        categories_str = '[' + ', '.join(f'"{cat}"' for cat in station['categories']) + ']'
        
        kotlin_code += f'''        CombinedStation(
            name = "{station['name']}",
            stopCodes = listOf({', '.join(f'"{code}"' for code in station['stop_codes'])}),
            coordinate = LatLng({station['latitude']}, {station['longitude']}),
            lines = listOf({', '.join(f'"{line}"' for line in station['lines'])}),
            categories = listOf({', '.join(f'"{cat}"' for cat in station['categories'])})
        ),
'''
    
    kotlin_code += '''    )
    
    /**
     * Find station by approximate coordinate (within 50m)
     */
    fun findStationNear(lat: Double, lon: Double, radiusMeters: Double = 50.0): CombinedStation? {
        return stations.firstOrNull { station ->
            val distance = haversineDistance(
                lat, lon,
                station.coordinate.latitude, station.coordinate.longitude
            )
            distance <= radiusMeters
        }
    }
    
    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // Earth radius in meters
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        
        return R * c
    }
}
'''
    
    return kotlin_code

def main():
    print("="*60)
    print("Combining Metro and Tram Stations")
    print("="*60)
    
    # Load all stops
    all_stops = find_all_metro_tram_stops()
    
    if not all_stops:
        print("No stops found! Check the GeoJSON file paths.")
        return
    
    # Combine nearby stations
    combined_stations = combine_nearby_stations(all_stops)
    
    # Generate GeoJSON output
    geojson_data = generate_geojson_output(combined_stations)
    
    # Write GeoJSON file
    OUTPUT_JSON_PATH.parent.mkdir(parents=True, exist_ok=True)
    with open(OUTPUT_JSON_PATH, 'w', encoding='utf-8') as f:
        json.dump(geojson_data, f, indent=2, ensure_ascii=False)
    print(f"\nGenerated GeoJSON: {OUTPUT_JSON_PATH}")
    
    # Generate Kotlin file
    kotlin_code = generate_kotlin_output(combined_stations)
    
    OUTPUT_KOTLIN_PATH.parent.mkdir(parents=True, exist_ok=True)
    with open(OUTPUT_KOTLIN_PATH, 'w', encoding='utf-8') as f:
        f.write(kotlin_code)
    print(f"Generated Kotlin: {OUTPUT_KOTLIN_PATH}")
    
    print("\n" + "="*60)
    print(f"Successfully combined {len(all_stops)} stops into {len(combined_stations)} stations")
    print("="*60)

if __name__ == '__main__':
    main()
