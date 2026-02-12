package com.example.newoasa.data.local

/**
 * Platform-specific database builder
 * Android uses context, iOS uses file path
 */
expect object DatabaseBuilder {
    fun build(): TransitDatabase
}

/**
 * Get the database instance
 * This is a convenience function that works across all platforms
 */
fun getDatabase(): TransitDatabase = DatabaseBuilder.build()
