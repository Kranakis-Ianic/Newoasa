package com.example.newoasa.database

import com.example.newoasa.database.models.Station
import com.example.newoasa.database.models.TransitLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * iOS stub implementation of ITransitDatabase
 * TODO: Implement with SQLDelight or another iOS-compatible database
 */
class IosTransitDatabase : ITransitDatabase {
    
    override suspend fun insertLine(line: TransitLine): Long = 0L
    override suspend fun insertLines(lines: List<TransitLine>): List<Long> = emptyList()
    override suspend fun getLineById(id: Long): TransitLine? = null
    override fun getLineByIdFlow(id: Long): Flow<TransitLine?> = flowOf(null)
    override suspend fun getAllLines(): List<TransitLine> = emptyList()
    override fun getAllLinesFlow(): Flow<List<TransitLine>> = flowOf(emptyList())
    override suspend fun getLinesByCategory(category: String): List<TransitLine> = emptyList()
    override fun getLinesByCategoryFlow(category: String): Flow<List<TransitLine>> = flowOf(emptyList())
    override suspend fun searchLines(query: String): List<TransitLine> = emptyList()
    override fun searchLinesFlow(query: String): Flow<List<TransitLine>> = flowOf(emptyList())
    override suspend fun updateLine(line: TransitLine) {}
    override suspend fun deleteLine(line: TransitLine) {}
    override suspend fun deleteAllLines() {}
    override suspend fun getTotalLineCount(): Int = 0
    
    override suspend fun insertStation(station: Station): Long = 0L
    override suspend fun insertStations(stations: List<Station>): List<Long> = emptyList()
    override suspend fun getStationById(id: Long): Station? = null
    override fun getStationByIdFlow(id: Long): Flow<Station?> = flowOf(null)
    override suspend fun getStationByStopCode(stopCode: String): Station? = null
    override suspend fun getStationsByLine(lineId: Long): List<Station> = emptyList()
    override fun getStationsByLineFlow(lineId: Long): Flow<List<Station>> = flowOf(emptyList())
    override suspend fun getStationsByType(type: String): List<Station> = emptyList()
    override fun getStationsByTypeFlow(type: String): Flow<List<Station>> = flowOf(emptyList())
    override suspend fun getAllStations(): List<Station> = emptyList()
    override fun getAllStationsFlow(): Flow<List<Station>> = flowOf(emptyList())
    override suspend fun searchStations(query: String): List<Station> = emptyList()
    override fun searchStationsFlow(query: String): Flow<List<Station>> = flowOf(emptyList())
    override suspend fun getTransferStations(): List<Station> = emptyList()
    override fun getTransferStationsFlow(): Flow<List<Station>> = flowOf(emptyList())
    override suspend fun updateStation(station: Station) {}
    override suspend fun deleteStation(station: Station) {}
    override suspend fun deleteAllStations() {}
    override suspend fun getTotalStationCount(): Int = 0
}

/**
 * iOS actual implementation of getTransitDatabase
 */
actual fun getTransitDatabase(): ITransitDatabase {
    return IosTransitDatabase()
}
