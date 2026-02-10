package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trolley_lines")
data class TrolleyLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string of route IDs
    val routePaths: String, // JSON string of route paths
    val isActive: Boolean = true,
    val operatingHours: String = "",
    val frequency: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "trolley_stops")
data class TrolleyStopEntity(
    @PrimaryKey
    val stopId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to trolley_lines
    val order: Int,
    val hasShelter: Boolean = false,
    val hasRealTimeInfo: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
