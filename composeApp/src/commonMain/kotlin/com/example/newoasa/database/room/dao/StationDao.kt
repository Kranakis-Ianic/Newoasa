package com.example.newoasa.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newoasa.database.room.entities.StationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Station operations
 * Provides CRUD operations for stations/stops in the database
 */
@Dao
interface StationDao {
    
    /**
     * Get all stations
     */
    @Query("SELECT * FROM stations ORDER BY name ASC")
    fun getAllStations(): Flow<List<StationEntity>>
    
    /**
     * Get a specific station by ID
     */
    @Query("SELECT * FROM stations WHERE stationId = :stationId")
    suspend fun getStationById(stationId: String): StationEntity?
    
    /**
     * Get all stations for a specific line
     */
    @Query("SELECT * FROM stations WHERE lineId = :lineId ORDER BY stationOrder ASC")
    fun getStationsByLine(lineId: String): Flow<List<StationEntity>>
    
    /**
     * Search stations by name
     */
    @Query(
        """SELECT * FROM stations 
           WHERE name LIKE '%' || :query || '%' 
           OR stationCode LIKE '%' || :query || '%'
           ORDER BY name ASC"""
    )
    fun searchStations(query: String): Flow<List<StationEntity>>
    
    /**
     * Get nearby stations within a certain radius
     * Note: This is a simple lat/lon box search, not true distance calculation
     */
    @Query(
        """SELECT * FROM stations 
           WHERE latitude BETWEEN :minLat AND :maxLat 
           AND longitude BETWEEN :minLon AND :maxLon
           ORDER BY name ASC"""
    )
    fun getStationsInArea(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<StationEntity>>
    
    /**
     * Insert a new station
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: StationEntity)
    
    /**
     * Insert multiple stations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)
    
    /**
     * Update an existing station
     */
    @Update
    suspend fun updateStation(station: StationEntity)
    
    /**
     * Delete a station
     */
    @Delete
    suspend fun deleteStation(station: StationEntity)
    
    /**
     * Delete all stations for a specific line
     */
    @Query("DELETE FROM stations WHERE lineId = :lineId")
    suspend fun deleteStationsByLine(lineId: String)
    
    /**
     * Delete all stations
     */
    @Query("DELETE FROM stations")
    suspend fun deleteAllStations()
    
    /**
     * Get count of all stations
     */
    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getStationCount(): Int
    
    /**
     * Get count of stations for a specific line
     */
    @Query("SELECT COUNT(*) FROM stations WHERE lineId = :lineId")
    suspend fun getStationCountByLine(lineId: String): Int
}
