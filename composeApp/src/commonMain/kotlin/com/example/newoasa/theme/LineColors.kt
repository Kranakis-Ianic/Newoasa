package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color
import com.example.newoasa.utils.LineInfoLoader
import kotlinx.coroutines.runBlocking

/**
 * Line colors for Athens public transport
 * Dynamically loaded from line_info.json
 */
object LineColors {
    // Cached line info
    private var lineInfoCache: Map<String, LineInfoLoader.LineColorInfo>? = null
    
    /**
     * Load line info (cached after first call)
     */
    private fun getLineInfo(): Map<String, LineInfoLoader.LineColorInfo> {
        return lineInfoCache ?: runBlocking {
            LineInfoLoader.loadLineInfo().also { lineInfoCache = it }
        }
    }
    
    /**
     * Get color by line code/number
     * Looks up color from line_info.json
     */
    fun getColorForLine(lineCode: String): Color {
        val info = getLineInfo()
        
        // Try to find color for this line reference
        val colorHex = info[lineCode]?.color 
            ?: info[normalizeLineRef(lineCode)]?.color
            ?: "#009EC6" // Default blue
        
        return parseHexColor(colorHex)
    }
    
    /**
     * Get hex color string by line code
     */
    fun getHexColorForLine(lineCode: String): String {
        val info = getLineInfo()
        
        return info[lineCode]?.color 
            ?: info[normalizeLineRef(lineCode)]?.color
            ?: "#009EC6"
    }
    
    /**
     * Get color by transport category
     */
    fun getColorForCategory(category: String, isBus: Boolean = false): Color {
        return when (category.lowercase()) {
            "metro" -> getColorForLine("M1")
            "tram" -> getColorForLine("T6")
            "trolleys" -> parseHexColor("#F27C02")
            "suburban", "proastiakos" -> getColorForLine("A1")
            "buses" -> parseHexColor("#009EC6")
            else -> parseHexColor("#009EC6")
        }
    }
    
    /**
     * Get hex color string by category
     */
    fun getHexColorForCategory(category: String, isBus: Boolean = false): String {
        return when (category.lowercase()) {
            "metro" -> getHexColorForLine("M1")
            "tram" -> getHexColorForLine("T6")
            "trolleys" -> "#F27C02"
            "suburban", "proastiakos" -> getHexColorForLine("A1")
            "buses" -> "#009EC6"
            else -> "#009EC6"
        }
    }
    
    /**
     * Normalize line reference for lookup
     * Converts "Μ1" -> "M1", "1" -> "M1", etc.
     */
    private fun normalizeLineRef(lineRef: String): String {
        val normalized = lineRef.trim()
        
        // Replace Greek Mu with Latin M
        val withLatinM = normalized.replace("Μ", "M")
        
        // If already in standard format (M1, T6, A1, etc.), return it
        if (withLatinM.matches(Regex("^[MTA]\\d+$"))) {
            return withLatinM
        }
        
        // If it's just a number, try to infer the prefix
        if (withLatinM.matches(Regex("^\\d+$"))) {
            val num = withLatinM.toIntOrNull()
            return when (num) {
                1, 2, 3, 4 -> "M$withLatinM" // Metro lines
                6, 7 -> "T$withLatinM" // Tram lines
                else -> withLatinM
            }
        }
        
        return withLatinM
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
}
