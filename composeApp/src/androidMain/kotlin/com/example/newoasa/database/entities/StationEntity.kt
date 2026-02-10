package com.example.newoasa.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey

/**
 * Base entity for all station types
 * Different transport types have different station characteristics:
 * - Buses/Trolleys: Many stops with codes, street-level
 * - Metro: Fewer stations, underground/elevated, accessibility features
 * - Tram: Street-level stops, platform information
 * - Suburban Railway: Major stations, ticket zones, facilities
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
    
    /**
     * Reference to the transit line this station belongs to
     */
    val lineId: Long,
    
    /**
     * Type of station:
     * - "bus_stop" for bus stops
     * - "trolley_stop" for trolley stops
     * - "metro_station" for metro stations
     * - "tram_stop" for tram stops
     * - "suburban_station" for suburban railway stations
     */
    val stationType: String,
    
    /**
     * Station/Stop name (e.g., "Syntagma", "Piraeus")
     */
    val name: String,
    
    /**
     * Unique stop code (OASA code for buses/trolleys, station code for metro/suburban)
     */
    val stopCode: String,
    
    /**
     * Order/sequence number on the line (starting from 1)
     */
    val orderOnLine: Int,
    
    /**
     * Latitude coordinate
     */
    val latitude: Double,
    
    /**
     * Longitude coordinate
     */
    val longitude: Double,
    
    // === Bus/Trolley specific fields ===
    /**
     * Street address (mainly for buses and trolleys)
     */
    val address: String? = null,
    
    /**
     * Direction description (e.g., "Towards Piraeus")
     */
    val direction: String? = null,
    
    // === Metro/Suburban specific fields ===
    /**
     * Whether the station has wheelchair accessibility
     */
    val hasAccessibility: Boolean = false,
    
    /**
     * Whether the station has elevators
     */
    val hasElevators: Boolean = false,
    
    /**
     * Whether the station has escalators
     */
    val hasEscalators: Boolean = false,
    
    /**
     * Whether the station has parking facilities
     */
    val hasParking: Boolean = false,
    
    /**
     * Whether the station has bicycle parking
     */
    val hasBikeParking: Boolean = false,
    
    /**
     * Ticket zone (for suburban railway and metro)
     */
    val ticketZone: Int? = null,
    
    /**
     * Platform numbers (comma-separated, e.g., "1,2")
     */
    val platforms: String? = null,
    
    /**
     * Station depth in meters (for underground metro stations)
     */
    val depthMeters: Int? = null,
    
    /**
     * Whether station is underground, elevated, or ground level
     * Values: "underground", "elevated", "ground"
     */
    val levelType: String? = null,
    
    // === Tram specific fields ===
    /**
     * Platform type (for tram stops: "island", "side", "both")
     */
    val platformType: String? = null,
    
    /**
     * Whether the stop has a shelter
     */
    val hasShelter: Boolean = false,
    
    // === Common fields ===
    /**
     * Connection lines at this station (comma-separated line numbers)
     */
    val connections: String? = null,
    
    /**
     * Whether this station is a transfer/interchange point
     */
    val isTransferStation: Boolean = false,
    
    /**
     * Opening year of the station
     */
    val openingYear: Int? = null,
    
    /**
     * Additional notes or information
     */
    val notes: String? = null,
    
    /**
     * Whether the station is currently operational
     */
    val isActive: Boolean = true,
    
    /**
     * Timestamp when added to database
     */
    val createdAt: Long = System.currentTimeMillis(),
    
    /**
     * Timestamp when last updated
     */
    val updatedAt: Long = System.currentTimeMillis()
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
 * Level type constants
 */
object LevelType {
    const val UNDERGROUND = "underground"
    const val ELEVATED = "elevated"
    const val GROUND = "ground"
}

/**
 * Platform type constants
 */
object PlatformType {
    const val ISLAND = "island"
    const val SIDE = "side"
    const val BOTH = "both"
}

/**
 * Extension functions for convenience
 */
fun StationEntity.getConnectionsList(): List<String> {
    return connections?.split(",")?.map { it.trim() } ?: emptyList()
}

fun StationEntity.getPlatformsList(): List<String> {
    return platforms?.split(",")?.map { it.trim() } ?: emptyList()
}

fun StationEntity.isBusOrTrolleyStop(): Boolean {
    return stationType == StationType.BUS_STOP || stationType == StationType.TROLLEY_STOP
}

fun StationEntity.isRailStation(): Boolean {
    return stationType == StationType.METRO_STATION || stationType == StationType.SUBURBAN_STATION
}
