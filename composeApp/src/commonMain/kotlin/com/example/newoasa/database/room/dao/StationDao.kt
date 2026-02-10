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
    @Query("SELECT * FROM stations ORDER BY stationName ASC")
    fun getAll(): Flow<List<StationEntity>>
    
    /**
     * Get a specific station by ID
     */
    @Query("SELECT * FROM stations WHERE id = :id")
    suspend fun getById(id: Long): StationEntity?
    
    /**
     * Get a specific station by station code
     */
    @Query("SELECT * FROM stations WHERE stationCode = :stationCode")
    suspend fun getByStationCode(stationCode: String): StationEntity?
    
    /**
     * Get all stations for a specific line
     */
    @Query("SELECT * FROM stations WHERE lineCode = :lineCode ORDER BY `order` ASC")
    fun getByLineCode(lineCode: String): Flow<List<StationEntity>>
    
    /**
     * Get all stations by transport type
     */
    @Query("SELECT * FROM stations WHERE transportType = :type ORDER BY stationName ASC")
    fun getByTransportType(type: String): Flow<List<StationEntity>>
    
    /**
     * Search stations by name
     */
    @Query(
        """SELECT * FROM stations 
           WHERE stationName LIKE :query 
           OR stationCode LIKE :query
           ORDER BY stationName ASC"""
    )
    fun search(query: String): Flow<List<StationEntity>>
    
    /**
     * Get nearby stations within a certain radius
     * Note: This is a simple lat/lon box search, not true distance calculation
     */
    @Query(
        """SELECT * FROM stations 
           WHERE latitude BETWEEN :minLat AND :maxLat 
           AND longitude BETWEEN :minLon AND :maxLon
           ORDER BY stationName ASC"""
    )
    fun getInArea(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<StationEntity>>
    
    /**
     * Insert a new station
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: StationEntity)
    
    /**
     * Insert multiple stations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationEntity>)
    
    /**
     * Update an existing station
     */
    @Update
    suspend fun update(station: StationEntity)
    
    /**
     * Delete a station
     */
    @Delete
    suspend fun delete(station: StationEntity)
    
    /**
     * Delete all stations for a specific line
     */
    @Query("DELETE FROM stations WHERE lineCode = :lineCode")
    suspend fun deleteByLineCode(lineCode: String)
    
    /**
     * Delete all stations
     */
    @Query("DELETE FROM stations")
    suspend fun deleteAll()
    
    /**
     * Get count of all stations
     */
    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getCount(): Int
    
    /**
     * Get count of stations for a specific line
     */
    @Query("SELECT COUNT(*) FROM stations WHERE lineCode = :lineCode")
    suspend fun getCountByLineCode(lineCode: String): Int
}
