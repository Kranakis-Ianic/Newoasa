package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

/**
 * Station/Stop entity for Room database
 * Represents a single station or stop for any transport type
 */
@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    val stationId: String,
    val name: String,
    val stationCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to transit_lines
    val stationOrder: Int, // Order along the line
    val transportType: String, // "bus", "metro", "tram", "trolley", "railway"
    val hasAccessibility: Boolean = false,
    val lastUpdated: Long = currentTimeMillis()
)
