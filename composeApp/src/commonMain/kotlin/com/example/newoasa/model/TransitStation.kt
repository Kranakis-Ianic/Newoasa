package com.example.newoasa.model

import kotlinx.serialization.Serializable

@Serializable
data class TransitStation(
    val id: String,
    val name: String,
    val nameEn: String?,
    val latitude: Double,
    val longitude: Double,
    val lines: MutableSet<StationLine> = mutableSetOf()
) {
    @Serializable
    data class StationLine(
        val lineNumber: String,
        val category: String
    )
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransitStation) return false
        return id == other.id
    }
    
    override fun hashCode(): Int = id.hashCode()
}

data class ClusteredStation(
    val station: TransitStation,
    val pixelX: Double,
    val pixelY: Double
)