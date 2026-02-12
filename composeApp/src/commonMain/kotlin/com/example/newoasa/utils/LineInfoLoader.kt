package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import com.example.newoasa.data.TransitLineRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility object to provide line information and extract colors from GeoJSON files
 * Note: GeoJSON color extraction is disabled for now due to resource loading complexity
 * Falls back to default colors based on line categories
 */
object LineInfoLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private val colorCache = mutableMapOf<String, String>()
    private var lineInfoCache: Map<String, LineColorInfo>? = null
    
    data class LineColorInfo(
        val ref: String,
        val name: String,
        val nameEn: String?,
        val color: String,
        val route: String
    )
    
    /**
     * Get color for a specific line reference
     * Currently returns default colors based on line type
     * TODO: Implement GeoJSON color extraction when resource loading is set up
     */
    suspend fun getColorForLine(lineRef: String): String? {
        // Check cache first
        val normalized = normalizeLineRef(lineRef)
        colorCache[normalized]?.let { return it }
        
        return try {
            // Find the line in TransitLineRepository to get its category
            val allLines = TransitLineRepository.getAllLines()
            val matchingLine = allLines.find { line ->
                val normalizedLineNumber = normalizeLineRef(line.lineNumber)
                normalizedLineNumber == normalized || line.lineNumber == lineRef
            }
            
            val color = if (matchingLine != null) {
                // Use default color based on category for now
                getDefaultColorForCategory(matchingLine.category)
            } else {
                // Fallback to inferring from line ref
                getColorFromLineRef(normalized)
            }
            
            // Cache the color
            colorCache[normalized] = color
            color
        } catch (e: Exception) {
            println("Error getting color for $lineRef: ${e.message}")
            null
        }
    }
    
    /**
     * Load line information
     * Generates info from available lines with colors
     */
    suspend fun loadLineInfo(): Map<String, LineColorInfo> {
        // Return cached data if already loaded
        lineInfoCache?.let { return it }
        
        // Generate line info map from TransitLineRepository
        val infoMap = mutableMapOf<String, LineColorInfo>()
        
        try {
            val allLines = TransitLineRepository.getAllLines()
            
            for (line in allLines) {
                val lineRef = line.lineNumber
                val color = getColorForLine(lineRef) ?: getDefaultColorForCategory(line.category)
                val name = generateLineName(lineRef, line.category)
                
                infoMap[lineRef] = LineColorInfo(
                    ref = lineRef,
                    name = name,
                    nameEn = name,
                    color = color,
                    route = line.category
                )
            }
        } catch (e: Exception) {
            println("Error loading line info: ${e.message}")
        }
        
        lineInfoCache = infoMap
        return infoMap
    }
    
    /**
     * Convert a Station to StationInfo with full line information including colors
     */
    suspend fun toStationInfo(station: Station): StationInfo {
        val lineInfoList = station.lines.map { lineRef ->
            val color = getColorForLine(lineRef) ?: "#009EC6"
            LineInfo(
                ref = lineRef,
                colour = color,
                name = generateLineName(lineRef),
                route = null
            )
        }
        
        return StationInfo(
            name = station.name,
            nameEn = station.nameEn,
            latitude = station.latitude,
            longitude = station.longitude,
            lines = lineInfoList,
            wheelchair = station.wheelchair
        )
    }
    
    /**
     * Generate a display name for a line reference
     */
    private fun generateLineName(lineRef: String, category: String? = null): String {
        return when {
            lineRef.matches(Regex("^M?[1-4]$")) -> "Metro Line ${lineRef.replace("M", "")}"
            lineRef.matches(Regex("^T?[67]$")) -> "Tram ${lineRef.replace("T", "")}"
            lineRef.matches(Regex("^0?[1-9]$|^[12][0-5]$")) -> "Trolley $lineRef"
            lineRef.matches(Regex("^[XΧ]\\d+$")) -> "Express $lineRef"
            lineRef.matches(Regex("^[ABΑΒ]\\d+$")) -> "Airport $lineRef"
            category != null -> "${category.capitalize()} $lineRef"
            else -> "Line $lineRef"
        }
    }
    
    /**
     * Get default color for a category (fallback)
     */
    private fun getDefaultColorForCategory(category: String): String {
        return when (category.lowercase()) {
            "metro" -> "#00A651"
            "tram" -> "#FFA500"
            "trolleys", "trolley" -> "#F27C02"
            "suburban", "proastiakos" -> "#009640"
            "buses", "bus" -> "#009EC6"
            else -> "#009EC6"
        }
    }
    
    /**
     * Get color based on line reference pattern
     */
    private fun getColorFromLineRef(normalized: String): String {
        return when {
            normalized.startsWith("M1") || normalized == "1" -> "#00A651" // Green Line
            normalized.startsWith("M2") || normalized == "2" -> "#ED1C24" // Red Line  
            normalized.startsWith("M3") || normalized == "3" -> "#0066B3" // Blue Line
            normalized.startsWith("M4") || normalized == "4" -> "#FFD200" // Yellow Line
            normalized.startsWith("T6") || normalized == "6" -> "#FFA500" // Tram
            normalized.startsWith("T7") || normalized == "7" -> "#FFA500" // Tram
            normalized.matches(Regex("^0?[1-9]$|^1[0-9]$|^2[0-5]$")) -> "#F27C02" // Trolley
            normalized.startsWith("X") || normalized.startsWith("Χ") -> "#E2231A" // Express
            normalized.matches(Regex("^[AΑ]\\d+$")) -> "#009EC6" // Airport
            normalized.matches(Regex("^[BΒ]\\d+$")) -> "#009EC6" // Airport
            else -> "#009EC6" // Default bus blue
        }
    }
    
    /**
     * Normalize line reference for lookup
     */
    private fun normalizeLineRef(lineRef: String): String {
        val normalized = lineRef.trim()
        
        // Replace Greek characters with Latin equivalents
        return normalized
            .replace("Μ", "M")
            .replace("Χ", "X")
            .replace("Α", "A")
            .replace("Β", "B")
            .replace("Γ", "G")
            .replace("Ε", "E")
            .replace("Τ", "T")
    }
    
    /**
     * Clear all caches
     */
    fun clearCache() {
        colorCache.clear()
        lineInfoCache = null
    }
}
