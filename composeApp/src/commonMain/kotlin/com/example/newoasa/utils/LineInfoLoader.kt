package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import com.example.newoasa.theme.LineColors

/**
 * Utility object to provide line information
 * Updated to use LineColors instead of loading from JSON
 */
object LineInfoLoader {
    private var lineInfoCache: Map<String, LineColorInfo>? = null
    
    data class LineColorInfo(
        val ref: String,
        val name: String,
        val nameEn: String?,
        val color: String,
        val route: String
    )
    
    /**
     * Load line information
     * Now generates info from LineColors instead of JSON
     */
    suspend fun loadLineInfo(): Map<String, LineColorInfo> {
        // Return cached data if already loaded
        lineInfoCache?.let { return it }
        
        // Generate line info map from LineColors
        val infoMap = mutableMapOf<String, LineColorInfo>()
        
        // Metro lines
        listOf("M1", "M2", "M3", "M4", "1", "2", "3", "4").forEach { ref ->
            val color = LineColors.getHexColorForLine(ref)
            val name = when (ref.replace("M", "")) {
                "1" -> "Line 1 (Green)"
                "2" -> "Line 2 (Red)"
                "3" -> "Line 3 (Blue)"
                "4" -> "Line 4 (Yellow)"
                else -> "Metro $ref"
            }
            infoMap[ref] = LineColorInfo(ref, name, name, color, "metro")
        }
        
        // Tram lines
        listOf("T6", "T7", "6", "7").forEach { ref ->
            val color = LineColors.getHexColorForLine(ref)
            val name = "Tram ${ref.replace("T", "")}"
            infoMap[ref] = LineColorInfo(ref, name, name, color, "tram")
        }
        
        // Trolley lines (1-25)
        (1..25).forEach { num ->
            val ref = num.toString()
            val paddedRef = num.toString().padStart(3, '0')
            val color = LineColors.getHexColorForLine(ref)
            val name = "Trolley $num"
            infoMap[ref] = LineColorInfo(ref, name, name, color, "trolley")
            if (paddedRef != ref) {
                infoMap[paddedRef] = LineColorInfo(paddedRef, name, name, color, "trolley")
            }
        }
        
        // Express buses
        listOf("X93", "X95", "X96", "X97", "X14", "Χ93", "Χ95", "Χ96", "Χ97", "Χ14").forEach { ref ->
            val color = LineColors.getHexColorForLine(ref)
            val name = "Express $ref"
            infoMap[ref] = LineColorInfo(ref, name, name, color, "bus")
        }
        
        // Airport buses
        listOf("A1", "A2", "A3", "A5", "A7", "A8", "A10", "A11", "A13", "A15",
               "B1", "B2", "B5", "B9", "B10", "B11", "B12", "B15").forEach { ref ->
            val color = LineColors.getHexColorForLine(ref)
            val name = "Airport $ref"
            infoMap[ref] = LineColorInfo(ref, name, name, color, "bus")
        }
        
        lineInfoCache = infoMap
        return infoMap
    }
    
    /**
     * Get color for a specific line reference
     */
    suspend fun getColorForLine(lineRef: String): String? {
        return LineColors.getHexColorForLine(lineRef)
    }
    
    /**
     * Convert a Station to StationInfo with full line information including colors
     */
    suspend fun toStationInfo(station: Station): StationInfo {
        val lineInfoList = station.lines.map { lineRef ->
            val color = LineColors.getHexColorForLine(lineRef)
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
    private fun generateLineName(lineRef: String): String {
        return when {
            lineRef.matches(Regex("^M?[1-4]$")) -> "Metro Line ${lineRef.replace("M", "")}"
            lineRef.matches(Regex("^T?[67]$")) -> "Tram ${lineRef.replace("T", "")}"
            lineRef.matches(Regex("^0?[1-9]$|^[12][0-5]$")) -> "Trolley $lineRef"
            lineRef.matches(Regex("^[XΧ]\\d+$")) -> "Express $lineRef"
            lineRef.matches(Regex("^[ABΑΒ]\\d+$")) -> "Airport $lineRef"
            else -> "Bus $lineRef"
        }
    }
}
