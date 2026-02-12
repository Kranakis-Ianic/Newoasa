package com.example.newoasa

import android.app.Application
import com.example.newoasa.database.databaseModule // Make sure this import matches your actual module name
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewOasaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@NewOasaApplication)
            // Load modules
            // IMPORTANT: Replace 'databaseModule' with the actual name of your Koin module variable
            // defined in your DatabaseModule.kt or similar file.
            modules(databaseModule)
        }
    }
}