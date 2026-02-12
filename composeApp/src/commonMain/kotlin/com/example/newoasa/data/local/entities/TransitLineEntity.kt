package com.example.newoasa.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transit_lines")
data class TransitLineEntity(
    @PrimaryKey
    @ColumnInfo(name = "line_id")
    val lineId: String,

    @ColumnInfo(name = "line_code") // Added to resolve potential query errors
    val lineCode: String? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "transport_type") // Added to resolve 'no such column: transportType'
    val transportType: String? = null
)