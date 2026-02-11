package com.example.newoasa.data

/**
 * Represents a transit station with its associated lines
 */
data class StationInfo(
    val name: String,
    val nameEn: String?,
    val latitude: Double,
    val longitude: Double,
    val lines: List<LineInfo>,
    val wheelchair: Boolean = false
)

/**
 * Line information for a station
 */
data class LineInfo(
    val ref: String,        // e.g., "Μ1", "Μ2", "T6"
    val colour: String?,     // e.g., "#007A33"
    val name: String?,       // e.g., "Μετρό Μ1: Κηφισιά → Πειραιά"
    val route: String?       // e.g., "subway", "tram"
)
