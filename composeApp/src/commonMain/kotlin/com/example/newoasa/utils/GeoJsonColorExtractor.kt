package com.example.newoasa.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * Utility to extract color information from GeoJSON files
 */
object GeoJsonColorExtractor {
    private val json = Json { ignoreUnknownKeys = true }
    private val colorCache = mutableMapOf<String, String>()
    
    /**
     * Extract color from a GeoJSON file path
     * @param geojsonPath The path to the GeoJSON file (e.g., "files/geojson/Metro lines/1/lines_metro_1_ Kifissia → Piraeus.geojson")
     * @return The hex color string from the GeoJSON properties, or null if not found
     */
    @OptIn(ExperimentalResourceApi::class)
    suspend fun extractColorFromGeoJson(geojsonPath: String): String? {
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
     * Extract color for a specific line by finding its GeoJSON file
     * @param lineNumber The line number (e.g., "1", "M1", "022")
     * @param category The category ("metro", "buses", "trolleys", "tram")
     * @return The hex color string, or null if not found
     */
    @OptIn(ExperimentalResourceApi::class)
    suspend fun getColorForLine(lineNumber: String, category: String): String? {
        // Normalize line number
        val normalizedLine = lineNumber.replace("M", "").replace("Μ", "").trim()
        
        // Determine the folder name based on category
        val folderName = when (category.lowercase()) {
            "metro" -> "Metro lines"
            "buses", "bus" -> "Bus lines"
            "trolleys", "trolley" -> "Trolley lines"
            "tram" -> "Tram lines"
            "suburban" -> "Suburban Railway lines"
            else -> return null
        }
        
        // Try to find and read a GeoJSON file for this line
        // Note: This is a simplified approach - you may need to adjust based on your file structure
        val basePath = "files/geojson/$folderName/$normalizedLine"
        
        return try {
            // Try to list and read the first lines_*.geojson file
            // This is a simplified approach - in production, you'd enumerate files properly
            val testPaths = listOf(
                "$basePath/lines_${category.lowercase()}_${normalizedLine}_.geojson",
                "$basePath/lines_${category.lowercase()}_${normalizedLine}.geojson",
                "$basePath/${category.lowercase()}_${normalizedLine}.geojson"
            )
            
            for (path in testPaths) {
                try {
                    val color = extractColorFromGeoJson(path)
                    if (color != null) return color
                } catch (e: Exception) {
                    // Try next path
                    continue
                }
            }
            
            null
        } catch (e: Exception) {
            println("Error finding GeoJSON for line $lineNumber: ${e.message}")
            null
        }
    }
    
    /**
     * Clear the color cache
     */
    fun clearCache() {
        colorCache.clear()
    }
}
