package com.example.newoasa.database

import com.example.newoasa.database.room.TransitDatabase
import com.example.newoasa.database.room.getDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for Android-specific database dependencies.
 * Updated to use the KMP Room Database.
 */
val databaseModule = module {
    // Provide the KMP TransitDatabase using the KMP builder function
    single<TransitDatabase> { getDatabase(androidContext()) }

    // Provide the DAOs via the database instance
    single { get<TransitDatabase>().transitLineDao() }
    single { get<TransitDatabase>().stationDao() }
}