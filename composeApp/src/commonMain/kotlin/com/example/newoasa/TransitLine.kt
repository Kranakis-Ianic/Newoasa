package com.example.newoasa

data class TransitLine(
    val id: String,
    val name: String,
    val category: String, // e.g., "bus", "metro"
    val routeNumber: String,
    val geoJsonPath: String,
    val color: String? = null
)
