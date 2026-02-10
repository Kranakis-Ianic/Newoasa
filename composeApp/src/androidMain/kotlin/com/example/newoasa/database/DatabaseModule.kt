package com.example.newoasa.database

import android.content.Context
import com.example.newoasa.database.dao.StationDao
import com.example.newoasa.database.dao.TransitLineDao

/**
 * Database module providing access to database and DAOs
 * This is a simple dependency injection alternative
 * For production, consider using Hilt or Koin
 */
object DatabaseModule {
    
    private var database: TransitDatabase? = null
    
    /**
     * Initialize the database
     * Call this in your Application class or MainActivity
     */
    fun initialize(context: Context) {
        if (database == null) {
            database = TransitDatabase.getInstance(context)
        }
    }
    
    /**
     * Get the database instance
     * Make sure to call initialize() first
     */
    fun getDatabase(): TransitDatabase {
        return database ?: throw IllegalStateException(
            "DatabaseModule not initialized. Call initialize(context) first."
        )
    }
    
    /**
     * Get TransitLineDao
     */
    fun provideTransitLineDao(): TransitLineDao {
        return getDatabase().transitLineDao()
    }
    
    /**
     * Get StationDao
     */
    fun provideStationDao(): StationDao {
        return getDatabase().stationDao()
    }
    
    /**
     * Clear all data (for testing or reset)
     */
    suspend fun clearAllData() {
        val db = getDatabase()
        db.transitLineDao().deleteAllLines()
        db.stationDao().deleteAllStations()
    }
    
    /**
     * Close database (call when app is destroyed)
     */
    fun closeDatabase() {
        database?.close()
        database = null
    }
}
