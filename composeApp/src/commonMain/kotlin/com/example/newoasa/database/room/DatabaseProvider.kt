package com.example.newoasa.database.room

import androidx.room.RoomDatabase

/**
 * Provides platform-specific database builder
 * Use expect/actual pattern for platform implementations
 */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<TransitDatabase>

/**
 * Get the transit database instance
 * Call this from common code
 */
fun getTransitRoomDatabase(): TransitDatabase {
    return getDatabaseBuilder()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .build()
}
