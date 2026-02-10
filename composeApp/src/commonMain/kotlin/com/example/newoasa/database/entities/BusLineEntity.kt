package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

@Entity(tableName = "bus_lines")
data class BusLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string of route IDs
    val routePaths: String, // JSON string of route paths
    val isActive: Boolean = true,
    val isExpress: Boolean = false, // Express bus service
    val isNightService: Boolean = false, // Night bus service
    val operatingHours: String = "",
    val frequency: String = "", // Average frequency
    val lastUpdated: Long = currentTimeMillis()
)

@Entity(tableName = "bus_stops")
data class BusStopEntity(
    @PrimaryKey
    val stopId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to bus_lines
    val order: Int,
    val hasShelter: Boolean = false,
    val hasRealTimeInfo: Boolean = false,
    val hasTicketMachine: Boolean = false,
    val lastUpdated: Long = currentTimeMillis()
)
