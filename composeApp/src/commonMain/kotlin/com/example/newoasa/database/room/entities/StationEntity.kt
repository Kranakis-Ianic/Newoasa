package com.example.newoasa.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey

/**
 * Base entity for all station types
 * Room Multiplatform compatible
 */
@Entity(
    tableName = "stations",
    indices = [
        Index(value = ["stationType"]),
        Index(value = ["lineId"]),
        Index(value = ["stopCode"], unique = true),
        Index(value = ["latitude", "longitude"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TransitLineEntity::class,
            parentColumns = ["id"],
            childColumns = ["lineId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val lineId: Long,
    val stationType: String,
    val name: String,
    val stopCode: String,
    val orderOnLine: Int,
    val latitude: Double,
    val longitude: Double,
    
    // Bus/Trolley specific
    val address: String? = null,
    val direction: String? = null,
    
    // Metro/Suburban specific
    val hasAccessibility: Boolean = false,
    val hasElevators: Boolean = false,
    val hasEscalators: Boolean = false,
    val hasParking: Boolean = false,
    val hasBikeParking: Boolean = false,
    val ticketZone: Int? = null,
    val platforms: String? = null,
    val depthMeters: Int? = null,
    val levelType: String? = null,
    
    // Tram specific
    val platformType: String? = null,
    val hasShelter: Boolean = false,
    
    // Common
    val connections: String? = null,
    val isTransferStation: Boolean = false,
    val openingYear: Int? = null,
    val notes: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

/**
 * Station type constants
 */
object StationType {
    const val BUS_STOP = "bus_stop"
    const val TROLLEY_STOP = "trolley_stop"
    const val METRO_STATION = "metro_station"
    const val TRAM_STOP = "tram_stop"
    const val SUBURBAN_STATION = "suburban_station"
}

/**
 * Extension functions
 */
fun StationEntity.getConnectionsList(): List<String> {
    return connections?.split(",")?.map { it.trim() } ?: emptyList()
}

fun StationEntity.getPlatformsList(): List<String> {
    return platforms?.split(",")?.map { it.trim() } ?: emptyList()
}
