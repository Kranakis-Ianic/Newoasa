package com.example.newoasa.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

/**
 * Type converters for Room database
 * Converts complex types to/from database-compatible types
 */
class Converters {
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
