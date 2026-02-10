package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

/**
 * Transit line entity for Room database
 * Represents a single transit line (bus, metro, tram, etc.)
 */
@Entity(tableName = "transit_lines")
data class TransitLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val transportType: String, // "bus", "metro", "tram", "trolley", "railway"
    val color: String, // Hex color code
    val routeIds: String, // JSON array of route IDs
    val routePaths: String, // JSON array of route geometries
    val isActive: Boolean = true,
    val lastUpdated: Long = currentTimeMillis()
)
