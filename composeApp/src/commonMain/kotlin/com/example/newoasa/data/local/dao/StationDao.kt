package com.example.newoasa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newoasa.data.local.entities.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations")
    fun getAllStations(): Flow<List<StationEntity>>

    // Matches @ColumnInfo(name = "line_id")
    @Query("SELECT * FROM stations WHERE line_id = :lineId")
    fun getStationsByLine(lineId: String): Flow<List<StationEntity>>

    // Matches @ColumnInfo(name = "line_category")
    @Query("SELECT * FROM stations WHERE line_category = :type")
    fun getStationsByType(type: String): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE name LIKE '%' || :query || '%'")
    fun searchStations(query: String): Flow<List<StationEntity>>

    // Matches @ColumnInfo(name = "stop_code")
    @Query("SELECT * FROM stations WHERE stop_code = :stopCode")
    suspend fun getStationByStopCode(stopCode: String): StationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: StationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationEntity>)

    @Query("DELETE FROM stations")
    suspend fun deleteAll()
}