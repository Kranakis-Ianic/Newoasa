package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color

/**
 * Line colors for Athens public transport based on OASA standards
 */
object LineColors {
    
    fun getColorForLine(lineCode: String): Color = parseHexColor(getHexColorForLine(lineCode))
    
    fun getHexColorForLine(lineCode: String): String {
        val normalized = lineCode.trim()
            .replace("Μ", "M").replace("Χ", "X")
            .replace("Α", "A").replace("Β", "B")
            .replace("Γ", "G").replace("Ε", "E")
            .replace("Τ", "T")
        
        return when {
            // Metro lines
            normalized.matches(Regex("^M?1$")) -> "#00A651" // Green
            normalized.matches(Regex("^M?2$")) -> "#ED1C24" // Red
            normalized.matches(Regex("^M?3$")) -> "#0066B3" // Blue
            normalized.matches(Regex("^M?4$")) -> "#FFD200" // Yellow
            
            // Tram lines
            normalized.matches(Regex("^T?[67]$")) -> "#FFA500" // Orange
            
            // Trolley lines (1-25)
            normalized.matches(Regex("^0?[1-9]$|^1[0-9]$|^2[0-5]$")) -> "#F27C02" // Orange-red
            
            // Express buses
            normalized.matches(Regex("^[XΧ]\\d+$")) -> "#E2231A" // Red
            
            // Airport/Special buses
            normalized.matches(Regex("^[ABΑΒΕΓ]\\d+$")) -> "#009EC6" // Blue
            
            // Default bus color
            else -> "#009EC6" // Blue
        }
    }
    
    fun getColorForCategory(category: String): Color {
        return parseHexColor(when (category.lowercase()) {
            "metro" -> "#00A651"
            "tram" -> "#FFA500"
            "trolleys", "trolley" -> "#F27C02"
            "suburban", "proastiakos" -> "#009640"
            else -> "#009EC6" // buses and default
        })
    }
    
    private fun parseHexColor(colorString: String): Color {
        return try {
            val hex = colorString.removePrefix("#")
            Color(if (hex.length == 6) 0xFF000000L or hex.toLong(16) else hex.toLong(16))
        } catch (e: Exception) {
            Color(0xFF009EC6) // Default blue on error
        }
    }
}
