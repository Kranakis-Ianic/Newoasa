package com.example.newoasa.database

import com.example.newoasa.database.entities.StationEntity
import com.example.newoasa.database.entities.TransitLineEntity
import com.example.newoasa.database.models.Station
import com.example.newoasa.database.models.TransitLine

/**
 * Conversion functions between Room entities and common models
 */

// TransitLine conversions

fun TransitLineEntity.toModel(): TransitLine {
    return TransitLine(
        id = this.id,
        lineNumber = this.lineNumber,
        category = this.category,
        displayName = this.displayName,
        routeCount = this.routeCount,
        routeIds = this.routeIds.split(",").filter { it.isNotBlank() },
        color = this.color,
        isActive = this.isActive,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun TransitLine.toEntity(): TransitLineEntity {
    return TransitLineEntity(
        id = this.id,
        lineNumber = this.lineNumber,
        category = this.category,
        displayName = this.displayName,
        routeCount = this.routeCount,
        routeIds = this.routeIds.joinToString(","),
        color = this.color,
        isActive = this.isActive,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

// Station conversions

fun StationEntity.toModel(): Station {
    return Station(
        id = this.id,
        lineId = this.lineId,
        stationType = this.stationType,
        name = this.name,
        stopCode = this.stopCode,
        orderOnLine = this.orderOnLine,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        direction = this.direction,
        hasAccessibility = this.hasAccessibility,
        hasElevators = this.hasElevators,
        hasEscalators = this.hasEscalators,
        hasParking = this.hasParking,
        hasBikeParking = this.hasBikeParking,
        ticketZone = this.ticketZone,
        platforms = this.platforms?.split(",")?.map { it.trim() } ?: emptyList(),
        depthMeters = this.depthMeters,
        levelType = this.levelType,
        platformType = this.platformType,
        hasShelter = this.hasShelter,
        connections = this.connections?.split(",")?.map { it.trim() } ?: emptyList(),
        isTransferStation = this.isTransferStation,
        openingYear = this.openingYear,
        notes = this.notes,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Station.toEntity(): StationEntity {
    return StationEntity(
        id = this.id,
        lineId = this.lineId,
        stationType = this.stationType,
        name = this.name,
        stopCode = this.stopCode,
        orderOnLine = this.orderOnLine,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        direction = this.direction,
        hasAccessibility = this.hasAccessibility,
        hasElevators = this.hasElevators,
        hasEscalators = this.hasEscalators,
        hasParking = this.hasParking,
        hasBikeParking = this.hasBikeParking,
        ticketZone = this.ticketZone,
        platforms = this.platforms.joinToString(","),
        depthMeters = this.depthMeters,
        levelType = this.levelType,
        platformType = this.platformType,
        hasShelter = this.hasShelter,
        connections = this.connections.joinToString(","),
        isTransferStation = this.isTransferStation,
        openingYear = this.openingYear,
        notes = this.notes,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
