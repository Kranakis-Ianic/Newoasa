package com.example.newoasa.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newoasa.database.entities.MetroLineEntity
import com.example.newoasa.database.entities.MetroStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetroLineDao {
    @Query("SELECT * FROM metro_lines WHERE isActive = 1")
    fun getAllActiveLines(): Flow<List<MetroLineEntity>>
    
    @Query("SELECT * FROM metro_lines WHERE lineId = :lineId")
    suspend fun getLineById(lineId: String): MetroLineEntity?
    
    @Query("SELECT * FROM metro_lines WHERE lineNumber = :lineNumber")
    suspend fun getLineByNumber(lineNumber: String): MetroLineEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLine(line: MetroLineEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(lines: List<MetroLineEntity>)
    
    @Update
    suspend fun updateLine(line: MetroLineEntity)
    
    @Delete
    suspend fun deleteLine(line: MetroLineEntity)
    
    @Query("DELETE FROM metro_lines")
    suspend fun deleteAllLines()
}

@Dao
interface MetroStationDao {
    @Query("SELECT * FROM metro_stations WHERE lineId = :lineId ORDER BY `order` ASC")
    fun getStationsByLine(lineId: String): Flow<List<MetroStationEntity>>
    
    @Query("SELECT * FROM metro_stations WHERE stationId = :stationId")
    suspend fun getStationById(stationId: String): MetroStationEntity?
    
    @Query("SELECT * FROM metro_stations WHERE stopCode = :stopCode")
    suspend fun getStationByCode(stopCode: String): MetroStationEntity?
    
    @Query("SELECT * FROM metro_stations WHERE isAccessible = 1")
    fun getAccessibleStations(): Flow<List<MetroStationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: MetroStationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<MetroStationEntity>)
    
    @Update
    suspend fun updateStation(station: MetroStationEntity)
    
    @Delete
    suspend fun deleteStation(station: MetroStationEntity)
    
    @Query("DELETE FROM metro_stations WHERE lineId = :lineId")
    suspend fun deleteStationsByLine(lineId: String)
    
    @Query("DELETE FROM metro_stations")
    suspend fun deleteAllStations()
}
