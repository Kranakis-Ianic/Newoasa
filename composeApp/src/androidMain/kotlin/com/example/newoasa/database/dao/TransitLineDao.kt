package com.example.newoasa.database.dao

import androidx.room.*
import com.example.newoasa.database.entities.TransitLineEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for transit lines
 */
@Dao
interface TransitLineDao {
    
    // === Create operations ===
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLine(line: TransitLineEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(lines: List<TransitLineEntity>): List<Long>
    
    // === Read operations ===
    
    @Query("SELECT * FROM transit_lines WHERE id = :lineId")
    suspend fun getLineById(lineId: Long): TransitLineEntity?
    
    @Query("SELECT * FROM transit_lines WHERE id = :lineId")
    fun getLineByIdFlow(lineId: Long): Flow<TransitLineEntity?>
    
    @Query("SELECT * FROM transit_lines WHERE lineNumber = :lineNumber AND category = :category")
    suspend fun getLineByNumberAndCategory(lineNumber: String, category: String): TransitLineEntity?
    
    @Query("SELECT * FROM transit_lines ORDER BY category, lineNumber")
    suspend fun getAllLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines ORDER BY category, lineNumber")
    fun getAllLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = :category ORDER BY lineNumber")
    suspend fun getLinesByCategory(category: String): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = :category ORDER BY lineNumber")
    fun getLinesByCategoryFlow(category: String): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'bus' ORDER BY lineNumber")
    suspend fun getBusLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'bus' ORDER BY lineNumber")
    fun getBusLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'trolley' ORDER BY lineNumber")
    suspend fun getTrolleyLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'trolley' ORDER BY lineNumber")
    fun getTrolleyLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'metro' ORDER BY lineNumber")
    suspend fun getMetroLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'metro' ORDER BY lineNumber")
    fun getMetroLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'tram' ORDER BY lineNumber")
    suspend fun getTramLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'tram' ORDER BY lineNumber")
    fun getTramLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'suburban' ORDER BY lineNumber")
    suspend fun getSuburbanLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE category = 'suburban' ORDER BY lineNumber")
    fun getSuburbanLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("SELECT * FROM transit_lines WHERE isActive = 1 ORDER BY category, lineNumber")
    suspend fun getActiveLines(): List<TransitLineEntity>
    
    @Query("SELECT * FROM transit_lines WHERE isActive = 1 ORDER BY category, lineNumber")
    fun getActiveLinesFlow(): Flow<List<TransitLineEntity>>
    
    @Query("""
        SELECT * FROM transit_lines 
        WHERE lineNumber LIKE '%' || :query || '%' 
        OR displayName LIKE '%' || :query || '%'
        ORDER BY category, lineNumber
    """)
    suspend fun searchLines(query: String): List<TransitLineEntity>
    
    @Query("""
        SELECT * FROM transit_lines 
        WHERE lineNumber LIKE '%' || :query || '%' 
        OR displayName LIKE '%' || :query || '%'
        ORDER BY category, lineNumber
    """)
    fun searchLinesFlow(query: String): Flow<List<TransitLineEntity>>
    
    // === Update operations ===
    
    @Update
    suspend fun updateLine(line: TransitLineEntity)
    
    @Update
    suspend fun updateLines(lines: List<TransitLineEntity>)
    
    @Query("UPDATE transit_lines SET isActive = :isActive WHERE id = :lineId")
    suspend fun updateLineActiveStatus(lineId: Long, isActive: Boolean)
    
    @Query("UPDATE transit_lines SET updatedAt = :timestamp WHERE id = :lineId")
    suspend fun updateLineTimestamp(lineId: Long, timestamp: Long = System.currentTimeMillis())
    
    // === Delete operations ===
    
    @Delete
    suspend fun deleteLine(line: TransitLineEntity)
    
    @Query("DELETE FROM transit_lines WHERE id = :lineId")
    suspend fun deleteLineById(lineId: Long)
    
    @Query("DELETE FROM transit_lines WHERE category = :category")
    suspend fun deleteLinesByCategory(category: String)
    
    @Query("DELETE FROM transit_lines")
    suspend fun deleteAllLines()
    
    // === Statistics ===
    
    @Query("SELECT COUNT(*) FROM transit_lines")
    suspend fun getTotalLineCount(): Int
    
    @Query("SELECT COUNT(*) FROM transit_lines WHERE category = :category")
    suspend fun getLineCountByCategory(category: String): Int
    
    @Query("SELECT COUNT(*) FROM transit_lines WHERE isActive = 1")
    suspend fun getActiveLineCount(): Int
}
