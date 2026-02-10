package com.example.newoasa.data

/**
 * Represents a transit stop/station
 */
data class Stop(
    val name: String,
    val stopCode: String,
    val order: String,
    val latitude: Double,
    val longitude: Double
)

/**
 * State for displaying stop information
 */
data class StopInfoState(
    val stop: Stop
)
