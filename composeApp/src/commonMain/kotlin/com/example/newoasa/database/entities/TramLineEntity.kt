package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tram_lines")
data class TramLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string of route IDs
    val routePaths: String, // JSON string of route paths
    val isActive: Boolean = true,
    val operatingHours: String = "", // Operating hours info
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "tram_stops")
data class TramStopEntity(
    @PrimaryKey
    val stopId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to tram_lines
    val order: Int,
    val hasShelter: Boolean = false,
    val hasTicketMachine: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
