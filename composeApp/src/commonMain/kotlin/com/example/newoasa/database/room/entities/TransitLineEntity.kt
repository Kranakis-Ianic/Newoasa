package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Unified entity for all types of transit lines
 * Works for buses, trolleys, metro, tram, and suburban railway
 */
@Entity(tableName = "transit_lines")
data class TransitLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val lineCode: String,
    val lineName: String,
    val category: String, // "buses", "trolleys", "metro", "tram", "suburban_railway"
    val transportType: String, // Specific type within category
    val color: String, // Hex color code
    val routePaths: List<String> // List of GeoJSON resource paths
)
