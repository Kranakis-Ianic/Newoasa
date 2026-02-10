package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

@Entity(tableName = "suburban_railway_lines")
data class SuburbanRailwayEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String, // R1, R2, etc.
    val displayName: String,
    val color: String,
    val routeIds: String,
    val routePaths: String,
    val isActive: Boolean = true,
    val operator: String = "",
    val serviceType: String = "commuter", // commuter, regional
    val lastUpdated: Long = currentTimeMillis()
)

@Entity(tableName = "railway_stations")
data class RailwayStationEntity(
    @PrimaryKey
    val stationId: String,
    val name: String,
    val stationCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String,
    val order: Int,
    val hasTicketOffice: Boolean = false,
    val hasParkingRide: Boolean = false,
    val hasElevator: Boolean = false,
    val isInterchange: Boolean = false,
    val interchangeLines: String = "",
    val lastUpdated: Long = currentTimeMillis()
)
