package com.example.newoasa.data

/**
 * Auto-generated Transit Line Repository
 * Generated from GeoJSON folder structure
 * 
 * DO NOT EDIT MANUALLY!
 * Run scripts/GenerateTransitLineRepository.kt to regenerate this file
 * 
 * Generated: 2026-02-06T14:36:00
 * Total Lines: 201
 * Total Routes: To be populated by running the generator script
 */

/**
 * Represents a transit line with its routes
 * @property lineNumber The line identifier (e.g., "022", "1", "309Β")
 * @property category The category of transit ("buses" or "trolleys")
 * @property routeIds List of route IDs for this line
 * @property routePaths Resource paths to the GeoJSON files for each route
 */
data class TransitLine(
    val lineNumber: String,
    val category: String,
    val routeIds: List<String> = emptyList(),
    val routePaths: List<String> = emptyList()
) {
    /**
     * Returns the display name for this line
     */
    val displayName: String
        get() = when (category) {
            "buses" -> "Bus $lineNumber"
            "trolleys" -> "Trolley $lineNumber"
            else -> "Line $lineNumber"
        }
    
    /**
     * Returns true if this is a bus line
     */
    val isBus: Boolean get() = category == "buses"
    
    /**
     * Returns true if this is a trolley line
     */
    val isTrolley: Boolean get() = category == "trolleys"
    
    /**
     * Returns the base path to this line's geojson folder
     */
    val basePath: String get() = "files/geojson/$category/$lineNumber"
}

/**
 * Repository providing access to all transit lines
 * This is a singleton object that holds all transit line data
 */
object TransitLineRepository {
    private val lines: List<TransitLine> = listOf(
        // Bus Lines
        TransitLine(lineNumber = "022", category = "buses"),
        TransitLine(lineNumber = "026", category = "buses"),
        TransitLine(lineNumber = "031", category = "buses"),
        TransitLine(lineNumber = "032", category = "buses"),
        TransitLine(lineNumber = "035", category = "buses"),
        TransitLine(lineNumber = "036", category = "buses"),
        TransitLine(lineNumber = "040", category = "buses"),
        TransitLine(lineNumber = "046", category = "buses"),
        TransitLine(lineNumber = "049", category = "buses"),
        TransitLine(lineNumber = "051", category = "buses"),
        TransitLine(lineNumber = "052", category = "buses"),
        TransitLine(lineNumber = "054", category = "buses"),
        TransitLine(lineNumber = "057", category = "buses"),
        TransitLine(lineNumber = "060", category = "buses"),
        TransitLine(lineNumber = "101", category = "buses"),
        TransitLine(lineNumber = "106", category = "buses"),
        TransitLine(lineNumber = "109", category = "buses"),
        TransitLine(lineNumber = "112", category = "buses"),
        TransitLine(lineNumber = "115", category = "buses"),
        TransitLine(lineNumber = "116", category = "buses"),
        TransitLine(lineNumber = "117", category = "buses"),
        TransitLine(lineNumber = "120", category = "buses"),
        TransitLine(lineNumber = "122", category = "buses"),
        TransitLine(lineNumber = "123", category = "buses"),
        TransitLine(lineNumber = "124", category = "buses"),
        TransitLine(lineNumber = "126", category = "buses"),
        TransitLine(lineNumber = "128", category = "buses"),
        TransitLine(lineNumber = "130", category = "buses"),
        TransitLine(lineNumber = "131", category = "buses"),
        TransitLine(lineNumber = "136", category = "buses"),
        TransitLine(lineNumber = "137", category = "buses"),
        TransitLine(lineNumber = "14", category = "buses"),
        TransitLine(lineNumber = "140", category = "buses"),
        TransitLine(lineNumber = "141", category = "buses"),
        TransitLine(lineNumber = "142", category = "buses"),
        TransitLine(lineNumber = "154", category = "buses"),
        TransitLine(lineNumber = "16", category = "buses"),
        TransitLine(lineNumber = "162", category = "buses"),
        TransitLine(lineNumber = "164", category = "buses"),
        TransitLine(lineNumber = "171", category = "buses"),
        TransitLine(lineNumber = "19Β", category = "buses"),
        TransitLine(lineNumber = "201", category = "buses"),
        TransitLine(lineNumber = "203", category = "buses"),
        TransitLine(lineNumber = "204", category = "buses"),
        TransitLine(lineNumber = "204Β", category = "buses"),
        TransitLine(lineNumber = "205", category = "buses"),
        TransitLine(lineNumber = "206", category = "buses"),
        TransitLine(lineNumber = "209", category = "buses"),
        TransitLine(lineNumber = "211", category = "buses"),
        TransitLine(lineNumber = "212", category = "buses"),
        TransitLine(lineNumber = "214", category = "buses"),
        TransitLine(lineNumber = "217", category = "buses"),
        TransitLine(lineNumber = "218", category = "buses"),
        TransitLine(lineNumber = "219", category = "buses"),
        TransitLine(lineNumber = "220", category = "buses"),
        TransitLine(lineNumber = "221", category = "buses"),
        TransitLine(lineNumber = "224", category = "buses"),
        TransitLine(lineNumber = "227", category = "buses"),
        TransitLine(lineNumber = "229", category = "buses"),
        TransitLine(lineNumber = "230", category = "buses"),
        TransitLine(lineNumber = "235", category = "buses"),
        TransitLine(lineNumber = "237", category = "buses"),
        TransitLine(lineNumber = "242", category = "buses"),
        TransitLine(lineNumber = "250", category = "buses"),
        TransitLine(lineNumber = "300", category = "buses"),
        TransitLine(lineNumber = "301", category = "buses"),
        TransitLine(lineNumber = "301Β", category = "buses"),
        TransitLine(lineNumber = "302", category = "buses"),
        TransitLine(lineNumber = "304", category = "buses"),
        TransitLine(lineNumber = "305", category = "buses"),
        TransitLine(lineNumber = "306", category = "buses"),
        TransitLine(lineNumber = "307", category = "buses"),
        TransitLine(lineNumber = "308", category = "buses"),
        TransitLine(lineNumber = "309", category = "buses"),
        TransitLine(lineNumber = "309Β", category = "buses"),
        TransitLine(lineNumber = "310", category = "buses"),
        TransitLine(lineNumber = "311", category = "buses"),
        TransitLine(lineNumber = "314", category = "buses"),
        TransitLine(lineNumber = "316", category = "buses"),
        TransitLine(lineNumber = "319", category = "buses"),
        TransitLine(lineNumber = "323", category = "buses"),
        TransitLine(lineNumber = "324", category = "buses"),
        TransitLine(lineNumber = "326", category = "buses"),
        TransitLine(lineNumber = "330", category = "buses"),
        TransitLine(lineNumber = "400", category = "buses"),
        TransitLine(lineNumber = "402", category = "buses"),
        TransitLine(lineNumber = "405", category = "buses"),
        TransitLine(lineNumber = "406", category = "buses"),
        TransitLine(lineNumber = "407", category = "buses"),
        TransitLine(lineNumber = "409", category = "buses"),
        TransitLine(lineNumber = "410", category = "buses"),
        TransitLine(lineNumber = "411", category = "buses"),
        TransitLine(lineNumber = "416", category = "buses"),
        TransitLine(lineNumber = "418", category = "buses"),
        TransitLine(lineNumber = "420", category = "buses"),
        TransitLine(lineNumber = "421", category = "buses"),
        TransitLine(lineNumber = "444", category = "buses"),
        TransitLine(lineNumber = "446", category = "buses"),
        TransitLine(lineNumber = "447", category = "buses"),
        TransitLine(lineNumber = "450", category = "buses"),
        TransitLine(lineNumber = "451", category = "buses"),
        TransitLine(lineNumber = "460", category = "buses"),
        TransitLine(lineNumber = "461", category = "buses"),
        TransitLine(lineNumber = "500", category = "buses"),
        TransitLine(lineNumber = "501", category = "buses"),
        TransitLine(lineNumber = "503", category = "buses"),
        TransitLine(lineNumber = "504", category = "buses"),
        TransitLine(lineNumber = "507", category = "buses"),
        TransitLine(lineNumber = "509", category = "buses"),
        TransitLine(lineNumber = "509Β", category = "buses"),
        TransitLine(lineNumber = "522", category = "buses"),
        TransitLine(lineNumber = "523", category = "buses"),
        TransitLine(lineNumber = "524", category = "buses"),
        TransitLine(lineNumber = "526", category = "buses"),
        TransitLine(lineNumber = "527", category = "buses"),
        TransitLine(lineNumber = "530", category = "buses"),
        TransitLine(lineNumber = "535", category = "buses"),
        TransitLine(lineNumber = "535Α", category = "buses"),
        TransitLine(lineNumber = "536", category = "buses"),
        TransitLine(lineNumber = "537", category = "buses"),
        TransitLine(lineNumber = "541", category = "buses"),
        TransitLine(lineNumber = "543", category = "buses"),
        TransitLine(lineNumber = "543Α", category = "buses"),
        TransitLine(lineNumber = "550", category = "buses"),
        TransitLine(lineNumber = "560", category = "buses"),
        TransitLine(lineNumber = "602", category = "buses"),
        TransitLine(lineNumber = "604", category = "buses"),
        TransitLine(lineNumber = "605", category = "buses"),
        TransitLine(lineNumber = "608", category = "buses"),
        TransitLine(lineNumber = "610", category = "buses"),
        TransitLine(lineNumber = "619", category = "buses"),
        TransitLine(lineNumber = "622", category = "buses"),
        TransitLine(lineNumber = "640", category = "buses"),
        TransitLine(lineNumber = "642", category = "buses"),
        TransitLine(lineNumber = "651", category = "buses"),
        TransitLine(lineNumber = "653", category = "buses"),
        TransitLine(lineNumber = "700", category = "buses"),
        TransitLine(lineNumber = "701", category = "buses"),
        TransitLine(lineNumber = "702", category = "buses"),
        TransitLine(lineNumber = "703", category = "buses"),
        TransitLine(lineNumber = "704", category = "buses"),
        TransitLine(lineNumber = "705", category = "buses"),
        TransitLine(lineNumber = "706", category = "buses"),
        TransitLine(lineNumber = "709", category = "buses"),
        TransitLine(lineNumber = "711", category = "buses"),
        TransitLine(lineNumber = "712", category = "buses"),
        TransitLine(lineNumber = "719", category = "buses"),
        TransitLine(lineNumber = "720", category = "buses"),
        TransitLine(lineNumber = "721", category = "buses"),
        TransitLine(lineNumber = "723", category = "buses"),
        TransitLine(lineNumber = "724", category = "buses"),
        TransitLine(lineNumber = "725", category = "buses"),
        TransitLine(lineNumber = "726", category = "buses"),
        TransitLine(lineNumber = "727", category = "buses"),
        TransitLine(lineNumber = "728", category = "buses"),
        TransitLine(lineNumber = "730", category = "buses"),
        TransitLine(lineNumber = "731", category = "buses"),
        TransitLine(lineNumber = "732", category = "buses"),
        TransitLine(lineNumber = "733", category = "buses"),
        TransitLine(lineNumber = "734", category = "buses"),
        TransitLine(lineNumber = "735", category = "buses"),
        TransitLine(lineNumber = "740", category = "buses"),
        TransitLine(lineNumber = "747", category = "buses"),
        TransitLine(lineNumber = "748", category = "buses"),
        TransitLine(lineNumber = "749", category = "buses"),
        TransitLine(lineNumber = "750", category = "buses"),
        TransitLine(lineNumber = "752", category = "buses"),
        TransitLine(lineNumber = "755", category = "buses"),
        TransitLine(lineNumber = "755Β", category = "buses"),
        TransitLine(lineNumber = "790", category = "buses"),
        TransitLine(lineNumber = "800", category = "buses"),
        TransitLine(lineNumber = "801", category = "buses"),
        TransitLine(lineNumber = "803", category = "buses"),
        TransitLine(lineNumber = "805", category = "buses"),
        TransitLine(lineNumber = "806", category = "buses"),
        TransitLine(lineNumber = "807", category = "buses"),
        TransitLine(lineNumber = "809", category = "buses"),
        TransitLine(lineNumber = "810", category = "buses"),
        TransitLine(lineNumber = "811", category = "buses"),
        TransitLine(lineNumber = "813", category = "buses"),
        TransitLine(lineNumber = "814", category = "buses"),
        
        // Trolley Lines
        TransitLine(lineNumber = "021", category = "trolleys"),
        TransitLine(lineNumber = "024", category = "trolleys"),
        TransitLine(lineNumber = "025", category = "trolleys"),
        TransitLine(lineNumber = "1", category = "trolleys"),
        TransitLine(lineNumber = "10", category = "trolleys"),
        TransitLine(lineNumber = "11", category = "trolleys"),
        TransitLine(lineNumber = "12", category = "trolleys"),
        TransitLine(lineNumber = "15", category = "trolleys"),
        TransitLine(lineNumber = "17", category = "trolleys"),
        TransitLine(lineNumber = "18", category = "trolleys"),
        TransitLine(lineNumber = "19", category = "trolleys"),
        TransitLine(lineNumber = "2", category = "trolleys"),
        TransitLine(lineNumber = "20", category = "trolleys"),
        TransitLine(lineNumber = "21", category = "trolleys"),
        TransitLine(lineNumber = "24", category = "trolleys"),
        TransitLine(lineNumber = "25", category = "trolleys"),
        TransitLine(lineNumber = "3", category = "trolleys"),
        TransitLine(lineNumber = "4", category = "trolleys"),
        TransitLine(lineNumber = "5", category = "trolleys"),
        TransitLine(lineNumber = "6", category = "trolleys")
    )
    
    /**
     * Get all transit lines
     */
    fun getAllLines(): List<TransitLine> = lines
    
    /**
     * Get a specific line by its number
     * @param lineNumber The line number to search for
     * @return The TransitLine if found, null otherwise
     */
    fun getLineByNumber(lineNumber: String): TransitLine? {
        return lines.find { it.lineNumber.equals(lineNumber, ignoreCase = true) }
    }
    
    /**
     * Get all lines in a specific category
     * @param category The category ("buses" or "trolleys")
     * @return List of transit lines in that category
     */
    fun getLinesByCategory(category: String): List<TransitLine> {
        return lines.filter { it.category.equals(category, ignoreCase = true) }
    }
    
    /**
     * Get all bus lines
     */
    fun getBusLines(): List<TransitLine> = getLinesByCategory("buses")
    
    /**
     * Get all trolley lines
     */
    fun getTrolleyLines(): List<TransitLine> = getLinesByCategory("trolleys")
    
    /**
     * Search for lines matching a query
     * @param query The search query
     * @return List of matching transit lines
     */
    fun searchLines(query: String): List<TransitLine> {
        return lines.filter { it.lineNumber.contains(query, ignoreCase = true) }
    }
    
    /**
     * Get statistics about the repository
     */
    fun getStats(): RepositoryStats {
        return RepositoryStats(
            totalLines = lines.size,
            totalBusLines = getBusLines().size,
            totalTrolleyLines = getTrolleyLines().size,
            totalRoutes = lines.sumOf { it.routeIds.size }
        )
    }
}

/**
 * Statistics about the transit line repository
 */
data class RepositoryStats(
    val totalLines: Int,
    val totalBusLines: Int,
    val totalTrolleyLines: Int,
    val totalRoutes: Int
)
