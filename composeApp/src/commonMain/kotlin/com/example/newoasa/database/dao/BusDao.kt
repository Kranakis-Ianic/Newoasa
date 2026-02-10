package com.example.newoasa.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newoasa.database.entities.BusLineEntity
import com.example.newoasa.database.entities.BusStopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusLineDao {
    @Query("SELECT * FROM bus_lines WHERE isActive = 1")
    fun getAllActiveLines(): Flow<List<BusLineEntity>>
    
    @Query("SELECT * FROM bus_lines WHERE lineId = :lineId")
    suspend fun getLineById(lineId: String): BusLineEntity?
    
    @Query("SELECT * FROM bus_lines WHERE lineNumber = :lineNumber")
    suspend fun getLineByNumber(lineNumber: String): BusLineEntity?
    
    @Query("SELECT * FROM bus_lines WHERE isExpress = 1 AND isActive = 1")
    fun getExpressLines(): Flow<List<BusLineEntity>>
    
    @Query("SELECT * FROM bus_lines WHERE isNightService = 1 AND isActive = 1")
    fun getNightServiceLines(): Flow<List<BusLineEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLine(line: BusLineEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(lines: List<BusLineEntity>)
    
    @Update
    suspend fun updateLine(line: BusLineEntity)
    
    @Delete
    suspend fun deleteLine(line: BusLineEntity)
    
    @Query("DELETE FROM bus_lines")
    suspend fun deleteAllLines()
}

@Dao
interface BusStopDao {
    @Query("SELECT * FROM bus_stops WHERE lineId = :lineId ORDER BY `order` ASC")
    fun getStopsByLine(lineId: String): Flow<List<BusStopEntity>>
    
    @Query("SELECT * FROM bus_stops WHERE stopId = :stopId")
    suspend fun getStopById(stopId: String): BusStopEntity?
    
    @Query("SELECT * FROM bus_stops WHERE stopCode = :stopCode")
    suspend fun getStopByCode(stopCode: String): BusStopEntity?
    
    @Query("SELECT * FROM bus_stops WHERE hasRealTimeInfo = 1")
    fun getStopsWithRealTimeInfo(): Flow<List<BusStopEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: BusStopEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<BusStopEntity>)
    
    @Update
    suspend fun updateStop(stop: BusStopEntity)
    
    @Delete
    suspend fun deleteStop(stop: BusStopEntity)
    
    @Query("DELETE FROM bus_stops WHERE lineId = :lineId")
    suspend fun deleteStopsByLine(lineId: String)
    
    @Query("DELETE FROM bus_stops")
    suspend fun deleteAllStops()
}
