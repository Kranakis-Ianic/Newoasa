package com.example.newoasa.data.repository

import com.example.newoasa.data.local.dao.StationDao
import com.example.newoasa.data.local.dao.TransitLineDao
import com.example.newoasa.data.local.entities.StationEntity
import com.example.newoasa.data.local.entities.TransitLineEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

// Define the missing data class here or in a separate file
data class DatabaseStatistics(
    val totalLines: Int,
    val totalStations: Int,
    val busLines: Int,
    val trolleyLines: Int,
    val metroLines: Int,
    val tramLines: Int,
    val suburbanLines: Int,
    val otherLines: Int,
    val busStations: Int,
    val trolleyStations: Int,
    val metroStations: Int,
    val tramStations: Int,
    val suburbanStations: Int,
    val otherStations: Int,
    val transferStations: Int
)

class TransitRepository(
    private val stationDao: StationDao,
    private val transitLineDao: TransitLineDao
) {
    // ----------------------------------------------------------------
    // TRANSIT LINES
    // ----------------------------------------------------------------

    fun getAllLines(): Flow<List<TransitLineEntity>> {
        return transitLineDao.getAllLines() // Changed from getAllLinesFlow
    }

    fun getLinesByCategory(category: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.getLinesByCategory(category) // Changed from getLinesByCategoryFlow
    }

    fun searchLines(query: String): Flow<List<TransitLineEntity>> {
        return transitLineDao.searchLines(query) // Changed from searchLinesFlow
    }

    suspend fun getLineById(lineId: String): TransitLineEntity? {
        return transitLineDao.getLineById(lineId)
    }

    suspend fun insertLine(line: TransitLineEntity) {
        transitLineDao.insert(line) // Changed from insertLine
    }

    suspend fun insertLines(lines: List<TransitLineEntity>) {
        transitLineDao.insertAll(lines) // Changed from insertLines
    }

    suspend fun deleteAllLines() {
        transitLineDao.deleteAll()
    }

    // ----------------------------------------------------------------
    // STATIONS
    // ----------------------------------------------------------------

    fun getAllStations(): Flow<List<StationEntity>> {
        return stationDao.getAllStations() // Changed from getAllStationsFlow
    }

    fun getStationsByLine(lineId: String): Flow<List<StationEntity>> {
        return stationDao.getStationsByLine(lineId) // Changed from getStationsByLineFlow
    }

    fun getStationsByType(type: String): Flow<List<StationEntity>> {
        return stationDao.getStationsByType(type) // Changed from getStationsByTypeFlow
    }

    fun searchStations(query: String): Flow<List<StationEntity>> {
        return stationDao.searchStations(query) // Changed from searchStationsFlow
    }

    suspend fun getStationByStopCode(stopCode: String): StationEntity? {
        return stationDao.getStationByStopCode(stopCode)
    }

    suspend fun insertStation(station: StationEntity) {
        stationDao.insert(station) // Changed from insertStation
    }

    suspend fun insertStations(stations: List<StationEntity>) {
        stationDao.insertAll(stations) // Changed from insertStations
    }

    suspend fun deleteAllStations() {
        stationDao.deleteAll()
    }

    // ----------------------------------------------------------------
    // STATISTICS
    // ----------------------------------------------------------------

    suspend fun getDatabaseStatistics(): DatabaseStatistics {
        // We use .first() to get the current value from the Flow
        val lines = transitLineDao.getAllLines().first()
        val stations = stationDao.getAllStations().first()

        return DatabaseStatistics(
            totalLines = lines.size,
            totalStations = stations.size,
            busLines = lines.count { it.category == "Bus lines" },
            trolleyLines = lines.count { it.category == "Trolley lines" },
            metroLines = lines.count { it.category == "Metro lines" },
            tramLines = lines.count { it.category == "Tram lines" },
            suburbanLines = lines.count { it.category == "Suburban Railway lines" },
            otherLines = lines.count { it.category == "Other" },
            busStations = stations.count { it.lineCategory == "Bus lines" },
            trolleyStations = stations.count { it.lineCategory == "Trolley lines" },
            metroStations = stations.count { it.lineCategory == "Metro lines" },
            tramStations = stations.count { it.lineCategory == "Tram lines" },
            suburbanStations = stations.count { it.lineCategory == "Suburban Railway lines" },
            otherStations = stations.count { it.lineCategory == "Other" },
            // Estimate transfer stations by checking duplicates or specific logic
            transferStations = 0 // Placeholder logic
        )
    }
}