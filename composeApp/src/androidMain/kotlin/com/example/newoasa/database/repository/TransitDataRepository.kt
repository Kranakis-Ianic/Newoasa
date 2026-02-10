package com.example.newoasa.database.repository

import android.content.Context
import com.example.newoasa.data.OasaJsonData
import com.example.newoasa.data.TransitLine
import com.example.newoasa.database.room.DatabaseBuilder
import com.example.newoasa.database.room.entities.StationEntity
import com.example.newoasa.database.room.entities.TransitLineEntity
import com.example.newoasa.theme.LineColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import newoasa.composeapp.generated.resources.Res

/**
 * Repository for transit data
 */
class TransitDataRepository(private val context: Context) {
    
    private val db = DatabaseBuilder.build()
    
    suspend fun getAllLines(): List<TransitLine> = withContext(Dispatchers.IO) {
        db.transitLineDao().getAll().map { entity ->
            TransitLine(
                lineCode = entity.lineCode,
                lineName = entity.lineName,
                category = entity.category,
                routePaths = entity.routePaths,
                isBus = entity.transportType == "bus"
            )
        }
    }
    
    suspend fun getLinesByCategory(category: String): List<TransitLine> = withContext(Dispatchers.IO) {
        db.transitLineDao().getByCategory(category).map { entity ->
            TransitLine(
                lineCode = entity.lineCode,
                lineName = entity.lineName,
                category = entity.category,
                routePaths = entity.routePaths,
                isBus = entity.transportType == "bus"
            )
        }
    }
    
    suspend fun searchLines(query: String): List<TransitLine> = withContext(Dispatchers.IO) {
        db.transitLineDao().search("%$query%").map { entity ->
            TransitLine(
                lineCode = entity.lineCode,
                lineName = entity.lineName,
                category = entity.category,
                routePaths = entity.routePaths,
                isBus = entity.transportType == "bus"
            )
        }
    }
    
    @OptIn(ExperimentalResourceApi::class)
    suspend fun populateInitialData() = withContext(Dispatchers.IO) {
        if (db.transitLineDao().getCount() == 0) {
            // Load oasa.json and populate database
            val json = String(Res.readBytes("files/oasa.json"))
            val oasaData = parseOasaJson(json)
            
            val lineEntities = mutableListOf<TransitLineEntity>()
            
            oasaData.buses.forEach { line ->
                lineEntities.add(
                    TransitLineEntity(
                        id = 0,
                        lineCode = line.lineCode,
                        lineName = line.lineName,
                        category = "buses",
                        transportType = "bus",
                        color = LineColors.getHexColorForCategory("buses"),
                        routePaths = line.routePaths
                    )
                )
            }
            
            db.transitLineDao().insertAll(lineEntities)
        }
    }
    
    private fun parseOasaJson(json: String): OasaJsonData {
        // JSON parsing implementation
        return OasaJsonData(emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    }
}
