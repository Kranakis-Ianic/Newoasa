package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import com.example.newoasa.theme.LineColors

/**
 * Utility to load line information and convert station data
 */
object LineInfoLoader {
    
    data class LineColorInfo(
        val ref: String,
        val name: String,
        val nameEn: String?,
        val color: String,
        val route: String
    )
    
    /**
     * Get color for a line (delegates to LineColors)
     */
    suspend fun getColorForLine(lineRef: String): String {
        return LineColors.getHexColorForLine(lineRef)
    }
    
    /**
     * Load line information (placeholder for future use)
     */
    suspend fun loadLineInfo(): Map<String, LineColorInfo> {
        return emptyMap()
    }
    
    /**
     * Convert Station to StationInfo with line colors
     */
    suspend fun toStationInfo(station: Station): StationInfo {
        val lineInfoList = station.lines.map { lineRef ->
            LineInfo(
                ref = lineRef,
                colour = LineColors.getHexColorForLine(lineRef),
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
     * Generate display name for a line
     */
    private fun generateLineName(lineRef: String): String {
        val normalized = lineRef.trim()
            .replace("Μ", "M").replace("Χ", "X")
            .replace("Α", "A").replace("Β", "B")
        
        return when {
            normalized.matches(Regex("^M?[1-4]$")) -> "Metro ${normalized.replace("M", "")}"
            normalized.matches(Regex("^T?[67]$")) -> "Tram ${normalized.replace("T", "")}"
            normalized.matches(Regex("^0?[1-9]$|^[12][0-5]$")) -> "Trolley $normalized"
            normalized.matches(Regex("^[XΧ]\\d+$")) -> "Express $normalized"
            normalized.matches(Regex("^[ABΑΒ]\\d+$")) -> "Bus $normalized"
            else -> "Line $normalized"
        }
    }
    
    /**
     * Clear caches (no-op for now)
     */
    fun clearCache() {}
}
