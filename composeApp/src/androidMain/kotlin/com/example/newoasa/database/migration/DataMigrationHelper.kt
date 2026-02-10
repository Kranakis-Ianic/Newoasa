package com.example.newoasa.database.migration

import android.content.Context
import com.example.newoasa.data.OasaJsonData
import com.example.newoasa.data.TransitLine
import com.example.newoasa.database.room.DatabaseBuilder
import com.example.newoasa.database.room.entities.StationEntity
import com.example.newoasa.database.room.entities.TransitLineEntity
import com.example.newoasa.theme.LineColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import newoasa.composeapp.generated.resources.Res

/**
 * Helper for data migration and initial database population
 */
object DataMigrationHelper {
    
    @OptIn(ExperimentalResourceApi::class)
    suspend fun populateInitialData(context: Context) = withContext(Dispatchers.IO) {
        // Load and parse oasa.json
        val json = String(Res.readBytes("files/oasa.json"))
        val oas aData = parseOasaJson(json)
        
        val db = DatabaseBuilder.build()
        
        // Transform data to entities
        val lineEntities = mutableListOf<TransitLineEntity>()
        val stationEntities = mutableListOf<StationEntity>()
        
        // Process buses
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
        
        // Process metro
        oasaData.metro.forEach { line ->
            lineEntities.add(
                TransitLineEntity(
                    id = 0,
                    lineCode = line.lineCode,
                    lineName = line.lineName,
                    category = "metro",
                    transportType = "metro",
                    color = LineColors.getHexColorForCategory("metro"),
                    routePaths = line.routePaths
                )
            )
        }
        
        // Insert into database
        db.transitLineDao().insertAll(lineEntities)
        // Similar for stations...
    }
    
    private fun parseOasaJson(json: String): OasaJsonData {
        // JSON parsing implementation
        return OasaJsonData(emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    }
}
