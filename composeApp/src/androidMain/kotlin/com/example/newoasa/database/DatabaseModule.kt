package com.example.newoasa.database

import com.example.newoasa.data.local.TransitDatabase
import com.example.newoasa.database.room.getDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    // Provide the Database using the builder function
    single<TransitDatabase> { getDatabase(androidContext()) }

    // Provide the DAOs
    single { get<TransitDatabase>().transitLineDao() }
    single { get<TransitDatabase>().stationDao() }
}