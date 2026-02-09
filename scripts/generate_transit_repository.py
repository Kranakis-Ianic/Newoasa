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
from typing import List, Dict, Tuple


class TransitLineData:
    """Represents a single transit line with its routes"""
    
    def __init__(self, line_number: str, category: str, route_files: List[str]):
        self.line_number = line_number
        self.category = category
        self.route_files = sorted(route_files)
        self.route_ids = self._extract_route_ids()
    
    def _extract_route_ids(self) -> List[str]:
        """Extract route IDs from filenames (e.g., route_3494.geojson -> 3494)"""
        route_ids = []
        pattern = re.compile(r'route_(\d+)\.geojson')
        
        for filename in self.route_files:
            match = pattern.match(filename)
            if match:
                route_ids.append(match.group(1))
        
        return route_ids
    
    def to_kotlin_code(self) -> str:
        """Generate Kotlin code for this transit line"""
        route_ids_str = ', '.join(f'"{rid}"' for rid in self.route_ids)
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
        
        categories = ["buses", "trolleys"]
        
        for category in categories:
            category_path = self.geojson_path / category
            
            if not category_path.exists():
                print(f"   ⚠️  Category directory not found: {category_path}")
                continue
            
            # Get all line folders
            line_folders = [d for d in category_path.iterdir() if d.is_dir()]
            line_folders.sort(key=lambda x: x.name)
            
            print(f"   Found {len(line_folders)} {category} lines")
            
            for line_folder in line_folders:
                line_number = line_folder.name
                
                # Get all .geojson files
                route_files = [f.name for f in line_folder.iterdir() 
                              if f.is_file() and f.name.endswith('.geojson')]
                
                if route_files:
                    transit_line = TransitLineData(line_number, category, route_files)
                    self.transit_lines.append(transit_line)
    
    def generate_kotlin_code(self) -> str:
        """Generate the complete Kotlin repository code"""
        # Statistics
        total_lines = len(self.transit_lines)
        bus_lines = sum(1 for line in self.transit_lines if line.category == "buses")
        trolley_lines = sum(1 for line in self.transit_lines if line.category == "trolleys")
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
 * @property category The category of transit ("buses" or "trolleys")
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
     * Returns the base path to this line's geojson folder
     */
    val basePath: String get() = "files/geojson/$category/$lineNumber"
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
     * @param category The category ("buses" or "trolleys")
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
        total_routes = sum(len(line.route_ids) for line in self.transit_lines)
        
        print("\\n✅ Successfully generated TransitLineRepository!")
        print(f"   Total lines: {total_lines}")
        print(f"   Bus lines: {bus_lines}")
        print(f"   Trolley lines: {trolley_lines}")
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
