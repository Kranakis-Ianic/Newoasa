package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color

object LineColors {
    // Metro lines
    val Metro1 = Color(0xFF00A651) // Green Line 1
    val Metro2 = Color(0xFFED1C24) // Red Line 2
    val Metro3 = Color(0xFF0066B3) // Blue Line 3
    
    // Tram
    val Tram = Color(0xFFFF8C00) // Orange
    
    // Trolley
    val Trolley = Color(0xFF9C27B0) // Purple
    
    // Buses - default blue
    val Bus = Color(0xFF1976D2) // Blue
    
    fun getColorForCategory(category: String, isBus: Boolean = false): Color {
        return when (category) {
            "metro" -> Metro1
            "tram" -> Tram
            "trolleys" -> Trolley
            "buses" -> Bus
            else -> Bus
        }
    }
    
    fun getHexColorForCategory(category: String, isBus: Boolean = false): String {
        return when (category) {
            "metro" -> "#00A651"
            "tram" -> "#FF8C00"
            "trolleys" -> "#9C27B0"
            "buses" -> "#1976D2"
            else -> "#1976D2"
        }
    }
}
