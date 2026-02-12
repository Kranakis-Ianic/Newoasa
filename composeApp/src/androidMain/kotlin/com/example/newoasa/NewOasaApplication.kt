package com.example.newoasa

import android.app.Application
import com.example.newoasa.database.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewOasaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@NewOasaApplication)
            // Load modules
            modules(databaseModule)
        }
    }
}