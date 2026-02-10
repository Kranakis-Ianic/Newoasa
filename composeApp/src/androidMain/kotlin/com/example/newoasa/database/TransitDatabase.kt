package com.example.newoasa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newoasa.database.dao.StationDao
import com.example.newoasa.database.dao.TransitLineDao
import com.example.newoasa.database.entities.StationEntity
import com.example.newoasa.database.entities.TransitLineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main Room database for the transit application
 * 
 * This database stores:
 * - Transit lines (buses, trolleys, metro, tram, suburban railway)
 * - Stations/stops with detailed information for each transport type
 * 
 * Database version: 1
 */
@Database(
    entities = [
        TransitLineEntity::class,
        StationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TransitDatabase : RoomDatabase() {
    
    abstract fun transitLineDao(): TransitLineDao
    abstract fun stationDao(): StationDao
    
    companion object {
        @Volatile
        private var INSTANCE: TransitDatabase? = null
        
        private const val DATABASE_NAME = "transit_database.db"
        
        /**
         * Get the singleton database instance
         */
        fun getInstance(context: Context): TransitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransitDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration() // For development only
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Clear the database instance (mainly for testing)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }
    
    /**
     * Database callback for initialization
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Optional: Pre-populate database with initial data
            // This can be done by calling a suspend function in a coroutine
        }
        
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // Optional: Perform any operations when database is opened
        }
    }
}

/**
 * Extension function to populate database with initial data
 * Call this after getting the database instance
 */
suspend fun TransitDatabase.populateInitialData() {
    // This function can be called to populate the database with
    // data from TransitLineRepository
    
    // Example structure:
    // val transitLineDao = transitLineDao()
    // val stationDao = stationDao()
    // 
    // Import data from TransitLineRepository and convert to entities
    // transitLineDao.insertLines(convertedLines)
    // stationDao.insertStations(convertedStations)
}
