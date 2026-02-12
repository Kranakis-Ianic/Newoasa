package com.example.newoasa.database.room

import android.content.Context
import androidx.room.Room
import com.example.newoasa.data.local.TransitDatabase
import com.example.newoasa.data.local.getRoomDatabase

// Simple factory function
fun getDatabase(context: Context): TransitDatabase {
    val dbFile = context.getDatabasePath("transit.db")
    return getRoomDatabase(
        Room.databaseBuilder<TransitDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    )
}