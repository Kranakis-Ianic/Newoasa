package com.example.newoasa.data

import kotlinx.serialization.Serializable

@Serializable
data class Station(
    val id: String,
    val name: String,
    val nameEn: String? = null,
    val latitude: Double,
    val longitude: Double,
    val lines: List<String> = emptyList(), // List of line numbers/names that stop here
    val wheelchair: Boolean = false,
    val type: String = "Station" // Station, Stop, etc.
)
