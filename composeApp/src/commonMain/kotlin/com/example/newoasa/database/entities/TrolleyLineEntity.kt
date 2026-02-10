package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newoasa.utils.currentTimeMillis

@Entity(tableName = "trolley_lines")
data class TrolleyLineEntity(
    @PrimaryKey
    val lineId: String,
    val lineNumber: String,
    val displayName: String,
    val color: String,
    val routeIds: String,
    val routePaths: String,
    val isActive: Boolean = true,
    val trolleyType: String = "standard", // standard, articulated
    val lastUpdated: Long = currentTimeMillis()
)

@Entity(tableName = "trolley_stops")
data class TrolleyStopEntity(
    @PrimaryKey
    val stopId: String,
    val name: String,
    val stopCode: String,
    val latitude: Double,
    val longitude: Double,
    val lineId: String,
    val order: Int,
    val hasShelter: Boolean = false,
    val lastUpdated: Long = currentTimeMillis()
)
