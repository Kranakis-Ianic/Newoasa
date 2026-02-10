package com.example.newoasa.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newoasa.database.room.entities.TransitLineEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for TransitLine operations
 * Provides CRUD operations for transit lines in the database
 */
@Dao
interface TransitLineDao {
    
    /**
     * Get all transit lines
     */
    @Query("SELECT * FROM transit_lines ORDER BY lineNumber ASC")
    fun getAllLines(): Flow<List<TransitLineEntity>>
    
    /**
     * Get a specific transit line by ID
     */
    @Query("SELECT * FROM transit_lines WHERE lineId = :lineId")
    suspend fun getLineById(lineId: String): TransitLineEntity?
    
    /**
     * Get all active transit lines
     */
    @Query("SELECT * FROM transit_lines WHERE isActive = 1 ORDER BY lineNumber ASC")
    fun getActiveLines(): Flow<List<TransitLineEntity>>
    
    /**
     * Get lines by transport type
     */
    @Query("SELECT * FROM transit_lines WHERE transportType = :type ORDER BY lineNumber ASC")
    fun getLinesByType(type: String): Flow<List<TransitLineEntity>>
    
    /**
     * Search lines by number or name
     */
    @Query(
        """SELECT * FROM transit_lines 
           WHERE lineNumber LIKE '%' || :query || '%' 
           OR displayName LIKE '%' || :query || '%'
           ORDER BY lineNumber ASC"""
    )
    fun searchLines(query: String): Flow<List<TransitLineEntity>>
    
    /**
     * Insert a new transit line
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLine(line: TransitLineEntity)
    
    /**
     * Insert multiple transit lines
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(lines: List<TransitLineEntity>)
    
    /**
     * Update an existing transit line
     */
    @Update
    suspend fun updateLine(line: TransitLineEntity)
    
    /**
     * Delete a transit line
     */
    @Delete
    suspend fun deleteLine(line: TransitLineEntity)
    
    /**
     * Delete all transit lines
     */
    @Query("DELETE FROM transit_lines")
    suspend fun deleteAllLines()
    
    /**
     * Get count of all lines
     */
    @Query("SELECT COUNT(*) FROM transit_lines")
    suspend fun getLineCount(): Int
}
