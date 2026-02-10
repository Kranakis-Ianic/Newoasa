package com.example.newoasa.database.migration

import android.content.Context
import com.example.newoasa.database.TransitDatabase
import com.example.newoasa.database.entities.StationEntity
import com.example.newoasa.database.entities.StationType
import com.example.newoasa.database.entities.TransitLineEntity
import com.example.newoasa.data.TransitLineRepository
import com.example.newoasa.LineColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import newoasa.composeapp.generated.resources.Res

/**
 * Helper class to migrate data from GeoJSON files to Room database
 */
class DataMigrationHelper(private val context: Context) {
    
    private val database = TransitDatabase.getInstance(context)
    private val transitLineDao = database.transitLineDao()
    private val stationDao = database.stationDao()
    
    /**
     * Perform full data migration from GeoJSON files to database
     */
    @OptIn(ExperimentalResourceApi::class)
    suspend fun migrateAllData() = withContext(Dispatchers.IO) {
        println("Starting data migration to Room database...")
        
        // Step 1: Migrate transit lines
        migrateTransitLines()
        
        // Step 2: Migrate stations from GeoJSON files
        migrateStations()
        
        println("Data migration completed!")
    }
    
    /**
     * Migrate transit lines from in-memory repository
     */
    private suspend fun migrateTransitLines() {
        println("Migrating transit lines...")
        
        val lines = TransitLineRepository.getAllLines()
        val entities = lines.map { transitLine ->
            val category = when (transitLine.category) {
                "buses" -> "bus"
                "trolleys" -> "trolley"
                "metro" -> "metro"
                "tram" -> "tram"
                else -> transitLine.category
            }
            
            val color = LineColors.getHexColorForCategory(
                transitLine.category,
                transitLine.isBus
            )
            
            TransitLineEntity(
                lineNumber = transitLine.lineNumber,
                category = category,
                displayName = transitLine.displayName,
                routeCount = transitLine.routeIds.size,
                routeIds = transitLine.routeIds.joinToString(","),
                color = color,
                isActive = true
            )
        }
        
        val insertedIds = transitLineDao.insertLines(entities)
        println("Migrated ${insertedIds.size} transit lines")
    }
    
    /**
     * Migrate stations from GeoJSON files
     */
    @OptIn(ExperimentalResourceApi::class)
    private suspend fun migrateStations() {
        println("Migrating stations from GeoJSON files...")
        
        val lines = TransitLineRepository.getAllLines()
        var totalStations = 0
        
        for (transitLine in lines) {
            // Get the database ID for this line
            val lineEntity = transitLineDao.getLineByNumberAndCategory(
                transitLine.lineNumber,
                when (transitLine.category) {
                    "buses" -> "bus"
                    "trolleys" -> "trolley"
                    else -> transitLine.category
                }
            )
            
            if (lineEntity == null) {
                println("Warning: Line entity not found for ${transitLine.lineNumber}")
                continue
            }
            
            // Process each route's GeoJSON file
            for (routePath in transitLine.routePaths) {
                try {
                    val geoJsonString = Res.readBytes(routePath).decodeToString()
                    val stations = extractStationsFromGeoJson(
                        geoJsonString,
                        lineEntity.id,
                        transitLine
                    )
                    
                    if (stations.isNotEmpty()) {
                        stationDao.insertStations(stations)
                        totalStations += stations.size
                    }
                } catch (e: Exception) {
                    println("Error processing route $routePath: ${e.message}")
                }
            }
        }
        
        println("Migrated $totalStations stations")
    }
    
    /**
     * Extract stations from GeoJSON file
     */
    private fun extractStationsFromGeoJson(
        geoJsonString: String,
        lineId: Long,
        transitLine: com.example.newoasa.data.TransitLine
    ): List<StationEntity> {
        val stations = mutableListOf<StationEntity>()
        
        try {
            val geoJson = JSONObject(geoJsonString)
            val features = geoJson.optJSONArray("features") ?: return emptyList()
            
            for (i in 0 until features.length()) {
                val feature = features.getJSONObject(i)
                val properties = feature.optJSONObject("properties")
                val geometry = feature.optJSONObject("geometry")
                
                val featureType = properties?.optString("type")
                val geometryType = geometry?.optString("type")
                
                // Only process stop/station features
                val isStop = featureType == "stop" ||
                        (featureType.isNullOrEmpty() && (geometryType == "Point" || geometryType == "MultiPoint"))
                
                if (isStop) {
                    val coordinates = geometry?.optJSONArray("coordinates")
                    if (coordinates != null && coordinates.length() >= 2) {
                        val lng = coordinates.getDouble(0)
                        val lat = coordinates.getDouble(1)
                        val stopName = properties?.optString("name") ?: "Unknown"
                        val stopCode = properties?.optString("stop_code") ?: ""
                        val order = properties?.optString("order")?.toIntOrNull() ?: (i + 1)
                        
                        // Determine station type based on line category
                        val stationType = when (transitLine.category) {
                            "buses" -> StationType.BUS_STOP
                            "trolleys" -> StationType.TROLLEY_STOP
                            "metro" -> StationType.METRO_STATION
                            "tram" -> StationType.TRAM_STOP
                            else -> StationType.BUS_STOP
                        }
                        
                        stations.add(
                            StationEntity(
                                lineId = lineId,
                                stationType = stationType,
                                name = stopName,
                                stopCode = stopCode,
                                orderOnLine = order,
                                latitude = lat,
                                longitude = lng,
                                isActive = true
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("Error extracting stations from GeoJSON: ${e.message}")
            e.printStackTrace()
        }
        
        return stations
    }
    
    /**
     * Check if database needs migration (is empty)
     */
    suspend fun needsMigration(): Boolean {
        val lineCount = transitLineDao.getTotalLineCount()
        return lineCount == 0
    }
    
    /**
     * Clear all data from database
     */
    suspend fun clearDatabase() {
        stationDao.deleteAllStations()
        transitLineDao.deleteAllLines()
        println("Database cleared")
    }
}
