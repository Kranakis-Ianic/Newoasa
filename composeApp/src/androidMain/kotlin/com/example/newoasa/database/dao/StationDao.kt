package com.example.newoasa.database.dao

import androidx.room.*
import com.example.newoasa.database.entities.StationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for stations/stops
 */
@Dao
interface StationDao {
    
    // === Create operations ===
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: StationEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>): List<Long>
    
    // === Read operations ===
    
    @Query("SELECT * FROM stations WHERE id = :stationId")
    suspend fun getStationById(stationId: Long): StationEntity?
    
    @Query("SELECT * FROM stations WHERE id = :stationId")
    fun getStationByIdFlow(stationId: Long): Flow<StationEntity?>
    
    @Query("SELECT * FROM stations WHERE stopCode = :stopCode")
    suspend fun getStationByStopCode(stopCode: String): StationEntity?
    
    @Query("SELECT * FROM stations WHERE stopCode = :stopCode")
    fun getStationByStopCodeFlow(stopCode: String): Flow<StationEntity?>
    
    @Query("SELECT * FROM stations WHERE lineId = :lineId ORDER BY orderOnLine")
    suspend fun getStationsByLine(lineId: Long): List<StationEntity>
    
    @Query("SELECT * FROM stations WHERE lineId = :lineId ORDER BY orderOnLine")
    fun getStationsByLineFlow(lineId: Long): Flow<List<StationEntity>>
    
    @Query("SELECT * FROM stations WHERE stationType = :type ORDER BY name")
    suspend fun getStationsByType(type: String): List<StationEntity>
    
    @Query("SELECT * FROM stations WHERE stationType = :type ORDER BY name")
    fun getStationsByTypeFlow(type: String): Flow<List<StationEntity>>
    
    @Query("SELECT * FROM stations ORDER BY name")
    suspend fun getAllStations(): List<StationEntity>
    
    @Query("SELECT * FROM stations ORDER BY name")
    fun getAllStationsFlow(): Flow<List<StationEntity>>
    
    @Query("""
        SELECT * FROM stations 
        WHERE name LIKE '%' || :query || '%' 
        OR stopCode LIKE '%' || :query || '%'
        OR address LIKE '%' || :query || '%'
        ORDER BY name
    """)
    suspend fun searchStations(query: String): List<StationEntity>
    
    @Query("""
        SELECT * FROM stations 
        WHERE name LIKE '%' || :query || '%' 
        OR stopCode LIKE '%' || :query || '%'
        OR address LIKE '%' || :query || '%'
        ORDER BY name
    """)
    fun searchStationsFlow(query: String): Flow<List<StationEntity>>
    
    @Query("SELECT * FROM stations WHERE isTransferStation = 1 ORDER BY name")
    suspend fun getTransferStations(): List<StationEntity>
    
    @Query("SELECT * FROM stations WHERE isTransferStation = 1 ORDER BY name")
    fun getTransferStationsFlow(): Flow<List<StationEntity>>
    
    @Query("""
        SELECT * FROM stations 
        WHERE stationType IN ('metro_station', 'suburban_station')
        AND hasAccessibility = 1
        ORDER BY name
    """)
    suspend fun getAccessibleStations(): List<StationEntity>
    
    @Query("""
        SELECT * FROM stations 
        WHERE latitude BETWEEN :minLat AND :maxLat 
        AND longitude BETWEEN :minLng AND :maxLng
        ORDER BY name
    """)
    suspend fun getStationsInBounds(
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double
    ): List<StationEntity>
    
    @Query("""
        SELECT * FROM stations 
        WHERE isActive = 1
        ORDER BY name
    """)
    suspend fun getActiveStations(): List<StationEntity>
    
    @Query("""
        SELECT * FROM stations 
        WHERE isActive = 1
        ORDER BY name
    """)
    fun getActiveStationsFlow(): Flow<List<StationEntity>>
    
    // === Update operations ===
    
    @Update
    suspend fun updateStation(station: StationEntity)
    
    @Update
    suspend fun updateStations(stations: List<StationEntity>)
    
    @Query("UPDATE stations SET isActive = :isActive WHERE id = :stationId")
    suspend fun updateStationActiveStatus(stationId: Long, isActive: Boolean)
    
    @Query("UPDATE stations SET updatedAt = :timestamp WHERE id = :stationId")
    suspend fun updateStationTimestamp(stationId: Long, timestamp: Long = System.currentTimeMillis())
    
    // === Delete operations ===
    
    @Delete
    suspend fun deleteStation(station: StationEntity)
    
    @Query("DELETE FROM stations WHERE id = :stationId")
    suspend fun deleteStationById(stationId: Long)
    
    @Query("DELETE FROM stations WHERE lineId = :lineId")
    suspend fun deleteStationsByLine(lineId: Long)
    
    @Query("DELETE FROM stations WHERE stationType = :type")
    suspend fun deleteStationsByType(type: String)
    
    @Query("DELETE FROM stations")
    suspend fun deleteAllStations()
    
    // === Statistics ===
    
    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getTotalStationCount(): Int
    
    @Query("SELECT COUNT(*) FROM stations WHERE stationType = :type")
    suspend fun getStationCountByType(type: String): Int
    
    @Query("SELECT COUNT(*) FROM stations WHERE lineId = :lineId")
    suspend fun getStationCountByLine(lineId: Long): Int
    
    @Query("SELECT COUNT(*) FROM stations WHERE isTransferStation = 1")
    suspend fun getTransferStationCount(): Int
    
    @Query("SELECT COUNT(*) FROM stations WHERE isActive = 1")
    suspend fun getActiveStationCount(): Int
}
