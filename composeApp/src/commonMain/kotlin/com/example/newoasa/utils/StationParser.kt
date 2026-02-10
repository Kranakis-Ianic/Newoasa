package com.example.newoasa.utils

import com.example.newoasa.model.TransitStation
import kotlinx.serialization.json.*

object StationParser {
    
    /**
     * Parse transit stations from a GeoJSON file content
     */
    fun parseStationsFromGeoJSON(
        geoJsonContent: String,
        lineNumber: String,
        category: String
    ): List<TransitStation> {
        val stations = mutableListOf<TransitStation>()
        
        try {
            val json = Json.parseToJsonElement(geoJsonContent).jsonObject
            val features = json["features"]?.jsonArray ?: return emptyList()
            
            features.forEach { feature ->
                val featureObj = feature.jsonObject
                val properties = featureObj["properties"]?.jsonObject ?: return@forEach
                val geometry = featureObj["geometry"]?.jsonObject ?: return@forEach
                
                // Check if this is a station (not a line or crossing)
                val type = properties["type"]?.jsonPrimitive?.content
                if (type != "Station") return@forEach
                
                // Skip railway crossings
                val railway = properties["railway"]?.jsonPrimitive?.content
                if (railway == "railway_crossing" || railway == "crossing") return@forEach
                
                // Extract station name
                val name = properties["name"]?.jsonPrimitive?.content ?: return@forEach
                val nameEn = properties["int_name"]?.jsonPrimitive?.content 
                    ?: properties["name:en"]?.jsonPrimitive?.content
                
                // Extract coordinates
                if (geometry["type"]?.jsonPrimitive?.content != "Point") return@forEach
                val coordinates = geometry["coordinates"]?.jsonArray ?: return@forEach
                if (coordinates.size < 2) return@forEach
                
                val longitude = coordinates[0].jsonPrimitive.double
                val latitude = coordinates[1].jsonPrimitive.double
                
                // Extract ID
                val id = properties["@id"]?.jsonPrimitive?.content 
                    ?: "station_${name.hashCode()}_${lineNumber}"
                
                val station = TransitStation(
                    id = id,
                    name = name,
                    nameEn = nameEn,
                    latitude = latitude,
                    longitude = longitude
                )
                station.lines.add(TransitStation.StationLine(lineNumber, category))
                
                stations.add(station)
            }
        } catch (e: Exception) {
            println("Error parsing GeoJSON for line $lineNumber: ${e.message}")
            e.printStackTrace()
        }
        
        return stations
    }
    
    /**
     * Cluster stations that are at the same location (within threshold)
     * This merges stations that are physically the same but appear in multiple line files
     */
    fun clusterStations(
        stations: List<TransitStation>,
        thresholdDegrees: Double = 0.0001 // ~11 meters
    ): List<TransitStation> {
        if (stations.isEmpty()) return emptyList()
        
        val clustered = mutableListOf<TransitStation>()
        val processed = mutableSetOf<String>()
        
        stations.forEach { station ->
            if (station.id in processed) return@forEach
            
            // Find all stations within threshold distance
            val nearbyStations = stations.filter { other ->
                other.id !in processed &&
                kotlin.math.abs(other.latitude - station.latitude) < thresholdDegrees &&
                kotlin.math.abs(other.longitude - station.longitude) < thresholdDegrees
            }
            
            if (nearbyStations.isEmpty()) {
                clustered.add(station)
                processed.add(station.id)
            } else {
                // Merge all nearby stations into one
                val allNearby = nearbyStations + station
                val mergedLines = allNearby.flatMap { it.lines }.toMutableSet()
                
                val merged = TransitStation(
                    id = station.id,
                    name = station.name,
                    nameEn = station.nameEn,
                    latitude = allNearby.map { it.latitude }.average(),
                    longitude = allNearby.map { it.longitude }.average(),
                    lines = mergedLines
                )
                
                clustered.add(merged)
                allNearby.forEach { processed.add(it.id) }
            }
        }
        
        return clustered
    }
    
    /**
     * Merge stations with the same ID but from different lines
     */
    fun mergeStationsByIdorLocation(stations: List<TransitStation>): List<TransitStation> {
        val stationMap = mutableMapOf<String, TransitStation>()
        
        stations.forEach { station ->
            val existing = stationMap[station.id]
            if (existing != null) {
                // Merge lines
                existing.lines.addAll(station.lines)
            } else {
                stationMap[station.id] = station.copy()
            }
        }
        
        return stationMap.values.toList()
    }
}