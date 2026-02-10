package com.example.newoasa.utils

import newoasa.composeapp.generated.resources.Res

/**
 * Load a GeoJSON file from Compose resources
 * @param path Path relative to composeResources directory (e.g., "files/geojson/buses/301/route_1234.geojson")
 * @return GeoJSON content as string, or null if loading failed
 */
suspend fun loadGeoJsonFromResources(path: String): String? {
    return try {
        val bytes = Res.readBytes(path)
        bytes.decodeToString()
    } catch (e: Exception) {
        println("Error loading GeoJSON from $path: ${e.message}")
        null
    }
}
