package com.example.newoasa.database.repository

import com.example.newoasa.database.dao.StationDao
import com.example.newoasa.database.dao.TransitLineDao
import com.example.newoasa.database.entities.StationEntity
import com.example.newoasa.database.entities.StationType
import com.example.newoasa.database.entities.TransitLineEntity
import com.example.newoasa.data.TransitLine
import com.example.newoasa.data.TransitLineRepository as InMemoryRepository
import com.example.newoasa.LineColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository that provides access to transit data from Room database
 * and can sync with in-memory TransitLineRepository
 */
class TransitDataRepository(
    private val transitLineDao: TransitLineDao,
    private val stationDao: StationDao
) {
    
    // === Transit Lines ===
    
    /**
     * Get all lines as Flow
     */
    fun getAllLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getAllLinesFlow()
    }
    
    /**
     * Get lines by category
     */
    fun getLinesByCategory(category: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow(category)
    }
    
    /**
     * Get bus lines
     */
    fun getBusLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getBusLinesFlow()
    }
    
    /**
     * Get trolley lines
     */
    fun getTrolleyLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getTrolleyLinesFlow()
    }
    
    /**
     * Get metro lines
     */
    fun getMetroLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getMetroLinesFlow()
    }
    
    /**
     * Get tram lines
     */
    fun getTramLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getTramLinesFlow()
    }
    
    /**
     * Search lines by query
     */
    fun searchLines(query: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.searchLinesFlow(query)
    }
    
    /**
     * Get line by ID
     */
    suspend fun getLineById(id: Long): TransitLineEntity? {
        return transitLineDao.getLineById(id)
    }
    
    /**
     * Insert or update a line
     */
    suspend fun upsertLine(line: TransitLineEntity): Long {
        return transitLineDao.insertLine(line)
    }
    
    /**
     * Insert or update multiple lines
     */
    suspend fun upsertLines(lines: List<TransitLineEntity>): List<Long> {
        return transitLineDao.insertLines(lines)
    }
    
    // === Stations ===
    
    /**
     * Get all stations
     */
    fun getAllStations(): Flow<List<StationEntity>> {
        return stationDao.getAllStationsFlow()
    }
    
    /**
     * Get stations for a specific line
     */
    fun getStationsByLine(lineId: Long): Flow<List<StationEntity>> {
        return stationDao.getStationsByLineFlow(lineId)
    }
    
    /**
     * Get stations by type
     */
    fun getStationsByType(type: String): Flow<List<StationEntity>> {
        return stationDao.getStationsByTypeFlow(type)
    }
    
    /**
     * Search stations
     */
    fun searchStations(query: String): Flow<List<StationEntity>> {
        return stationDao.searchStationsFlow(query)
    }
    
    /**
     * Get transfer/interchange stations
     */
    fun getTransferStations(): Flow<List<StationEntity>> {
        return stationDao.getTransferStationsFlow()
    }
    
    /**
     * Get station by stop code
     */
    suspend fun getStationByStopCode(stopCode: String): StationEntity? {
        return stationDao.getStationByStopCode(stopCode)
    }
    
    /**
     * Insert or update a station
     */
    suspend fun upsertStation(station: StationEntity): Long {
        return stationDao.insertStation(station)
    }
    
    /**
     * Insert or update multiple stations
     */
    suspend fun upsertStations(stations: List<StationEntity>): List<Long> {
        return stationDao.insertStations(stations)
    }
    
    // === Data Migration ===
    
    /**
     * Sync database with in-memory TransitLineRepository
     * This converts TransitLine objects to TransitLineEntity
     */
    suspend fun syncFromInMemoryRepository() {
        val inMemoryLines = InMemoryRepository.getAllLines()
        val entities = inMemoryLines.map { transitLine ->
            convertToEntity(transitLine)
        }
        transitLineDao.insertLines(entities)
    }
    
    /**
     * Convert TransitLine to TransitLineEntity
     */
    private fun convertToEntity(transitLine: TransitLine): TransitLineEntity {
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
        
        return TransitLineEntity(
            lineNumber = transitLine.lineNumber,
            category = category,
            displayName = transitLine.displayName,
            routeCount = transitLine.routeIds.size,
            routeIds = transitLine.routeIds.joinToString(","),
            color = color,
            isActive = true
        )
    }
    
    /**
     * Convert TransitLineEntity back to TransitLine for compatibility
     */
    fun convertToTransitLine(entity: TransitLineEntity): TransitLine {
        val category = when (entity.category) {
            "bus" -> "buses"
            "trolley" -> "trolleys"
            else -> entity.category
        }
        
        return TransitLine(
            lineNumber = entity.lineNumber,
            category = category,
            routeIds = entity.routeIds.split(",").filter { it.isNotBlank() },
            routePaths = emptyList() // Route paths would need to be reconstructed
        )
    }
    
    // === Statistics ===
    
    /**
     * Get database statistics
     */
    suspend fun getStatistics(): DatabaseStatistics {
        return DatabaseStatistics(
            totalLines = transitLineDao.getTotalLineCount(),
            totalBuses = transitLineDao.getLineCountByCategory("bus"),
            totalTrolleys = transitLineDao.getLineCountByCategory("trolley"),
            totalMetro = transitLineDao.getLineCountByCategory("metro"),
            totalTram = transitLineDao.getLineCountByCategory("tram"),
            totalSuburban = transitLineDao.getLineCountByCategory("suburban"),
            totalStations = stationDao.getTotalStationCount(),
            totalBusStops = stationDao.getStationCountByType(StationType.BUS_STOP),
            totalTrolleyStops = stationDao.getStationCountByType(StationType.TROLLEY_STOP),
            totalMetroStations = stationDao.getStationCountByType(StationType.METRO_STATION),
            totalTramStops = stationDao.getStationCountByType(StationType.TRAM_STOP),
            totalSuburbanStations = stationDao.getStationCountByType(StationType.SUBURBAN_STATION),
            transferStations = stationDao.getTransferStationCount()
        )
    }
}

/**
 * Database statistics data class
 */
data class DatabaseStatistics(
    val totalLines: Int,
    val totalBuses: Int,
    val totalTrolleys: Int,
    val totalMetro: Int,
    val totalTram: Int,
    val totalSuburban: Int,
    val totalStations: Int,
    val totalBusStops: Int,
    val totalTrolleyStops: Int,
    val totalMetroStations: Int,
    val totalTramStops: Int,
    val totalSuburbanStations: Int,
    val transferStations: Int
)
