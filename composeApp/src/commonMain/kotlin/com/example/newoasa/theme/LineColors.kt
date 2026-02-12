package com.example.newoasa.theme

import androidx.compose.ui.graphics.Color
import com.example.newoasa.utils.LineInfoLoader
import kotlinx.coroutines.runBlocking

/**
 * Line colour accessors.
 *
 * Source of truth: GeoJSON `properties.colour` in `files/geojson/final_all_lines.geojson`.
 */
object LineColors {

    fun getHexColorForLine(lineCode: String): String = runBlocking {
        LineInfoLoader.getHexColorForLine(lineCode)
    }

    fun getColorForLine(lineCode: String): Color = parseHex(getHexColorForLine(lineCode))

    private fun parseHex(hex: String): Color {
        val clean = hex.removePrefix("#")
        val value = clean.toLong(16)
        return when (clean.length) {
            6 -> Color(0xFF000000L or value)
            8 -> Color(value)
            else -> error("Invalid hex colour: $hex")
        }
    }

    fun clearCache() {
        LineInfoLoader.clearCache()
    }
}
