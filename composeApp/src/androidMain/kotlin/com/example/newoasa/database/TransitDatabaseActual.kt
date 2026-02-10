package com.example.newoasa.database

import android.content.Context

/**
 * Android actual implementation
 * Provides Room-based database instance
 */
private var databaseInstance: ITransitDatabase? = null
private var appContext: Context? = null

/**
 * Initialize database with Android context
 * Call this in your Application class or MainActivity
 */
fun initializeTransitDatabase(context: Context) {
    appContext = context.applicationContext
    databaseInstance = RoomTransitDatabase(context.applicationContext)
}

/**
 * Android actual implementation of getTransitDatabase
 */
actual fun getTransitDatabase(): ITransitDatabase {
    return databaseInstance ?: throw IllegalStateException(
        "Database not initialized. Call initializeTransitDatabase(context) first."
    )
}
