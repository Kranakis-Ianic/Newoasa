package com.example.newoasa

import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
class TransitLineRepository {
    private val lineIndex = mutableListOf<TransitLine>()
    
    init {
        // Initialize with your line data
        // You'll need to populate this based on your actual folder structure
        indexLines()
    }
    
    private fun indexLines() {
        // TODO: Replace this with actual data from your GeoJSON folder structure
        // This is a placeholder showing the structure
        // Example structure based on your description:
        // files/geojson/category1/line1/...
        // files/geojson/category2/line2/...
        
        // Placeholder examples - replace with your actual lines
        lineIndex.addAll(listOf(
            // Add your actual transit lines here
            // TransitLine(
            //     id = "1",
            //     name = "Line 1",
            //     category = "metro",
            //     routeNumber = "M1",
            //     geoJsonPath = "files/geojson/metro/line1/route.geojson"
            // )
        ))
    }
    
    fun searchLines(query: String): List<TransitLine> {
        if (query.isBlank()) return emptyList()
        
        val lowercaseQuery = query.lowercase()
        return lineIndex.filter { line ->
            line.name.lowercase().contains(lowercaseQuery) ||
            line.routeNumber.lowercase().contains(lowercaseQuery) ||
            line.category.lowercase().contains(lowercaseQuery)
        }
    }
    
    fun getAllLines(): List<TransitLine> = lineIndex.toList()
    
    fun getLinesByCategory(category: String): List<TransitLine> {
        return lineIndex.filter { it.category.equals(category, ignoreCase = true) }
    }
}
