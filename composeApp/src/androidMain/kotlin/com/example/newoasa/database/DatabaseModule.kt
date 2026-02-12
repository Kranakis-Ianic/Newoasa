package com.example.newoasa.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for Android-specific database dependencies.
 */
val databaseModule = module {
    // Provide the TransitDatabase instance using the existing getInstance method
    single { TransitDatabase.getInstance(androidContext()) }

    // Provide the DAOs via the database instance
    single { get<TransitDatabase>().transitLineDao() }
    single { get<TransitDatabase>().stationDao() }
}