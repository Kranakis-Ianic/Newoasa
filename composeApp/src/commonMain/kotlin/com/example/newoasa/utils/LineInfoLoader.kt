package com.example.newoasa.utils

import com.example.newoasa.data.LineInfo
import com.example.newoasa.data.Station
import com.example.newoasa.data.StationInfo
import com.example.newoasa.generated.resources.Res
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Loads line colors from the GeoJSON bundle (composeResources/files/geojson).
 */
object LineInfoLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private var colorsByRef: Map<String, String>? = null

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadColorsByRef(): Map<String, String> {
        colorsByRef?.let { return it }

        val text = Res.readBytes("files/geojson/final_all_lines.geojson").decodeToString()
        val root = json.parseToJsonElement(text).jsonObject
        val features = root["features"]?.jsonArray.orEmpty()

        val map = HashMap<String, String>(features.size)
        for (feature in features) {
            val props = feature.jsonObject["properties"]?.jsonObject ?: continue
            val ref = props["ref"]?.jsonPrimitive?.content ?: continue
            val color = props["colour"]?.jsonPrimitive?.content
                ?: props["color"]?.jsonPrimitive?.content
                ?: continue
            map[normalizeRef(ref)] = color
        }

        colorsByRef = map
        return map
    }

    /** Returns the hex colour for a line ref (e.g. "M1", "T6", "Χ95"). */
    suspend fun getHexColorForLine(lineRef: String): String {
        val key = normalizeRef(lineRef)
        return loadColorsByRef()[key]
            ?: error("Missing 'colour' for line ref: $lineRef")
    }

    /** Convert Station to StationInfo, using GeoJSON-derived colours. */
    suspend fun toStationInfo(station: Station): StationInfo {
        val lines = station.lines.map { ref ->
            LineInfo(
                ref = ref,
                colour = getHexColorForLine(ref),
                name = ref,
                route = null
            )
        }
        return StationInfo(
            name = station.name,
            nameEn = station.nameEn,
            latitude = station.latitude,
            longitude = station.longitude,
            lines = lines,
            wheelchair = station.wheelchair
        )
    }

    fun clearCache() {
        colorsByRef = null
    }

    private fun normalizeRef(ref: String): String {
        return ref.trim()
            .replace("Μ", "M")
            .replace("Χ", "X")
            .replace("Α", "A")
            .replace("Β", "B")
            .replace("Γ", "G")
            .replace("Ε", "E")
            .replace("Τ", "T")
            .uppercase()
    }
}
