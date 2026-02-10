package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

@Entity(tableName = "metro_lines")
data class MetroLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String, // M1, M2, M3, M4
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string
    val routePaths: String, // JSON string
    val isActive: Boolean = true,
    val lastUpdated: Long = currentTimeMillis()
)

@Entity(tableName = "metro_stations")
data class MetroStationEntity(
    @PrimaryKey
    val stationId: String,
    val name: String,
    val stationCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key
    val order: Int,
    val hasElevator: Boolean = false,
    val hasEscalator: Boolean = false,
    val isInterchange: Boolean = false,
    val interchangeLines: String = "", // JSON array of connecting lines
    val lastUpdated: Long = currentTimeMillis()
)
