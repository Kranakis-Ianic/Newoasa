package com.example.newoasa.di

import com.example.newoasa.data.repository.TransitRepository
import com.example.newoasa.data.local.TransitDatabase
import org.koin.dsl.module

val sharedModule = module {
    // Provide the Repository
    // It automatically finds StationDao and TransitLineDao which are provided by the platform-specific modules
    single { TransitRepository(get(), get()) }
}