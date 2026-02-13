package com.example.newoasa.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.newoasa.data.local.dao.StationDao
import com.example.newoasa.data.local.dao.TransitLineDao
import com.example.newoasa.data.local.entities.StationEntity
import com.example.newoasa.data.local.entities.TransitLineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

// The Room Database definition
@Database(entities = [StationEntity::class, TransitLineEntity::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(TransitDatabaseConstructor::class)
abstract class TransitDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun transitLineDao(): TransitLineDao
}

// The Helper function required by the Android Builder
fun getRoomDatabase(
    builder: RoomDatabase.Builder<TransitDatabase>
): TransitDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// Constructor expectation - Remove the type constraint
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object TransitDatabaseConstructor : RoomDatabaseConstructor<TransitDatabase> {
    override fun initialize(): TransitDatabase
}