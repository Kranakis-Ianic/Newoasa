package com.example.newoasa.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.ConstructedBy
import com.example.newoasa.database.room.dao.StationDao
import com.example.newoasa.database.room.dao.TransitLineDao
import com.example.newoasa.database.room.entities.StationEntity
import com.example.newoasa.database.room.entities.TransitLineEntity

/**
 * Main Room database for the transit application
 * Room Multiplatform compatible - works on Android and iOS
 * 
 * This database stores:
 * - Transit lines (buses, trolleys, metro, tram, suburban railway)
 * - Stations/stops with detailed information for each transport type
 * 
 * Room 2.7+ requires @ConstructedBy for multiplatform support
 */
@Database(
    entities = [
        TransitLineEntity::class,
        StationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(TransitDatabaseConstructor::class)
abstract class TransitDatabase : RoomDatabase() {
    abstract fun transitLineDao(): TransitLineDao
    abstract fun stationDao(): StationDao
}

/**
 * Room database constructor
 * Room generates the actual implementation automatically via KSP
 * The @Suppress annotation tells Kotlin compiler to not expect a manual implementation
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object TransitDatabaseConstructor : RoomDatabaseConstructor<TransitDatabase> {
    override fun initialize(): TransitDatabase
}
