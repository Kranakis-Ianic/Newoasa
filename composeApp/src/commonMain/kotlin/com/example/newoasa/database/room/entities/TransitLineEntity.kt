package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Entity representing a transit line in the database
 * This entity stores information about bus, trolley, metro, tram, and suburban railway lines
 * Room Multiplatform compatible
 */
@Entity(
    tableName = "transit_lines",
    indices = [
        Index(value = ["lineNumber", "category"], unique = true),
        Index(value = ["category"])
    ]
)
data class TransitLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val lineNumber: String,
    val category: String,  // "bus", "trolley", "metro", "tram", "suburban"
    val displayName: String,
    val routeCount: Int = 0,
    val routeIds: String = "",  // Comma-separated list
    val color: String,  // Hex color
    val isActive: Boolean = true,
    val description: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

/**
 * Extension function to get route IDs as a list
 */
fun TransitLineEntity.getRouteIdsList(): List<String> {
    return if (routeIds.isBlank()) emptyList() else routeIds.split(",")
}
