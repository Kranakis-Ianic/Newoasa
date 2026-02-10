package com.example.newoasa.database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory

private const val DATABASE_NAME = "transit_database.db"

/**
 * iOS actual implementation of database builder
 */
actual fun getDatabaseBuilder(): RoomDatabase.Builder<TransitDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/$DATABASE_NAME"
    return Room.databaseBuilder<TransitDatabase>(
        name = dbFilePath
    )
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
}
