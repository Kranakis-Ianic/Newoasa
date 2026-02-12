package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import newoasa.composeapp.generated.resources.Res

/**
 * Utility object to load and cache line information from line_info.json
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
     * Load line information from line_info.json resource file
     */
    suspend fun loadLineInfo(): Map<String, LineColorInfo> {
        // Return cached data if already loaded
        lineInfoCache?.let { return it }
        
        try {
            val bytes = Res.readBytes("files/line_info.json")
            val jsonString = bytes.decodeToString()
            
            val json = Json.parseToJsonElement(jsonString).jsonObject
            val lineColors = json["lineColors"]?.jsonObject ?: throw Exception("lineColors not found")
            
            val infoMap = mutableMapOf<String, LineColorInfo>()
            
            // Process each category (metro, tram, suburban, etc.)
            lineColors.forEach { (category, linesObj) ->
                val lines = linesObj.jsonObject
                lines.forEach { (lineKey, lineDataObj) ->
                    val lineData = lineDataObj.jsonObject
                    
                    val ref = lineData["ref"]?.jsonPrimitive?.content ?: ""
                    val name = lineData["name"]?.jsonPrimitive?.content ?: ""
                    val nameEn = lineData["nameEn"]?.jsonPrimitive?.content
                    val color = lineData["color"]?.jsonPrimitive?.content ?: "#666666"
                    val route = lineData["route"]?.jsonPrimitive?.content ?: ""
                    
                    if (ref.isNotEmpty()) {
                        infoMap[ref] = LineColorInfo(ref, name, nameEn, color, route)
                        
                        // Also add entry for the key (like "M1", "T6") if different from ref
                        if (lineKey != ref) {
                            infoMap[lineKey] = LineColorInfo(ref, name, nameEn, color, route)
                        }
                    }
                }
            }
            
            lineInfoCache = infoMap
            return infoMap
        } catch (e: Exception) {
            println("Error loading line_info.json: ${e.message}")
            e.printStackTrace()
            return emptyMap()
        }
    }
    
    /**
     * Get color for a specific line reference
     */
    suspend fun getColorForLine(lineRef: String): String? {
        val info = loadLineInfo()
        return info[lineRef]?.color
    }
    
    /**
     * Convert a Station to StationInfo with full line information including colors
     */
    suspend fun toStationInfo(station: Station): StationInfo {
        val lineInfoMap = loadLineInfo()
        
        val lineInfoList = station.lines.mapNotNull { lineRef ->
            val info = lineInfoMap[lineRef]
            if (info != null) {
                LineInfo(
                    ref = lineRef,
                    colour = info.color,
                    name = info.nameEn ?: info.name,
                    route = info.route
                )
            } else {
                // Fallback if line info not found
                LineInfo(
                    ref = lineRef,
                    colour = "#666666", // Gray as fallback
                    name = lineRef,
                    route = null
                )
            }
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
}
