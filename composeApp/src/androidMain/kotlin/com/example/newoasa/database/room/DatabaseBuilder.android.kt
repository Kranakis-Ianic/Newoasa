package com.example.newoasa.database.room

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.newoasa.data.local.TransitDatabase
import kotlinx.coroutines.Dispatchers

/**
 * Android implementation of database builder
 */
actual object DatabaseBuilder {
    private var instance: TransitDatabase? = null
    private lateinit var appContext: Context
    
    /**
     * Initialize the database builder with application context
     * Call this from your Application class or MainActivity
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }
    
    actual fun build(): TransitDatabase {
        if (instance == null) {
            if (!::appContext.isInitialized) {
                throw IllegalStateException(
                    "DatabaseBuilder not initialized. Call DatabaseBuilder.initialize(context) first."
                )
            }
            
            val dbBuilder = Room.databaseBuilder<TransitDatabase>(
                context = appContext,
                name = appContext.getDatabasePath("transit_database.db").absolutePath
            )
            instance = dbBuilder
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
        return instance!!
    }
}
