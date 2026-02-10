package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Entity representing a transit line in the database
 * This entity stores information about bus, trolley, metro, tram, and suburban railway lines
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
    
    /**
     * Line identifier (e.g., "022", "1", "309Β", "T6")
     */
    val lineNumber: String,
    
    /**
     * Category of transit:
     * - "bus" for buses
     * - "trolley" for trolleys
     * - "metro" for metro lines
     * - "tram" for tram lines
     * - "suburban" for suburban railway (proastiakos)
     */
    val category: String,
    
    /**
     * Display name for the line (e.g., "Bus 022", "Metro Line 1")
     */
    val displayName: String,
    
    /**
     * Total number of routes for this line
     */
    val routeCount: Int = 0,
    
    /**
     * Comma-separated list of route IDs
     */
    val routeIds: String = "",
    
    /**
     * Line color in hex format (e.g., "#FF0000" for red)
     */
    val color: String,
    
    /**
     * Whether this line is currently active/operational
     */
    val isActive: Boolean = true,
    
    /**
     * Optional description or notes about the line
     */
    val description: String? = null,
    
    /**
     * Timestamp when the line was added to database
     */
    val createdAt: Long = System.currentTimeMillis(),
    
    /**
     * Timestamp when the line was last updated
     */
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Extension function to get route IDs as a list
 */
fun TransitLineEntity.getRouteIdsList(): List<String> {
    return if (routeIds.isBlank()) emptyList() else routeIds.split(",")
}
