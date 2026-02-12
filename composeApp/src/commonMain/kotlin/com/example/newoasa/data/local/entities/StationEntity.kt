package com.example.newoasa.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    @ColumnInfo(name = "stop_code")
    val stopCode: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "line_id")
    val lineId: String,

    @ColumnInfo(name = "line_category")
    val lineCategory: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "order")
    val order: Int = 0
)