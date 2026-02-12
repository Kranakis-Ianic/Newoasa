package com.example.newoasa.database.room

import android.content.Context
import androidx.room.Room

fun getDatabase(context: Context): TransitDatabase {
    val dbFile = context.getDatabasePath("transit.db")
    return getRoomDatabase(
        Room.databaseBuilder<TransitDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    )
}