#!/usr/bin/env python3
"""
Merge Close Route Segments

This script identifies route segments of the same transit line that are within 50 meters
of each other and merges them into a single line. Segments that are far apart are kept separate.

This version works on the actual GeoJSON route files in:
composeApp/src/commonMain/composeResources/files/

Usage:
    python scripts/merge_close_route_segments.py
"""

import json
import os
import math
from pathlib import Path
from typing import List, Tuple, Dict, Set
from collections import defaultdict


def haversine_distance(coord1: Tuple[float, float], coord2: Tuple[float, float]) -> float:
    """
    Calculate the Haversine distance between two coordinates in meters.
    coord format: (longitude, latitude)
    """
    lon1, lat1 = coord1
    lon2, lat2 = coord2
    
    # Convert to radians
    lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])
    
    # Haversine formula
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
    c = 2 * math.asin(math.sqrt(a))
    
    # Earth's radius in meters
    r = 6371000
    return r * c


def min_distance_between_segments(seg1: List[Tuple[float, float]], 
                                   seg2: List[Tuple[float, float]]) -> float:
    """
    Calculate the minimum distance between any two points in two line segments.
    Returns the minimum distance in meters.
    """
    min_dist = float('inf')
    
    # Sample points to check (check every 5th point for performance, plus endpoints)
    sample_indices_1 = list(range(0, len(seg1), 5)) + [len(seg1) - 1]
    sample_indices_2 = list(range(0, len(seg2), 5)) + [len(seg2) - 1]
    
    for i in sample_indices_1:
        for j in sample_indices_2:
            dist = haversine_distance(seg1[i], seg2[j])
            min_dist = min(min_dist, dist)
            
            # Early exit if we found points very close
            if min_dist < 10:  # 10 meters
                return min_dist
    
    return min_dist


def are_segments_close(seg1: List[Tuple[float, float]], 
                       seg2: List[Tuple[float, float]], 
                       threshold_meters: float = 50) -> bool:
    """
    Check if two line segments are within the threshold distance of each other.
    """
    return min_distance_between_segments(seg1, seg2) <= threshold_meters


def merge_linestrings(segments: List[List[Tuple[float, float]]]) -> List[Tuple[float, float]]:
    """
    Merge multiple LineString segments into one.
    This is a simple concatenation - more sophisticated merging could be implemented.
    """
    if not segments:
        return []
    
    if len(segments) == 1:
        return segments[0]
    
    # Start with the first segment
    merged = list(segments[0])
    
    # Add remaining segments
    for seg in segments[1:]:
        # Check if we should connect end-to-start or need to reverse
        if len(merged) > 0 and len(seg) > 0:
            # Simple concatenation, avoiding duplicate points at junction
            last_point = merged[-1]
            first_point = seg[0]
            
            if haversine_distance(last_point, first_point) < 100:  # 100m threshold for connection
                # Connected segments - skip duplicate point
                merged.extend(seg[1:])
            else:
                # Disconnected - add all points
                merged.extend(seg)
        else:
            merged.extend(seg)
    
    return merged


def group_close_segments(segments: List[Dict], threshold_meters: float = 50) -> List[List[int]]:
    """
    Group segments that are within threshold_meters of each other.
    Returns a list of groups, where each group is a list of segment indices.
    """
    n = len(segments)
    
    # Extract coordinates from segments
    coords = []
    for seg in segments:
        if seg['geometry']['type'] == 'LineString':
            coords.append(seg['geometry']['coordinates'])
        else:
            coords.append([])
    
    # Build adjacency list
    close_to = defaultdict(set)
    
    print(f"  Analyzing {n} segments for proximity...")
    for i in range(n):
        if not coords[i]:
            continue
        for j in range(i + 1, n):
            if not coords[j]:
                continue
            
            if are_segments_close(coords[i], coords[j], threshold_meters):
                close_to[i].add(j)
                close_to[j].add(i)
    
    # Group connected segments using DFS
    visited = set()
    groups = []
    
    def dfs(node: int, group: List[int]):
        visited.add(node)
        group.append(node)
        for neighbor in close_to[node]:
            if neighbor not in visited:
                dfs(neighbor, group)
    
    for i in range(n):
        if i not in visited and coords[i]:
            group = []
            dfs(i, group)
            groups.append(group)
    
    # Add isolated segments as single-element groups
    for i in range(n):
        if i not in visited and coords[i]:
            groups.append([i])
    
    return groups


def process_geojson_file(input_path: Path, threshold_meters: float = 50):
    """
    Process a single GeoJSON file and merge close segments IN PLACE.
    """
    with open(input_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    if data.get('type') != 'FeatureCollection':
        print(f"  Warning: {input_path.name} is not a FeatureCollection, skipping")
        return
    
    features = data.get('features', [])
    
    # Separate route paths from stops
    route_features = []
    stop_features = []
    
    for feature in features:
        feat_type = feature.get('properties', {}).get('type', '')
        geom_type = feature.get('geometry', {}).get('type', '')
        
        if feat_type in ['route_path', 'Line'] or \
           (not feat_type and geom_type in ['LineString', 'MultiLineString']):
            route_features.append(feature)
        else:
            stop_features.append(feature)
    
    if not route_features:
        print(f"  No route features found in {input_path.name}")
        return
    
    print(f"  Found {len(route_features)} route segments")
    
    # Group close segments
    groups = group_close_segments(route_features, threshold_meters)
    
    print(f"  Grouped into {len(groups)} segment groups")
    
    # Create merged features
    merged_features = []
    
    for group in groups:
        if len(group) == 1:
            # Single segment, keep as is
            merged_features.append(route_features[group[0]])
        else:
            # Multiple segments - merge them
            print(f"    Merging {len(group)} close segments into one")
            
            segments_to_merge = []
            for idx in group:
                feature = route_features[idx]
                if feature['geometry']['type'] == 'LineString':
                    segments_to_merge.append(feature['geometry']['coordinates'])
            
            merged_coords = merge_linestrings(segments_to_merge)
            
            # Create merged feature using properties from first segment
            merged_feature = {
                'type': 'Feature',
                'properties': route_features[group[0]].get('properties', {}),
                'geometry': {
                    'type': 'LineString',
                    'coordinates': merged_coords
                }
            }
            
            merged_features.append(merged_feature)
    
    # Combine with stops
    output_features = merged_features + stop_features
    
    # Create output GeoJSON
    output_data = {
        'type': 'FeatureCollection',
        'features': output_features
    }
    
    # Write back to same file
    with open(input_path, 'w', encoding='utf-8') as f:
        json.dump(output_data, f, ensure_ascii=False, indent=2)
    
    print(f"  ✓ Merged {len(route_features)} → {len(merged_features)} route segments")


def find_geojson_files(base_path: Path) -> List[Path]:
    """
    Recursively find all .geojson files under the base path.
    """
    geojson_files = []
    for root, dirs, files in os.walk(base_path):
        for file in files:
            if file.endswith('.geojson'):
                geojson_files.append(Path(root) / file)
    return geojson_files


def main():
    """
    Main function to process all transit line GeoJSON files.
    """
    print("="*60)
    print("Merge Close Route Segments Script")
    print("="*60)
    print(f"Threshold: 50 meters\n")
    
    # Base path where GeoJSON files are stored
    base_path = Path('composeApp/src/commonMain/composeResources/files')
    
    if not base_path.exists():
        print(f"Error: Base path not found: {base_path}")
        print("Please run this script from the repository root directory.")
        return
    
    # Find all GeoJSON files (excluding combined_metro_tram_stations.json)
    all_files = find_geojson_files(base_path)
    geojson_files = [
        f for f in all_files 
        if 'combined_metro_tram_stations' not in f.name
    ]
    
    if not geojson_files:
        print(f"No GeoJSON files found in {base_path}")
        return
    
    print(f"Found {len(geojson_files)} GeoJSON files to process\n")
    
    # Process each file
    for geojson_file in sorted(geojson_files):
        print(f"\nProcessing: {geojson_file.relative_to(base_path)}")
        try:
            process_geojson_file(geojson_file, threshold_meters=50)
        except Exception as e:
            print(f"  Error: {e}")
    
    print("\n" + "="*60)
    print("Processing complete!")
    print("="*60)


if __name__ == '__main__':
    main()
