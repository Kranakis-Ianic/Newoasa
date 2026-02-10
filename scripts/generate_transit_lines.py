#!/usr/bin/env python3
"""
Generate TransitLineRepository.kt by scanning GeoJSON folder structure.

New structure:
- files/geojson/Metro lines/{1,2,3}/
- files/geojson/Tram lines/{T6,T7}/
- files/geojson/Suburban Railway lines/{A1,A2,A3,A4}/
- files/geojson/Bus lines/buses/{line_number}/
- files/geojson/Bus lines/trolleys/{line_number}/

Each metro/tram/suburban line has 2 GeoJSON files (one per direction)
that need to be combined into routePaths.
"""

import os
import sys
from pathlib import Path
from typing import List, Dict

# Determine base path relative to this script
SCRIPT_DIR = Path(__file__).parent.absolute()
PROJECT_ROOT = SCRIPT_DIR.parent
GEOJSON_BASE = PROJECT_ROOT / "composeApp/src/commonMain/composeResources/files/geojson"

def get_geojson_files(line_folder: Path) -> List[str]:
    """Get all .geojson files in a folder, sorted alphabetically."""
    if not line_folder.exists():
        return []
    
    files = sorted([f.name for f in line_folder.glob("*.geojson")])
    return files

def scan_metro_lines() -> List[Dict]:
    """Scan Metro lines (1, 2, 3)."""
    metro_folder = GEOJSON_BASE / "Metro lines"
    lines = []
    
    if not metro_folder.exists():
        print(f"Warning: Metro folder not found at {metro_folder}")
        return []
    
    for line_num in ["1", "2", "3"]:
        line_folder = metro_folder / line_num
        if not line_folder.exists():
            continue
        
        files = get_geojson_files(line_folder)
        if not files:
            continue
        
        # Create paths relative to files/ directory
        route_paths = [f"files/geojson/Metro lines/{line_num}/{f}" for f in files]
        
        lines.append({
            "lineNumber": line_num,
            "displayName": f"Metro Line {line_num}",
            "category": "METRO",
            "routePaths": route_paths
        })
    
    return lines

def scan_tram_lines() -> List[Dict]:
    """Scan Tram lines (T6, T7)."""
    tram_folder = GEOJSON_BASE / "Tram lines"
    lines = []
    
    if not tram_folder.exists():
        print(f"Warning: Tram folder not found at {tram_folder}")
        return []

    for line_num in ["T6", "T7"]:
        line_folder = tram_folder / line_num
        if not line_folder.exists():
            continue
        
        files = get_geojson_files(line_folder)
        if not files:
            continue
        
        route_paths = [f"files/geojson/Tram lines/{line_num}/{f}" for f in files]
        
        lines.append({
            "lineNumber": line_num,
            "displayName": f"Tram {line_num}",
            "category": "TRAM",
            "routePaths": route_paths
        })
    
    return lines

def scan_suburban_lines() -> List[Dict]:
    """Scan Suburban Railway lines (A1, A2, A3, A4)."""
    suburban_folder = GEOJSON_BASE / "Suburban Railway lines"
    lines = []
    
    if not suburban_folder.exists():
        print(f"Warning: Suburban folder not found at {suburban_folder}")
        return []

    for line_num in ["A1", "A2", "A3", "A4"]:
        line_folder = suburban_folder / line_num
        if not line_folder.exists():
            continue
        
        files = get_geojson_files(line_folder)
        if not files:
            continue
        
        route_paths = [f"files/geojson/Suburban Railway lines/{line_num}/{f}" for f in files]
        
        lines.append({
            "lineNumber": line_num,
            "displayName": f"Suburban Railway {line_num}",
            "category": "SUBURBAN",
            "routePaths": route_paths
        })
    
    return lines

def scan_bus_lines() -> List[Dict]:
    """Scan Bus lines."""
    bus_folder = GEOJSON_BASE / "Bus lines" / "buses"
    if not bus_folder.exists():
        return []
    
    lines = []
    for line_folder in sorted(bus_folder.iterdir()):
        if not line_folder.is_dir():
            continue
        
        line_num = line_folder.name
        files = get_geojson_files(line_folder)
        if not files:
            continue
        
        route_paths = [f"files/geojson/Bus lines/buses/{line_num}/{f}" for f in files]
        
        lines.append({
            "lineNumber": line_num,
            "displayName": f"Bus {line_num}",
            "category": "BUS",
            "routePaths": route_paths
        })
    
    return lines

def scan_trolley_lines() -> List[Dict]:
    """Scan Trolley lines."""
    trolley_folder = GEOJSON_BASE / "Bus lines" / "trolleys"
    if not trolley_folder.exists():
        return []
    
    lines = []
    for line_folder in sorted(trolley_folder.iterdir()):
        if not line_folder.is_dir():
            continue
        
        line_num = line_folder.name
        files = get_geojson_files(line_folder)
        if not files:
            continue
        
        route_paths = [f"files/geojson/Bus lines/trolleys/{line_num}/{f}" for f in files]
        
        lines.append({
            "lineNumber": line_num,
            "displayName": f"Trolley {line_num}",
            "category": "TROLLEY",
            "routePaths": route_paths
        })
    
    return lines

def generate_kotlin_code(all_lines: Dict[str, List[Dict]]) -> str:
    """Generate the Kotlin repository file."""
    
    def format_transit_line(line: Dict) -> str:
        paths = ',\n            '.join([f'"{ p}"' for p in line['routePaths']])
        return f"""        TransitLine(
            lineNumber = "{line['lineNumber']}",
            displayName = "{line['displayName']}",
            category = TransitCategory.{line['category']},
            routePaths = listOf(
            {paths}
            )
        )"""
    
    metro_lines = ',\n'.join([format_transit_line(l) for l in all_lines['metro']])
    tram_lines = ',\n'.join([format_transit_line(l) for l in all_lines['tram']])
    suburban_lines = ',\n'.join([format_transit_line(l) for l in all_lines['suburban']])
    bus_lines = ',\n'.join([format_transit_line(l) for l in all_lines['bus']])
    trolley_lines = ',\n'.join([format_transit_line(l) for l in all_lines['trolley']])
    
    # Generate search index
    all_searchable = all_lines['metro'] + all_lines['tram'] + all_lines['suburban'] + all_lines['bus'] + all_lines['trolley']
    
    kotlin_code = f'''package com.example.newoasa.data

/**
 * Transit line categories
 */
enum class TransitCategory {{
    METRO,
    TRAM,
    SUBURBAN,
    BUS,
    TROLLEY
}}

/**
 * Represents a single transit line with its routes
 */
data class TransitLine(
    val lineNumber: String,
    val displayName: String,
    val category: TransitCategory,
    val routePaths: List<String>,  // Paths to GeoJSON files
    val routeIds: List<String> = routePaths  // For compatibility
) {{
    val isMetro: Boolean get() = category == TransitCategory.METRO
    val isTram: Boolean get() = category == TransitCategory.TRAM
    val isSuburban: Boolean get() = category == TransitCategory.SUBURBAN
    val isBus: Boolean get() = category == TransitCategory.BUS
    val isTrolley: Boolean get() = category == TransitCategory.TROLLEY
}}

/**
 * Repository for all transit lines
 * Auto-generated from GeoJSON folder structure
 */
object TransitLineRepository {{
    
    /**
     * Get all Metro lines (1, 2, 3)
     */
    fun getMetroLines(): List<TransitLine> {{
        return listOf(
{metro_lines}
        )
    }}
    
    /**
     * Get all Tram lines (T6, T7)
     */
    fun getTramLines(): List<TransitLine> {{
        return listOf(
{tram_lines}
        )
    }}
    
    /**
     * Get all Suburban Railway lines (A1, A2, A3, A4)
     */
    fun getSuburbanLines(): List<TransitLine> {{
        return listOf(
{suburban_lines}
        )
    }}
    
    /**
     * Get all Bus lines
     */
    fun getBusLines(): List<TransitLine> {{
        return listOf(
{bus_lines}
        )
    }}
    
    /**
     * Get all Trolley lines
     */
    fun getTrolleyLines(): List<TransitLine> {{
        return listOf(
{trolley_lines}
        )
    }}
    
    /**
     * Get all lines (for search)
     */
    fun getAllLines(): List<TransitLine> {{
        return getMetroLines() + 
               getTramLines() + 
               getSuburbanLines() + 
               getBusLines() + 
               getTrolleyLines()
    }}
    
    /**
     * Search lines by line number or display name
     */
    fun searchLines(query: String): List<TransitLine> {{
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) {{
            return emptyList()
        }}
        
        return getAllLines().filter {{
            it.lineNumber.lowercase().contains(normalizedQuery) ||
            it.displayName.lowercase().contains(normalizedQuery)
        }}
    }}
}}
'''
    
    return kotlin_code

def main():
    print("============================================================")
    print("Transit Line Repository Generator (Updated)")
    print("============================================================")
    print(f"Scanning directory: {GEOJSON_BASE}")
    
    if not GEOJSON_BASE.exists():
        print(f"❌ Error: Base directory not found!")
        print(f"Expected: {GEOJSON_BASE}")
        sys.exit(1)

    # Scan all line types
    metro = scan_metro_lines()
    tram = scan_tram_lines()
    suburban = scan_suburban_lines()
    bus = scan_bus_lines()
    trolley = scan_trolley_lines()
    
    all_lines = {
        'metro': metro,
        'tram': tram,
        'suburban': suburban,
        'bus': bus,
        'trolley': trolley
    }
    
    total = len(metro) + len(tram) + len(suburban) + len(bus) + len(trolley)
    
    print(f"Found:")
    print(f"  Metro: {len(metro)} lines")
    print(f"  Tram: {len(tram)} lines")
    print(f"  Suburban: {len(suburban)} lines")
    print(f"  Bus: {len(bus)} lines")
    print(f"  Trolley: {len(trolley)} lines")
    print(f"  Total: {total} lines")
    
    if total == 0:
        print("\n❌ No lines found! Check your folder structure.")
        sys.exit(1)
    
    # Generate Kotlin code
    kotlin_code = generate_kotlin_code(all_lines)
    
    # Write to file
    output_path = PROJECT_ROOT / "composeApp/src/commonMain/kotlin/com/example/newoasa/data/TransitLineRepository.kt"
    output_path.parent.mkdir(parents=True, exist_ok=True)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(kotlin_code)
    
    print(f"\n✓ Generated: {output_path}")

if __name__ == "__main__":
    main()
