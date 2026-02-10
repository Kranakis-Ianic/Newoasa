package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color

/**
 * Official line colors for Athens public transport
 * Based on: 
 * - Athens Metro: https://en.wikipedia.org/wiki/Athens_Metro
 * - Athens Tram: https://en.wikipedia.org/wiki/Athens_Tram
 * - Athens Suburban Railway: https://en.wikipedia.org/wiki/Athens_Suburban_Railway
 */
object LineColors {
    // ===== ATHENS METRO =====
    // Line 1 (Green Line) - Piraeus to Kifissia
    val Metro1 = Color(0xFF00A651) // Official green
    
    // Line 2 (Red Line) - Anthoupoli to Elliniko
    val Metro2 = Color(0xFFED1C24) // Official red
    
    // Line 3 (Blue Line) - Dimotiko Theatro to Airport
    val Metro3 = Color(0xFF0066B3) // Official blue
    
    // Line 4 (planned) - Using yellow as distinctive color
    val Metro4 = Color(0xFFFFC107) // Yellow (future line)
    
    // ===== ATHENS TRAM =====
    // Line 6 & 7 (both Green according to official sources)
    val Tram6 = Color(0xFF00A651) // Green (same as Metro Line 1)
    val Tram7 = Color(0xFF00A651) // Green (same as Metro Line 1)
    
    // ===== ATHENS SUBURBAN RAILWAY (PROASTIAKOS) =====
    // Line A1 - Piraeus to Airport (Yellow)
    val SuburbanA1 = Color(0xFFFFD600) // Official yellow
    
    // Line A2 - Ano Liosia to Airport (Purple)
    val SuburbanA2 = Color(0xFF9C27B0) // Official purple
    
    // Line A3 - Athens to Chalcis (Lime green)
    val SuburbanA3 = Color(0xFF8BC34A) // Official lime green
    
    // Line A4 - Piraeus to Kiato (Sky blue)
    val SuburbanA4 = Color(0xFF87CEEB) // Official sky blue
    
    // ===== TROLLEY & BUSES =====
    // Trolley - Orange (official OASA color)
    val Trolley = Color(0xFFF27C02) // Official orange
    
    // Buses - Cyan blue (official OASA color)
    val Bus = Color(0xFF009EC6) // Official cyan blue
    
    /**
     * Get color by line code/number
     */
    fun getColorForLine(lineCode: String): Color {
        return when {
            // Metro
            lineCode.equals("M1", ignoreCase = true) || lineCode.equals("1", ignoreCase = true) -> Metro1
            lineCode.equals("M2", ignoreCase = true) || lineCode.equals("2", ignoreCase = true) -> Metro2
            lineCode.equals("M3", ignoreCase = true) || lineCode.equals("3", ignoreCase = true) -> Metro3
            lineCode.equals("M4", ignoreCase = true) || lineCode.equals("4", ignoreCase = true) -> Metro4
            
            // Tram
            lineCode.equals("T6", ignoreCase = true) || lineCode.equals("6", ignoreCase = true) -> Tram6
            lineCode.equals("T7", ignoreCase = true) || lineCode.equals("7", ignoreCase = true) -> Tram7
            
            // Suburban Railway
            lineCode.equals("A1", ignoreCase = true) -> SuburbanA1
            lineCode.equals("A2", ignoreCase = true) -> SuburbanA2
            lineCode.equals("A3", ignoreCase = true) -> SuburbanA3
            lineCode.equals("A4", ignoreCase = true) -> SuburbanA4
            
            // Default
            else -> Bus
        }
    }
    
    /**
     * Get color by transport category
     */
    fun getColorForCategory(category: String, isBus: Boolean = false): Color {
        return when (category.lowercase()) {
            "metro" -> Metro1
            "tram" -> Tram6
            "trolleys" -> Trolley
            "suburban", "proastiakos" -> SuburbanA1
            "buses" -> Bus
            else -> Bus
        }
    }
    
    /**
     * Get hex color string by line code
     */
    fun getHexColorForLine(lineCode: String): String {
        return when {
            // Metro
            lineCode.equals("M1", ignoreCase = true) || lineCode.equals("1", ignoreCase = true) -> "#00A651"
            lineCode.equals("M2", ignoreCase = true) || lineCode.equals("2", ignoreCase = true) -> "#ED1C24"
            lineCode.equals("M3", ignoreCase = true) || lineCode.equals("3", ignoreCase = true) -> "#0066B3"
            lineCode.equals("M4", ignoreCase = true) || lineCode.equals("4", ignoreCase = true) -> "#FFC107"
            
            // Tram
            lineCode.equals("T6", ignoreCase = true) || lineCode.equals("6", ignoreCase = true) -> "#00A651"
            lineCode.equals("T7", ignoreCase = true) || lineCode.equals("7", ignoreCase = true) -> "#00A651"
            
            // Suburban Railway
            lineCode.equals("A1", ignoreCase = true) -> "#FFD600"
            lineCode.equals("A2", ignoreCase = true) -> "#9C27B0"
            lineCode.equals("A3", ignoreCase = true) -> "#8BC34A"
            lineCode.equals("A4", ignoreCase = true) -> "#87CEEB"
            
            // Default
            else -> "#009EC6"
        }
    }
    
    /**
     * Get hex color string by category
     */
    fun getHexColorForCategory(category: String, isBus: Boolean = false): String {
        return when (category.lowercase()) {
            "metro" -> "#00A651"
            "tram" -> "#00A651"
            "trolleys" -> "#F27C02"
            "suburban", "proastiakos" -> "#FFD600"
            "buses" -> "#009EC6"
            else -> "#009EC6"
        }
    }
}
