package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color
import com.example.newoasa.utils.LineInfoLoader
import kotlinx.coroutines.runBlocking

/**
 * Line colors for Athens public transport
 * Reads colors from GeoJSON files via LineInfoLoader with fallback to OASA standards
 */
object LineColors {
    private val colorCache = mutableMapOf<String, String>()
    
    /**
     * Get color by line code/number
     * First tries to read from GeoJSON via LineInfoLoader, then falls back to hardcoded colors
     */
    fun getColorForLine(lineCode: String): Color {
        return parseHexColor(getHexColorForLine(lineCode))
    }
    
    /**
     * Get hex color string by line code
     * Attempts to read from GeoJSON files via LineInfoLoader first
     */
    fun getHexColorForLine(lineCode: String): String {
        val normalized = normalizeLineRef(lineCode)
        
        // Check cache first
        colorCache[normalized]?.let { return it }
        
        // Try to get color from GeoJSON via LineInfoLoader
        val colorFromGeoJson = tryGetColorFromGeoJson(normalized)
        if (colorFromGeoJson != null) {
            colorCache[normalized] = colorFromGeoJson
            return colorFromGeoJson
        }
        
        // Fallback to hardcoded colors
        val hardcodedColor = getHardcodedColor(normalized)
        colorCache[normalized] = hardcodedColor
        return hardcodedColor
    }
    
    /**
     * Try to extract color from GeoJSON files using LineInfoLoader
     */
    private fun tryGetColorFromGeoJson(lineRef: String): String? {
        return try {
            runBlocking {
                LineInfoLoader.getColorForLine(lineRef)
            }
        } catch (e: Exception) {
            println("Error getting color from GeoJSON for $lineRef: ${e.message}")
            null
        }
    }
    
    /**
     * Get hardcoded color as fallback
     */
    private fun getHardcodedColor(normalized: String): String {
        // Metro lines
        return when {
            normalized.startsWith("M1") || normalized == "1" -> "#00A651" // Green Line
            normalized.startsWith("M2") || normalized == "2" -> "#ED1C24" // Red Line  
            normalized.startsWith("M3") || normalized == "3" -> "#0066B3" // Blue Line
            normalized.startsWith("M4") || normalized == "4" -> "#FFD200" // Yellow Line (future)
            
            // Tram lines
            normalized.startsWith("T6") || normalized == "6" -> "#FFA500" // Orange
            normalized.startsWith("T7") || normalized == "7" -> "#FFA500" // Orange
            
            // Trolley lines
            normalized.matches(Regex("^0?2[0-5]$")) -> "#F27C02" // Trolley lines
            normalized.matches(Regex("^[1-9]$|^1[0-9]$|^2[0-1]$")) -> "#F27C02" // Trolley 1-21
            
            // Suburban Railway (Proastiakos)
            normalized.startsWith("A") -> "#009640" // Green for suburban
            
            // Express bus lines (X prefix)
            normalized.startsWith("X") || normalized.startsWith("Χ") -> "#E2231A" // Red for express
            
            // Airport buses (Α, Β prefix)
            normalized.matches(Regex("^[ΑA]\\d+$")) -> "#009EC6" // Blue for airport
            normalized.matches(Regex("^[ΒB]\\d+$")) -> "#009EC6" // Blue for airport
            
            // Special buses (Ε, Γ prefix)
            normalized.matches(Regex("^[ΕE]\\d+$")) -> "#8B4789" // Purple for special
            normalized.matches(Regex("^[ΓΓ]\\d+$")) -> "#8B4789" // Purple for special
            
            // Regular bus lines (default)
            else -> "#009EC6" // Standard blue for buses
        }
    }
    
    /**
     * Get color by transport category
     */
    fun getColorForCategory(category: String, isBus: Boolean = false): Color {
        return when (category.lowercase()) {
            "metro" -> parseHexColor("#00A651") // Metro green (line 1 representative)
            "tram" -> parseHexColor("#FFA500") // Tram orange
            "trolleys", "trolley" -> parseHexColor("#F27C02") // Trolley orange-red
            "suburban", "proastiakos" -> parseHexColor("#009640") // Suburban green
            "buses", "bus" -> parseHexColor("#009EC6") // Bus blue
            else -> parseHexColor("#009EC6") // Default blue
        }
    }
    
    /**
     * Get hex color string by category
     */
    fun getHexColorForCategory(category: String, isBus: Boolean = false): String {
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
     * Converts "Μ1" -> "M1", etc.
     */
    private fun normalizeLineRef(lineRef: String): String {
        val normalized = lineRef.trim()
        
        // Replace Greek characters with Latin equivalents
        val withLatin = normalized
            .replace("Μ", "M")
            .replace("Χ", "X")
            .replace("Α", "A")
            .replace("Β", "B")
            .replace("Γ", "G")
            .replace("Ε", "E")
            .replace("Τ", "T")
        
        return withLatin
    }
    
    /**
     * Parse a hex color string to a Compose Color
     * Supports formats: #RRGGBB, #AARRGGBB
     */
    private fun parseHexColor(colorString: String): Color {
        return try {
            val hex = colorString.removePrefix("#")
            
            when (hex.length) {
                6 -> {
                    // #RRGGBB -> #FFRRGGBB
                    Color((0xFF000000L or hex.toLong(16)))
                }
                8 -> {
                    // #AARRGGBB
                    Color(hex.toLong(16))
                }
                else -> Color(0xFF009EC6) // Default blue
            }
        } catch (e: Exception) {
            Color(0xFF009EC6) // Error parsing, use default blue
        }
    }
    
    /**
     * Clear the color cache (useful for testing or reloading)
     */
    fun clearCache() {
        colorCache.clear()
        LineInfoLoader.clearCache()
    }
}
