package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import com.example.newoasa.data.TransitLineRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * Utility object to provide line information and extract colors from GeoJSON files
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
     * Extract color from a GeoJSON file
     */
    @OptIn(ExperimentalResourceApi::class)
    private suspend fun extractColorFromGeoJson(geojsonPath: String): String? {
        // Check cache first
        colorCache[geojsonPath]?.let { return it }
        
        return try {
            // Read the GeoJSON file
            val fileContent = resource(geojsonPath).readBytes().decodeToString()
            
            // Parse JSON
            val jsonObject = json.parseToJsonElement(fileContent).jsonObject
            
            // Get features array
            val features = jsonObject["features"]?.jsonArray
            
            // Get the first feature's properties
            val firstFeature = features?.firstOrNull()?.jsonObject
            val properties = firstFeature?.get("properties")?.jsonObject
            
            // Extract color from properties
            val color = properties?.get("colour")?.jsonPrimitive?.content
                ?: properties?.get("color")?.jsonPrimitive?.content
                ?: properties?.get("stroke")?.jsonPrimitive?.content
            
            // Cache the color if found
            color?.let { colorCache[geojsonPath] = it }
            
            color
        } catch (e: Exception) {
            println("Error extracting color from GeoJSON $geojsonPath: ${e.message}")
            null
        }
    }
    
    /**
     * Get color for a specific line reference from GeoJSON files
     */
    suspend fun getColorForLine(lineRef: String): String? {
        // Check cache first
        val normalized = normalizeLineRef(lineRef)
        colorCache[normalized]?.let { return it }
        
        return try {
            // Find the line in TransitLineRepository
            val allLines = TransitLineRepository.getAllLines()
            val matchingLine = allLines.find { line ->
                val normalizedLineNumber = normalizeLineRef(line.lineNumber)
                normalizedLineNumber == normalized || line.lineNumber == lineRef
            }
            
            if (matchingLine != null && matchingLine.routePaths.isNotEmpty()) {
                // Get the first route path and extract color
                val firstRoutePath = matchingLine.routePaths.first()
                val color = extractColorFromGeoJson(firstRoutePath)
                
                // Cache the color
                color?.let { colorCache[normalized] = it }
                
                color
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error getting color from GeoJSON for $lineRef: ${e.message}")
            null
        }
    }
    
    /**
     * Load line information
     * Generates info from available lines with colors from GeoJSON
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
     * Convert a Station to StationInfo with full line information including colors from GeoJSON
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
