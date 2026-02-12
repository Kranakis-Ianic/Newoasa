package com.example.newoasa.database

import com.example.newoasa.data.models.Station
import com.example.newoasa.data.models.TransitLine
import kotlinx.coroutines.flow.Flow

/**
 * Common interface for transit database operations
 * Implemented differently on each platform (expect/actual pattern)
 */
interface ITransitDatabase {
    
    // === Transit Lines ===
    
    suspend fun insertLine(line: TransitLine): Long
    suspend fun insertLines(lines: List<TransitLine>): List<Long>
    
    suspend fun getLineById(id: Long): TransitLine?
    fun getLineByIdFlow(id: Long): Flow<TransitLine?>
    
    suspend fun getAllLines(): List<TransitLine>
    fun getAllLinesFlow(): Flow<List<TransitLine>>
    
    suspend fun getLinesByCategory(category: String): List<TransitLine>
    fun getLinesByCategoryFlow(category: String): Flow<List<TransitLine>>
    
    suspend fun searchLines(query: String): List<TransitLine>
    fun searchLinesFlow(query: String): Flow<List<TransitLine>>
    
    suspend fun updateLine(line: TransitLine)
    suspend fun deleteLine(line: TransitLine)
    suspend fun deleteAllLines()
    
    suspend fun getTotalLineCount(): Int
    
    // === Stations ===
    
    suspend fun insertStation(station: Station): Long
    suspend fun insertStations(stations: List<Station>): List<Long>
    
    suspend fun getStationById(id: Long): Station?
    fun getStationByIdFlow(id: Long): Flow<Station?>
    
    suspend fun getStationByStopCode(stopCode: String): Station?
    
    suspend fun getStationsByLine(lineId: Long): List<Station>
    fun getStationsByLineFlow(lineId: Long): Flow<List<Station>>
    
    suspend fun getStationsByType(type: String): List<Station>
    fun getStationsByTypeFlow(type: String): Flow<List<Station>>
    
    suspend fun getAllStations(): List<Station>
    fun getAllStationsFlow(): Flow<List<Station>>
    
    suspend fun searchStations(query: String): List<Station>
    fun searchStationsFlow(query: String): Flow<List<Station>>
    
    suspend fun getTransferStations(): List<Station>
    fun getTransferStationsFlow(): Flow<List<Station>>
    
    suspend fun updateStation(station: Station)
    suspend fun deleteStation(station: Station)
    suspend fun deleteAllStations()
    
    suspend fun getTotalStationCount(): Int
}

/**
 * Database statistics
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

/**
 * Expect function to get platform-specific database instance
 */
expect fun getTransitDatabase(): ITransitDatabase
