package com.example.newoasa.database.models

/**
 * Common data model for transit lines
 * Platform-agnostic representation
 */
data class TransitLine(
    val id: Long = 0,
    val lineNumber: String,
    val category: String,  // "bus", "trolley", "metro", "tram", "suburban"
    val displayName: String,
    val routeCount: Int = 0,
    val routeIds: List<String> = emptyList(),
    val color: String,
    val isActive: Boolean = true,
    val description: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
