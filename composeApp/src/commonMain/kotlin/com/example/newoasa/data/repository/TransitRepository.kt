package com.example.newoasa.data.repository

import com.example.newoasa.data.local.dao.StationDao
import com.example.newoasa.data.local.dao.TransitLineDao
import com.example.newoasa.data.local.entities.StationEntity
import com.example.newoasa.data.local.entities.TransitLineEntity
import kotlinx.coroutines.flow.Flow

/**
 * Common repository for accessing transit data
 * Works across all platforms using the expect/actual database
 */
class TransitRepository(
    private val stationDao: StationDao,
    private val transitLineDao: TransitLineDao
) {

    // === Transit Lines ===

    fun getAllLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getAllLinesFlow()
    }

    fun getLinesByCategory(category: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow(category)
    }

    fun getBusLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow("bus")
    }

    fun getTrolleyLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow("trolley")
    }

    fun getMetroLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow("metro")
    }

    fun getTramLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategoryFlow("tram")
    }

    fun searchLines(query: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.searchLinesFlow(query)
    }

    suspend fun getLineById(id: Long): TransitLineEntity? {
        return transitLineDao.getLineById(id)
    }

    suspend fun insertLine(line: TransitLineEntity): Long {
        return transitLineDao.insertLine(line)
    }

    suspend fun insertLines(lines: List<TransitLineEntity>): List<Long> {
        return transitLineDao.insertLines(lines)
    }

    // === Stations ===

    fun getAllStations(): Flow<List<StationEntity>> {
        return stationDao.getAllStationsFlow()
    }

    fun getStationsByLine(lineId: Long): Flow<List<StationEntity>> {
        return stationDao.getStationsByLineFlow(lineId)
    }

    fun getStationsByType(type: String): Flow<List<StationEntity>> {
        return stationDao.getStationsByTypeFlow(type)
    }

    fun searchStations(query: String): Flow<List<StationEntity>> {
        return stationDao.searchStationsFlow(query)
    }

    fun getTransferStations(): Flow<List<StationEntity>> {
        return stationDao.getTransferStationsFlow()
    }

    suspend fun getStationByStopCode(stopCode: String): StationEntity? {
        return stationDao.getStationByStopCode(stopCode)
    }

    suspend fun insertStation(station: StationEntity): Long {
        return stationDao.insertStation(station)
    }

    suspend fun insertStations(stations: List<StationEntity>): List<Long> {
        return stationDao.insertStations(stations)
    }

    // === Statistics ===

    suspend fun getStatistics(): DatabaseStatistics {
        return DatabaseStatistics(
            totalLines = transitLineDao.getTotalLineCount(),
            totalBuses = transitLineDao.getLinesByCategory("bus").size,
            totalTrolleys = transitLineDao.getLinesByCategory("trolley").size,
            totalMetro = transitLineDao.getLinesByCategory("metro").size,
            totalTram = transitLineDao.getLinesByCategory("tram").size,
            totalSuburban = transitLineDao.getLinesByCategory("suburban").size,
            totalStations = stationDao.getTotalStationCount(),
            totalBusStops = stationDao.getStationsByType(StationType.BUS_STOP).size,
            totalTrolleyStops = stationDao.getStationsByType(StationType.TROLLEY_STOP).size,
            totalMetroStations = stationDao.getStationsByType(StationType.METRO_STATION).size,
            totalTramStops = stationDao.getStationsByType(StationType.TRAM_STOP).size,
            totalSuburbanStations = stationDao.getStationsByType(StationType.SUBURBAN_STATION).size,
            transferStations = stationDao.getTransferStations().size
        )
    }
}