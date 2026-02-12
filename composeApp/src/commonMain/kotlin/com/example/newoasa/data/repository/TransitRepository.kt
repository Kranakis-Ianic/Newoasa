package com.example.newoasa.data.repository

import com.example.newoasa.database.DatabaseStatistics
import com.example.newoasa.database.ITransitDatabase
import com.example.newoasa.database.getTransitDatabase
import com.example.newoasa.data.models.Station
import com.example.newoasa.data.models.StationType
import com.example.newoasa.data.models.TransitLine
import kotlinx.coroutines.flow.Flow

/**
 * Common repository for accessing transit data
 * Works across all platforms using the expect/actual database
 */
class TransitRepository(
    private val database: ITransitDatabase = getTransitDatabase()
) {

    // === Transit Lines ===

    fun getAllLines(): Flow<List<TransitLine>> {
        return database.getAllLinesFlow()
    }

    fun getLinesByCategory(category: String): Flow<List<TransitLine>> {
        return database.getLinesByCategoryFlow(category)
    }

    fun getBusLines(): Flow<List<TransitLine>> {
        return database.getLinesByCategoryFlow("bus")
    }

    fun getTrolleyLines(): Flow<List<TransitLine>> {
        return database.getLinesByCategoryFlow("trolley")
    }

    fun getMetroLines(): Flow<List<TransitLine>> {
        return database.getLinesByCategoryFlow("metro")
    }

    fun getTramLines(): Flow<List<TransitLine>> {
        return database.getLinesByCategoryFlow("tram")
    }

    fun searchLines(query: String): Flow<List<TransitLine>> {
        return database.searchLinesFlow(query)
    }

    suspend fun getLineById(id: Long): TransitLine? {
        return database.getLineById(id)
    }

    suspend fun insertLine(line: TransitLine): Long {
        return database.insertLine(line)
    }

    suspend fun insertLines(lines: List<TransitLine>): List<Long> {
        return database.insertLines(lines)
    }

    // === Stations ===

    fun getAllStations(): Flow<List<Station>> {
        return database.getAllStationsFlow()
    }

    fun getStationsByLine(lineId: Long): Flow<List<Station>> {
        return database.getStationsByLineFlow(lineId)
    }

    fun getStationsByType(type: String): Flow<List<Station>> {
        return database.getStationsByTypeFlow(type)
    }

    fun searchStations(query: String): Flow<List<Station>> {
        return database.searchStationsFlow(query)
    }

    fun getTransferStations(): Flow<List<Station>> {
        return database.getTransferStationsFlow()
    }

    suspend fun getStationByStopCode(stopCode: String): Station? {
        return database.getStationByStopCode(stopCode)
    }

    suspend fun insertStation(station: Station): Long {
        return database.insertStation(station)
    }

    suspend fun insertStations(stations: List<Station>): List<Long> {
        return database.insertStations(stations)
    }

    // === Statistics ===

    suspend fun getStatistics(): DatabaseStatistics {
        return DatabaseStatistics(
            totalLines = database.getTotalLineCount(),
            totalBuses = database.getLinesByCategory("bus").size,
            totalTrolleys = database.getLinesByCategory("trolley").size,
            totalMetro = database.getLinesByCategory("metro").size,
            totalTram = database.getLinesByCategory("tram").size,
            totalSuburban = database.getLinesByCategory("suburban").size,
            totalStations = database.getTotalStationCount(),
            totalBusStops = database.getStationsByType(StationType.BUS_STOP).size,
            totalTrolleyStops = database.getStationsByType(StationType.TROLLEY_STOP).size,
            totalMetroStations = database.getStationsByType(StationType.METRO_STATION).size,
            totalTramStops = database.getStationsByType(StationType.TRAM_STOP).size,
            totalSuburbanStations = database.getStationsByType(StationType.SUBURBAN_STATION).size,
            transferStations = database.getTransferStations().size
        )
    }
}