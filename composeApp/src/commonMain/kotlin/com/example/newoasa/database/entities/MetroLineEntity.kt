package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metro_lines")
data class MetroLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string of route IDs
    val routePaths: String, // JSON string of route paths
    val isActive: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "metro_stations")
data class MetroStationEntity(
    @PrimaryKey
    val stationId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to metro_lines
    val order: Int,
    val isAccessible: Boolean = false,
    val hasElevator: Boolean = false,
    val hasEscalator: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
