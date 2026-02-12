package com.example.newoasa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newoasa.data.local.entities.TransitLineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransitLineDao {
    // Matches @ColumnInfo(name = "line_id")
    @Query("SELECT * FROM transit_lines")
    fun getAllLines(): Flow<List<TransitLineEntity>>

    @Query("SELECT * FROM transit_lines WHERE category = :category")
    fun getLinesByCategory(category: String): Flow<List<TransitLineEntity>>

    @Query("SELECT * FROM transit_lines WHERE line_id = :lineId")
    suspend fun getLineById(lineId: String): TransitLineEntity?

    @Query("SELECT * FROM transit_lines WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchLines(query: String): Flow<List<TransitLineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(line: TransitLineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lines: List<TransitLineEntity>)

    @Query("DELETE FROM transit_lines")
    suspend fun deleteAll()
}