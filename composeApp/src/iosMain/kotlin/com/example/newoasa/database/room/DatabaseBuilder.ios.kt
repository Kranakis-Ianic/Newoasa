package com.example.newoasa.database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS implementation of database builder
 */
actual object DatabaseBuilder {
    private var instance: TransitDatabase? = null
    
    @OptIn(ExperimentalForeignApi::class)
    actual fun build(): TransitDatabase {
        if (instance == null) {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            val dbPath = requireNotNull(documentDirectory?.path) + "/transit_database.db"
            
            val dbBuilder = Room.databaseBuilder<TransitDatabase>(
                name = dbPath
            )
            instance = dbBuilder
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
        return instance!!
    }
}
