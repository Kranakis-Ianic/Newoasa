package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

@Entity(tableName = "tram_lines")
data class TramLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String,
    val routePaths: String,
    val isActive: Boolean = true,
    val tramType: String = "standard", // standard, modern, vintage
    val lastUpdated: Long = currentTimeMillis()
)

@Entity(tableName = "tram_stops")
data class TramStopEntity(
    @PrimaryKey
    val stopId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String,
    val order: Int,
    val hasAccessiblePlatform: Boolean = false,
    val lastUpdated: Long = currentTimeMillis()
)
