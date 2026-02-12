package com.example.newoasa.data.models

/**
 * Common data model for stations/stops
 * Platform-agnostic representation
 */
data class Station(
    val id: Long = 0,
    val lineId: Long,
    val stationType: String,  // "bus_stop", "trolley_stop", "metro_station", "tram_stop", "suburban_station"
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
    val platforms: List<String> = emptyList(),
    val depthMeters: Int? = null,
    val levelType: String? = null,
    
    // Tram specific
    val platformType: String? = null,
    val hasShelter: Boolean = false,
    
    // Common
    val connections: List<String> = emptyList(),
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
