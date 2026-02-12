package com.example.newoasa.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.newoasa.data.local.TransitDatabase
import kotlinx.coroutines.Dispatchers

private lateinit var appContext: Context
private const val DATABASE_NAME = "transit_database.db"

/**
 * Initialize the database with Android context
 * Call this in your Application class or MainActivity onCreate
 */
fun initializeDatabase(context: Context) {
    appContext = context.applicationContext
}

/**
 * Android actual implementation of database builder
 */
actual fun getDatabaseBuilder(): RoomDatabase.Builder<TransitDatabase> {
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<TransitDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
}
