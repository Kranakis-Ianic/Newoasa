#!/usr/bin/env python3
"""
Combine all transit line GeoJSON files into a single file.
Includes both LineString features (lines) and Point features (stations).
"""

import json
import os
from pathlib import Path

def combine_geojson_files(input_dir, output_file):
    """
    Combine all GeoJSON files from input_dir into a single GeoJSON file.
    Preserves both LineString (routes) and Point (stations) features.
    """
    combined_features = []
    processed_files = 0
    
    # Walk through all subdirectories
    for root, dirs, files in os.walk(input_dir):
        for file in files:
            if file.endswith('.geojson'):
                file_path = os.path.join(root, file)
                
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        data = json.load(f)
                    
                    # Check if it's a valid GeoJSON FeatureCollection
                    if data.get('type') == 'FeatureCollection' and 'features' in data:
                        features = data['features']
                        
                        for feature in features:
                            if feature.get('type') == 'Feature':
                                geometry_type = feature.get('geometry', {}).get('type')
                                
                                # Include LineString (lines) and Point (stations)
                                if geometry_type in ['LineString', 'Point']:
                                    # For LineString features, ensure they have colour property
                                    if geometry_type == 'LineString':
                                        # Check if colour exists in properties or nested in relations
                                        if 'properties' in feature:
                                            props = feature['properties']
                                            
                                            # If colour not at top level, try to extract from relations
                                            if 'colour' not in props:
                                                relations = props.get('@relations', [])
                                                if relations and len(relations) > 0:
                                                    reltags = relations[0].get('reltags', {})
                                                    if 'colour' in reltags:
                                                        props['colour'] = reltags['colour']
                                    
                                    combined_features.append(feature)
                        
                        processed_files += 1
                        print(f"Processed: {file_path}")
                    
                except Exception as e:
                    print(f"Error processing {file_path}: {e}")
    
    # Create combined FeatureCollection
    combined_geojson = {
        "type": "FeatureCollection",
        "features": combined_features
    }
    
    # Write to output file
    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)
    
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(combined_geojson, f, ensure_ascii=False, indent=2)
    
    # Print statistics
    line_count = sum(1 for f in combined_features if f.get('geometry', {}).get('type') == 'LineString')
    station_count = sum(1 for f in combined_features if f.get('geometry', {}).get('type') == 'Point')
    
    print(f"\n✅ Combined {processed_files} files")
    print(f"📊 Total features: {len(combined_features)}")
    print(f"   - Lines: {line_count}")
    print(f"   - Stations: {station_count}")
    print(f"💾 Output: {output_file}")

if __name__ == '__main__':
    # Define paths relative to script location
    script_dir = Path(__file__).parent
    project_root = script_dir.parent
    
    # Input directory containing all GeoJSON files
    input_directory = project_root / 'composeApp' / 'src' / 'commonMain' / 'composeResources' / 'files' / 'geojson' / 'Metro lines'
    
    # Output file
    output_file = project_root / 'composeApp' / 'src' / 'commonMain' / 'composeResources' / 'files' / 'geojson' / 'final_all_lines.geojson'
    
    print("🚇 Combining GeoJSON files...")
    print(f"📁 Input: {input_directory}")
    print(f"📄 Output: {output_file}")
    print()
    
    combine_geojson_files(str(input_directory), str(output_file))
    print("\n✨ Done!")
