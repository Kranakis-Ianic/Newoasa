package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suburban_railway_lines")
data class SuburbanRailwayLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String, // JSON string of route IDs
    val routePaths: String, // JSON string of route paths
    val isActive: Boolean = true,
    val trainType: String = "", // Type of train (e.g., "Proastiakos")
    val operatingHours: String = "",
    val frequency: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "suburban_railway_stations")
data class SuburbanRailwayStationEntity(
    @PrimaryKey
    val stationId: String,
    val name: String,
    val stationCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String, // Foreign key to suburban_railway_lines
    val order: Int,
    val isAccessible: Boolean = false,
    val hasElevator: Boolean = false,
    val hasParkingLot: Boolean = false,
    val hasIntermodalConnection: Boolean = false, // Connection to other transit types
    val connectedLines: String = "", // JSON string of connected line IDs
    val lastUpdated: Long = System.currentTimeMillis()
)
