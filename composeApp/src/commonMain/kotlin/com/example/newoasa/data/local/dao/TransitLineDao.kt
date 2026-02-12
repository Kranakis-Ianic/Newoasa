package com.example.newoasa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newoasa.data.local.entities.TransitLineEntity
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
    @Query("SELECT * FROM transit_lines ORDER BY lineCode ASC")
    fun getAll(): Flow<List<TransitLineEntity>>
    
    /**
     * Get a specific transit line by ID
     */
    @Query("SELECT * FROM transit_lines WHERE id = :id")
    suspend fun getById(id: Long): TransitLineEntity?
    
    /**
     * Get a specific transit line by line code
     */
    @Query("SELECT * FROM transit_lines WHERE lineCode = :lineCode")
    suspend fun getByLineCode(lineCode: String): TransitLineEntity?
    
    /**
     * Get lines by category
     */
    @Query("SELECT * FROM transit_lines WHERE category = :category ORDER BY lineCode ASC")
    fun getByCategory(category: String): Flow<List<TransitLineEntity>>
    
    /**
     * Get lines by transport type
     */
    @Query("SELECT * FROM transit_lines WHERE transportType = :type ORDER BY lineCode ASC")
    fun getByType(type: String): Flow<List<TransitLineEntity>>
    
    /**
     * Search lines by code or name
     */
    @Query(
        """SELECT * FROM transit_lines 
           WHERE lineCode LIKE :query 
           OR lineName LIKE :query
           ORDER BY lineCode ASC"""
    )
    fun search(query: String): Flow<List<TransitLineEntity>>
    
    /**
     * Insert a new transit line
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(line: TransitLineEntity)
    
    /**
     * Insert multiple transit lines
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lines: List<TransitLineEntity>)
    
    /**
     * Update an existing transit line
     */
    @Update
    suspend fun update(line: TransitLineEntity)
    
    /**
     * Delete a transit line
     */
    @Delete
    suspend fun delete(line: TransitLineEntity)
    
    /**
     * Delete all transit lines
     */
    @Query("DELETE FROM transit_lines")
    suspend fun deleteAll()
    
    /**
     * Get count of all lines
     */
    @Query("SELECT COUNT(*) FROM transit_lines")
    suspend fun getCount(): Int

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
