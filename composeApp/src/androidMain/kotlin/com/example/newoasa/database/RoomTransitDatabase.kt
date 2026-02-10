package com.example.newoasa.database

import android.content.Context
import com.example.newoasa.database.models.Station
import com.example.newoasa.database.models.TransitLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Android implementation of ITransitDatabase using Room
 * Adapts Room database to common interface
 */
class RoomTransitDatabase(
    context: Context
) : ITransitDatabase {
    
    private val roomDb = TransitDatabase.getInstance(context)
    private val lineDao = roomDb.transitLineDao()
    private val stationDao = roomDb.stationDao()
    
    // === Transit Lines ===
    
    override suspend fun insertLine(line: TransitLine): Long {
        return lineDao.insertLine(line.toEntity())
    }
    
    override suspend fun insertLines(lines: List<TransitLine>): List<Long> {
        return lineDao.insertLines(lines.map { it.toEntity() })
    }
    
    override suspend fun getLineById(id: Long): TransitLine? {
        return lineDao.getLineById(id)?.toModel()
    }
    
    override fun getLineByIdFlow(id: Long): Flow<TransitLine?> {
        return lineDao.getLineByIdFlow(id).map { it?.toModel() }
    }
    
    override suspend fun getAllLines(): List<TransitLine> {
        return lineDao.getAllLines().map { it.toModel() }
    }
    
    override fun getAllLinesFlow(): Flow<List<TransitLine>> {
        return lineDao.getAllLinesFlow().map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun getLinesByCategory(category: String): List<TransitLine> {
        return lineDao.getLinesByCategory(category).map { it.toModel() }
    }
    
    override fun getLinesByCategoryFlow(category: String): Flow<List<TransitLine>> {
        return lineDao.getLinesByCategoryFlow(category).map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun searchLines(query: String): List<TransitLine> {
        return lineDao.searchLines(query).map { it.toModel() }
    }
    
    override fun searchLinesFlow(query: String): Flow<List<TransitLine>> {
        return lineDao.searchLinesFlow(query).map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun updateLine(line: TransitLine) {
        lineDao.updateLine(line.toEntity())
    }
    
    override suspend fun deleteLine(line: TransitLine) {
        lineDao.deleteLine(line.toEntity())
    }
    
    override suspend fun deleteAllLines() {
        lineDao.deleteAllLines()
    }
    
    override suspend fun getTotalLineCount(): Int {
        return lineDao.getTotalLineCount()
    }
    
    // === Stations ===
    
    override suspend fun insertStation(station: Station): Long {
        return stationDao.insertStation(station.toEntity())
    }
    
    override suspend fun insertStations(stations: List<Station>): List<Long> {
        return stationDao.insertStations(stations.map { it.toEntity() })
    }
    
    override suspend fun getStationById(id: Long): Station? {
        return stationDao.getStationById(id)?.toModel()
    }
    
    override fun getStationByIdFlow(id: Long): Flow<Station?> {
        return stationDao.getStationByIdFlow(id).map { it?.toModel() }
    }
    
    override suspend fun getStationByStopCode(stopCode: String): Station? {
        return stationDao.getStationByStopCode(stopCode)?.toModel()
    }
    
    override suspend fun getStationsByLine(lineId: Long): List<Station> {
        return stationDao.getStationsByLine(lineId).map { it.toModel() }
    }
    
    override fun getStationsByLineFlow(lineId: Long): Flow<List<Station>> {
        return stationDao.getStationsByLineFlow(lineId).map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun getStationsByType(type: String): List<Station> {
        return stationDao.getStationsByType(type).map { it.toModel() }
    }
    
    override fun getStationsByTypeFlow(type: String): Flow<List<Station>> {
        return stationDao.getStationsByTypeFlow(type).map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun getAllStations(): List<Station> {
        return stationDao.getAllStations().map { it.toModel() }
    }
    
    override fun getAllStationsFlow(): Flow<List<Station>> {
        return stationDao.getAllStationsFlow().map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun searchStations(query: String): List<Station> {
        return stationDao.searchStations(query).map { it.toModel() }
    }
    
    override fun searchStationsFlow(query: String): Flow<List<Station>> {
        return stationDao.searchStationsFlow(query).map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun getTransferStations(): List<Station> {
        return stationDao.getTransferStations().map { it.toModel() }
    }
    
    override fun getTransferStationsFlow(): Flow<List<Station>> {
        return stationDao.getTransferStationsFlow().map { list -> list.map { it.toModel() } }
    }
    
    override suspend fun updateStation(station: Station) {
        stationDao.updateStation(station.toEntity())
    }
    
    override suspend fun deleteStation(station: Station) {
        stationDao.deleteStation(station.toEntity())
    }
    
    override suspend fun deleteAllStations() {
        stationDao.deleteAllStations()
    }
    
    override suspend fun getTotalStationCount(): Int {
        return stationDao.getTotalStationCount()
    }
}
