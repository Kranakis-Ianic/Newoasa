#!/usr/bin/env python3
"""
Transit Line Repository Generator

This script scans the GeoJSON folder structure and generates
Kotlin code for the TransitLineRepository.

Usage:
    python3 scripts/generate_transit_repository.py

Requirements:
    - Python 3.6+
    - No external dependencies required
"""

import os
import re
from pathlib import Path
from datetime import datetime
from typing import List, Dict, Tuple, Optional


class TransitLineData:
    """Represents a single transit line with its routes"""
    
    def __init__(self, line_number: str, category: str, route_files: List[str], is_flat: bool = False):
        self.line_number = line_number
        self.category = category
        self.route_files = sorted(route_files)
        self.is_flat = is_flat
        self.route_ids = self._extract_route_ids()
    
    def _extract_route_ids(self) -> List[str]:
        """Extract route IDs"""
        route_ids = []
        
        if self.is_flat:
            # For flat structure (Metro/Tram), we usually just have one route per file/line
            # We can use the line number or just "main" as the ID since there's typically one file
            # But based on the file naming (metro_line_1.geojson), the ID is implicitly the line itself.
            # Let's generate a simple ID "1" or similar if multiple files existed, but for now just "main" or the line number.
            # Actually, looking at the previous logic, it extracted IDs from "route_3494.geojson".
            # For "metro_line_1.geojson", the file IS the route. 
            # Let's use "main" as a default route ID for these single-file lines.
            return ["main"]
        else:
            # Nested structure: route_3494.geojson -> 3494
            pattern = re.compile(r'route_(\d+)\.geojson')
            for filename in self.route_files:
                match = pattern.match(filename)
                if match:
                    route_ids.append(match.group(1))
            
        return route_ids
    
    def to_kotlin_code(self) -> str:
        """Generate Kotlin code for this transit line"""
        route_ids_str = ', '.join(f'"{rid}"' for rid in self.route_ids)
        
        if self.is_flat:
            # Flat structure path: files/geojson/Metro/metro_line_1.geojson
            # category in Kotlin is lowercase ("metro"), but folder is "Metro"
            # We need to preserve the actual file path.
            # However, the Kotlin TransitLine class expects a "category" field.
            # We should probably pass the folder name or handle the path construction carefully.
            
            # Let's reconstruct the correct path based on the category directory mapping known by the generator
            # But here we only store 'category' string (e.g. "metro").
            # We need to know the source folder name.
            
            # To simplify, we'll store the relative path in the object or construct it here.
            # But wait, self.route_files contains just filenames.
            
            folder_name = "Metro" if self.category == "metro" else "Tram" if self.category == "tram" else self.category
            
            route_paths_str = ', '.join(
                f'"files/geojson/{folder_name}/{rf}"' 
                for rf in self.route_files
            )
        else:
            # Nested structure path: files/geojson/buses/022/route_123.geojson
            route_paths_str = ', '.join(
                f'"files/geojson/{self.category}/{self.line_number}/{rf}"' 
                for rf in self.route_files
            )
        
        return f'''        TransitLine(
            lineNumber = "{self.line_number}",
            category = "{self.category}",
            routeIds = listOf({route_ids_str}),
            routePaths = listOf({route_paths_str})
        )'''


class RepositoryGenerator:
    """Generates the TransitLineRepository.kt file"""
    
    def __init__(self, base_path: str = "."):
        self.base_path = Path(base_path)
        self.geojson_path = self.base_path / "composeApp" / "src" / "commonMain" / "composeResources" / "files" / "geojson"
        self.output_path = self.base_path / "composeApp" / "src" / "commonMain" / "kotlin" / "com" / "example" / "newoasa" / "data" / "TransitLineRepository.kt"
        self.transit_lines: List[TransitLineData] = []
    
    def scan_geojson_folders(self):
        """Scan the geojson folder structure and collect transit line data"""
        print("🔍 Scanning GeoJSON folders...")
        
        # Configuration for categories: (kotlin_category_name, folder_name, is_flat)
        category_config = [
            ("buses", "buses", False),
            ("trolleys", "trolleys", False),
            ("metro", "Metro", True),
            ("tram", "Tram", True)
        ]
        
        for category_name, folder_name, is_flat in category_config:
            category_path = self.geojson_path / folder_name
            
            if not category_path.exists():
                print(f"   ⚠️  Category directory not found: {category_path}")
                continue
            
            if is_flat:
                # Flat structure: Scan files directly in the category folder
                print(f"   Scanning flat directory: {folder_name}")
                
                # Regex to match files like "metro_line_1.geojson" or "tram_line_T6.geojson"
                # We capture the line number from the filename
                file_pattern = re.compile(rf'{category_name}_line_(\w+)\.geojson', re.IGNORECASE)
                
                found_files = []
                for f in category_path.iterdir():
                    if f.is_file() and f.name.endswith('.geojson'):
                        match = file_pattern.match(f.name)
                        if match:
                            line_number = match.group(1)
                            found_files.append((line_number, f.name))
                
                # Sort by line number (natural sort)
                def natural_sort_key(item):
                    s = item[0]
                    return [int(text) if text.isdigit() else text.lower()
                            for text in re.split('([0-9]+)', s)]
                            
                found_files.sort(key=natural_sort_key)
                print(f"   Found {len(found_files)} {category_name} lines")
                
                for line_number, filename in found_files:
                    transit_line = TransitLineData(line_number, category_name, [filename], is_flat=True)
                    self.transit_lines.append(transit_line)
                    
            else:
                # Nested structure: Scan subdirectories (one per line)
                # Get all line folders
                line_folders = [d for d in category_path.iterdir() if d.is_dir()]
                
                # Sort naturally
                def natural_sort_key_folder(f):
                    return [int(text) if text.isdigit() else text.lower()
                            for text in re.split('([0-9]+)', f.name)]
                            
                line_folders.sort(key=natural_sort_key_folder)
                
                print(f"   Found {len(line_folders)} {category_name} lines")
                
                for line_folder in line_folders:
                    line_number = line_folder.name
                    
                    # Get all .geojson files
                    route_files = [f.name for f in line_folder.iterdir() 
                                  if f.is_file() and f.name.endswith('.geojson')]
                    
                    if route_files:
                        transit_line = TransitLineData(line_number, category_name, route_files, is_flat=False)
                        self.transit_lines.append(transit_line)
    
    def generate_kotlin_code(self) -> str:
        """Generate the complete Kotlin repository code"""
        # Statistics
        total_lines = len(self.transit_lines)
        bus_lines = sum(1 for line in self.transit_lines if line.category == "buses")
        trolley_lines = sum(1 for line in self.transit_lines if line.category == "trolleys")
        metro_lines = sum(1 for line in self.transit_lines if line.category == "metro")
        tram_lines = sum(1 for line in self.transit_lines if line.category == "tram")
        total_routes = sum(len(line.route_ids) for line in self.transit_lines)
        
        # Generate transit line entries
        transit_lines_code = ',\n'.join(line.to_kotlin_code() for line in self.transit_lines)
        
        # Template
        kotlin_code = f'''package com.example.newoasa.data

/**
 * Auto-generated Transit Line Repository
 * Generated from GeoJSON folder structure
 * 
 * DO NOT EDIT MANUALLY!
 * Run scripts/generate_transit_repository.py to regenerate this file
 * 
 * Generated: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
 * Total Lines: {total_lines}
 * Total Routes: {total_routes}
 */

/**
 * Represents a transit line with its routes
 * @property lineNumber The line identifier (e.g., "022", "1", "309Β")
 * @property category The category of transit ("buses", "trolleys", "metro", "tram")
 * @property routeIds List of route IDs for this line
 * @property routePaths Resource paths to the GeoJSON files for each route
 */
data class TransitLine(
    val lineNumber: String,
    val category: String,
    val routeIds: List<String>,
    val routePaths: List<String>
) {{
    /**
     * Returns the display name for this line
     */
    val displayName: String
        get() = when (category) {{
            "buses" -> "Bus $lineNumber"
            "trolleys" -> "Trolley $lineNumber"
            "metro" -> "Metro Line $lineNumber"
            "tram" -> "Tram Line $lineNumber"
            else -> "Line $lineNumber"
        }}
    
    /**
     * Returns true if this is a bus line
     */
    val isBus: Boolean get() = category == "buses"
    
    /**
     * Returns true if this is a trolley line
     */
    val isTrolley: Boolean get() = category == "trolleys"

    /**
     * Returns true if this is a metro line
     */
    val isMetro: Boolean get() = category == "metro"

    /**
     * Returns true if this is a tram line
     */
    val isTram: Boolean get() = category == "tram"
    
    /**
     * Returns the base path to this line's geojson folder
     * Note: For Metro and Tram which use a flat structure, this points to the category folder
     */
    val basePath: String get() = when(category) {{
        "metro" -> "files/geojson/Metro"
        "tram" -> "files/geojson/Tram"
        else -> "files/geojson/$category/$lineNumber"
    }}
}}

/**
 * Repository providing access to all transit lines
 * This is a singleton object that holds all transit line data
 */
object TransitLineRepository {{
    private val lines: List<TransitLine> = listOf(
{transit_lines_code}
    )
    
    /**
     * Get all transit lines
     */
    fun getAllLines(): List<TransitLine> = lines
    
    /**
     * Get a specific line by its number
     * @param lineNumber The line number to search for
     * @return The TransitLine if found, null otherwise
     */
    fun getLineByNumber(lineNumber: String): TransitLine? {{
        return lines.find {{ it.lineNumber.equals(lineNumber, ignoreCase = true) }}
    }}
    
    /**
     * Get all lines in a specific category
     * @param category The category ("buses", "trolleys", "metro", "tram")
     * @return List of transit lines in that category
     */
    fun getLinesByCategory(category: String): List<TransitLine> {{
        return lines.filter {{ it.category.equals(category, ignoreCase = true) }}
    }}
    
    /**
     * Get all bus lines
     */
    fun getBusLines(): List<TransitLine> = getLinesByCategory("buses")
    
    /**
     * Get all trolley lines
     */
    fun getTrolleyLines(): List<TransitLine> = getLinesByCategory("trolleys")

    /**
     * Get all metro lines
     */
    fun getMetroLines(): List<TransitLine> = getLinesByCategory("metro")

    /**
     * Get all tram lines
     */
    fun getTramLines(): List<TransitLine> = getLinesByCategory("tram")
    
    /**
     * Search for lines matching a query
     * @param query The search query
     * @return List of matching transit lines
     */
    fun searchLines(query: String): List<TransitLine> {{
        return lines.filter {{ it.lineNumber.contains(query, ignoreCase = true) }}
    }}
    
    /**
     * Get statistics about the repository
     */
    fun getStats(): RepositoryStats {{
        return RepositoryStats(
            totalLines = lines.size,
            totalBusLines = getBusLines().size,
            totalTrolleyLines = getTrolleyLines().size,
            totalMetroLines = getMetroLines().size,
            totalTramLines = getTramLines().size,
            totalRoutes = lines.sumOf {{ it.routeIds.size }}
        )
    }}
}}

/**
 * Statistics about the transit line repository
 */
data class RepositoryStats(
    val totalLines: Int,
    val totalBusLines: Int,
    val totalTrolleyLines: Int,
    val totalMetroLines: Int,
    val totalTramLines: Int,
    val totalRoutes: Int
)
'''
        return kotlin_code
    
    def write_output(self, kotlin_code: str):
        """Write the generated code to the output file"""
        # Create directory if it doesn't exist
        self.output_path.parent.mkdir(parents=True, exist_ok=True)
        
        # Write the file
        with open(self.output_path, 'w', encoding='utf-8') as f:
            f.write(kotlin_code)
    
    def generate(self):
        """Main generation process"""
        print("="*60)
        print("Transit Line Repository Generator")
        print("="*60)
        print()
        
        # Scan folders
        self.scan_geojson_folders()
        
        if not self.transit_lines:
            print("\\n❌ No transit lines found!")
            return
        
        # Generate code
        print("\\n📝 Generating Kotlin code...")
        kotlin_code = self.generate_kotlin_code()
        
        # Write output
        print(f"\\n💾 Writing to: {self.output_path}")
        self.write_output(kotlin_code)
        
        # Statistics
        total_lines = len(self.transit_lines)
        bus_lines = sum(1 for line in self.transit_lines if line.category == "buses")
        trolley_lines = sum(1 for line in self.transit_lines if line.category == "trolleys")
        metro_lines = sum(1 for line in self.transit_lines if line.category == "metro")
        tram_lines = sum(1 for line in self.transit_lines if line.category == "tram")
        total_routes = sum(len(line.route_ids) for line in self.transit_lines)
        
        print("\\n✅ Successfully generated TransitLineRepository!")
        print(f"   Total lines: {total_lines}")
        print(f"   Bus lines: {bus_lines}")
        print(f"   Trolley lines: {trolley_lines}")
        print(f"   Metro lines: {metro_lines}")
        print(f"   Tram lines: {tram_lines}")
        print(f"   Total routes: {total_routes}")
        print()
        print("="*60)


def main():
    """Entry point"""
    try:
        # Determine base path (run from project root)
        script_dir = Path(__file__).parent
        project_root = script_dir.parent
        
        generator = RepositoryGenerator(base_path=project_root)
        generator.generate()
        
    except Exception as e:
        print(f"\\n❌ Error: {e}")
        import traceback
        traceback.print_exc()
        exit(1)


if __name__ == "__main__":
    main()
