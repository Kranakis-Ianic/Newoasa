import java.io.File

/**
 * Code generator script that scans the GeoJSON folder structure
 * and generates Kotlin code for TransitLineRepository
 * 
 * Run this script whenever you add new transit lines to the geojson folder
 * Usage: kotlinc -script GenerateTransitLineRepository.kt
 */

fun generateTransitLineRepositoryCode() {
    val basePath = "composeApp/src/commonMain/composeResources/files/geojson"
    val outputFile = "composeApp/src/commonMain/kotlin/com/example/newoasa/data/TransitLineRepository.kt"
    
    println("🔍 Scanning GeoJSON folders...")
    
    val categories = listOf("buses", "trolleys")
    val transitLines = mutableListOf<String>()
    var totalRoutes = 0
    
    for (category in categories) {
        val categoryDir = File("$basePath/$category")
        
        if (categoryDir.exists() && categoryDir.isDirectory) {
            val lineDirs = categoryDir.listFiles()?.filter { it.isDirectory }?.sortedBy { it.name } ?: emptyList()
            println("   Found ${lineDirs.size} $category lines")
            
            lineDirs.forEach { lineDir ->
                val lineNumber = lineDir.name
                val routeFiles = lineDir.listFiles()
                    ?.filter { it.name.endsWith(".geojson") }
                    ?.sortedBy { it.name }
                    ?: emptyList()
                
                if (routeFiles.isNotEmpty()) {
                    val routeIds = routeFiles.mapNotNull { fileName ->
                        Regex("""route_(\d+)\.geojson""").find(fileName.name)?.groupValues?.get(1)
                    }
                    
                    val routePaths = routeFiles.map { 
                        "\"files/geojson/$category/$lineNumber/${it.name}\""
                    }.joinToString(", ")
                    
                    totalRoutes += routeFiles.size
                    
                    transitLines.add("""
        TransitLine(
            lineNumber = "$lineNumber",
            category = "$category",
            routeIds = listOf(${routeIds.joinToString(", ") { "\"$it\"" }}),
            routePaths = listOf($routePaths)
        )""".trimIndent())
                }
            }
        } else {
            println("   ⚠️  Category directory not found: $categoryDir")
        }
    }
    
    val code = """
package com.example.newoasa.data

/**
 * Auto-generated Transit Line Repository
 * Generated from GeoJSON folder structure
 * 
 * DO NOT EDIT MANUALLY!
 * Run scripts/GenerateTransitLineRepository.kt to regenerate this file
 * 
 * Generated: ${java.time.LocalDateTime.now()}
 * Total Lines: ${transitLines.size}
 * Total Routes: $totalRoutes
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
    val routeIds: List<String>,
    val routePaths: List<String>
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
}

/**
 * Repository providing access to all transit lines
 * This is a singleton object that holds all transit line data
 */
object TransitLineRepository {
    private val lines: List<TransitLine> = listOf(
${transitLines.joinToString(",\n")}
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
""".trimIndent()
    
    // Create directory if it doesn't exist
    val outputFileObj = File(outputFile)
    outputFileObj.parentFile.mkdirs()
    
    // Write the generated code
    outputFileObj.writeText(code)
    
    println("\n✅ Successfully generated TransitLineRepository!")
    println("   Output: $outputFile")
    println("   Total lines: ${transitLines.size}")
    println("   Total routes: $totalRoutes")
    println("   Buses: ${transitLines.count { it.contains("\"buses\"") }}")
    println("   Trolleys: ${transitLines.count { it.contains("\"trolleys\"") }}")
}

// Run the generator
fun main() {
    try {
        generateTransitLineRepositoryCode()
    } catch (e: Exception) {
        println("\n❌ Error generating repository: ${e.message}")
        e.printStackTrace()
    }
}

// Execute main
main()
