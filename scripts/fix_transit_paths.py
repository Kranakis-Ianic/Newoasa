#!/usr/bin/env python3
"""
Script to fix the paths in TransitLineRepository.kt
Adds "Bus lines/" to bus and trolley paths
"""

import re

def fix_paths():
    file_path = "../composeApp/src/commonMain/kotlin/com/example/newoasa/data/TransitLineRepository.kt"
    
    print("Reading TransitLineRepository.kt...")
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    print("Applying path fixes...")
    # Fix bus paths
    content = content.replace('"files/geojson/buses/', '"files/geojson/Bus lines/buses/')
    
    # Fix trolley paths  
    content = content.replace('"files/geojson/trolleys/', '"files/geojson/Bus lines/trolleys/')
    
    # Update generation timestamp
    import datetime
    timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    content = re.sub(
        r'Generated: \d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}',
        f'Generated: {timestamp}',
        content
    )
    
    print("Writing updated file...")
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print("✓ Paths fixed successfully!")
    print("  - Bus paths: files/geojson/buses/ → files/geojson/Bus lines/buses/")
    print("  - Trolley paths: files/geojson/trolleys/ → files/geojson/Bus lines/trolleys/")

if __name__ == "__main__":
    fix_paths()
