package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Unified entity for all types of stations/stops
 * Works for metro stations, tram stops, bus stops, trolley stops, and suburban railway stations
 */
@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val stationCode: String,
    val stationName: String,
    val transportType: String, // "metro", "tram", "bus", "trolley", "suburban_railway"
    val lineCode: String?, // Associated line code (nullable for interchange stations)
    val latitude: Double,
    val longitude: Double,
    val order: Int? = null, // Order along the line (nullable for stations not on a specific route)
    val address: String? = null,
    val accessibility: Boolean = false
)
