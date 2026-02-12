package com.example.newoasa

import android.app.Application
import com.example.newoasa.database.databaseModule
import com.example.newoasa.di.sharedModule // Import the new module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewOasaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewOasaApplication)
            // Load BOTH modules: one for Android DB setup, one for shared Repositories
            modules(databaseModule, sharedModule)
        }
    }
}