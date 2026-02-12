package com.example.newoasa.database

import com.example.newoasa.database.room.TransitDatabase
import com.example.newoasa.database.room.getDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    // Provide the TransitDatabase instance using the function we created
    single<TransitDatabase> { getDatabase(androidContext()) }

    // Provide DAOs
    single { get<TransitDatabase>().transitLineDao() }
    single { get<TransitDatabase>().stationDao() }
}