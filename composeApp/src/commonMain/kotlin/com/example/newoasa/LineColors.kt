package com.example.newoasa

import androidx.compose.ui.graphics.Color

/**
 * Transit line colors used throughout the app
 */
object LineColors {
    // Metro - Red
    val Metro = Color(0xFFF44336)
    
    // Tram - Green  
    val Tram = Color(0xFF4CAF50)
    
    // Bus - Blue
    val Bus = Color(0xFF2196F3)
    
    // Trolley - Purple
    val Trolley = Color(0xFF9C27B0)
    
    // Default for unknown types
    val Default = Color(0xFF9E9E9E)
    
    /**
     * Get the appropriate color for a transit line category
     */
    fun getColorForCategory(category: String?, isBus: Boolean): Color {
        return when {
            category.equals("metro", ignoreCase = true) -> Metro
            category.equals("tram", ignoreCase = true) -> Tram
            isBus -> Bus
            else -> Trolley // Default to trolley for other types
        }
    }
    
    /**
     * Get hex color string for use in MapLibre (Android)
     */
    fun getHexColorForCategory(category: String?, isBus: Boolean): String {
        return when {
            category.equals("metro", ignoreCase = true) -> "#F44336"
            category.equals("tram", ignoreCase = true) -> "#4CAF50"
            isBus -> "#2196F3"
            else -> "#9C27B0"
        }
    }
}
