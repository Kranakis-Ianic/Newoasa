package com.example.newoasa.data

/**
 * Example usage of TransitLineRepository
 * 
 * This file demonstrates how to use the TransitLineRepository
 * to access transit line data in your application.
 */

object TransitLineRepositoryExample {
    
    /**
     * Example 1: Get all lines and print statistics
     */
    fun example1_GetAllLines() {
        val allLines = TransitLineRepository.getAllLines()
        
        println("✨ All Transit Lines:")
        println("Total: ${allLines.size}")
        println("Buses: ${allLines.count { it.isBus }}")
        println("Trolleys: ${allLines.count { it.isTrolley }}")
    }
    
    /**
     * Example 2: Get a specific line by number
     */
    fun example2_GetSpecificLine() {
        val lineNumber = "022"
        val line = TransitLineRepository.getLineByNumber(lineNumber)
        
        line?.let {
            println("🚌 Found: ${it.displayName}")
            println("   Category: ${it.category}")
            println("   Base path: ${it.basePath}")
            println("   Routes: ${it.routeIds.size}")
        } ?: println("❌ Line $lineNumber not found")
    }
    
    /**
     * Example 3: List all bus lines
     */
    fun example3_ListBusLines() {
        val busLines = TransitLineRepository.getBusLines()
        
        println("🚌 All Bus Lines (${busLines.size} total):")
        busLines.take(10).forEach { line ->
            println("   - ${line.displayName}")
        }
        if (busLines.size > 10) {
            println("   ... and ${busLines.size - 10} more")
        }
    }
    
    /**
     * Example 4: List all trolley lines
     */
    fun example4_ListTrolleyLines() {
        val trolleyLines = TransitLineRepository.getTrolleyLines()
        
        println("🚎 All Trolley Lines (${trolleyLines.size} total):")
        trolleyLines.forEach { line ->
            println("   - ${line.displayName}")
        }
    }
    
    /**
     * Example 5: Search for lines
     */
    fun example5_SearchLines() {
        val query = "30"
        val results = TransitLineRepository.searchLines(query)
        
        println("🔍 Search results for '$query': (${results.size} found)")
        results.forEach { line ->
            println("   - ${line.displayName}")
        }
    }
    
    /**
     * Example 6: Get repository statistics
     */
    fun example6_GetStatistics() {
        val stats = TransitLineRepository.getStats()
        
        println("📊 Repository Statistics:")
        println("   Total Lines: ${stats.totalLines}")
        println("   Bus Lines: ${stats.totalBusLines}")
        println("   Trolley Lines: ${stats.totalTrolleyLines}")
        println("   Total Routes: ${stats.totalRoutes}")
    }
    
    /**
     * Example 7: Filter lines by criteria
     */
    fun example7_FilterLines() {
        // Get all 700-series bus lines
        val series700 = TransitLineRepository.getBusLines()
            .filter { it.lineNumber.startsWith("7") }
        
        println("🚌 700-series buses (${series700.size} total):")
        series700.forEach { line ->
            println("   - ${line.displayName}")
        }
    }
    
    /**
     * Example 8: Group lines by category
     */
    fun example8_GroupByCategory() {
        val linesByCategory = TransitLineRepository.getAllLines()
            .groupBy { it.category }
        
        println("📋 Lines grouped by category:")
        linesByCategory.forEach { (category, lines) ->
            println("   $category: ${lines.size} lines")
            println("      First 3: ${lines.take(3).map { it.lineNumber }.joinToString(", ")}")
        }
    }
    
    /**
     * Example 9: Check if a line exists
     */
    fun example9_CheckLineExists() {
        val linesToCheck = listOf("022", "999", "1", "ABC")
        
        println("✅ Checking if lines exist:")
        linesToCheck.forEach { lineNumber ->
            val exists = TransitLineRepository.getLineByNumber(lineNumber) != null
            val emoji = if (exists) "✅" else "❌"
            println("   $emoji Line $lineNumber: ${if (exists) "exists" else "not found"}")
        }
    }
    
    /**
     * Example 10: Get lines with Greek characters
     */
    fun example10_GreekCharacterLines() {
        val greekLines = TransitLineRepository.getAllLines()
            .filter { it.lineNumber.any { char -> char in 'Α'..'Ω' || char in 'α'..'ω' } }
        
        println("🇬🇷 Lines with Greek characters (${greekLines.size} total):")
        greekLines.forEach { line ->
            println("   - ${line.displayName}")
        }
    }
    
    /**
     * Run all examples
     */
    fun runAllExamples() {
        println("=".repeat(60))
        println("Transit Line Repository - Usage Examples")
        println("=".repeat(60))
        println()
        
        example1_GetAllLines()
        println()
        
        example2_GetSpecificLine()
        println()
        
        example3_ListBusLines()
        println()
        
        example4_ListTrolleyLines()
        println()
        
        example5_SearchLines()
        println()
        
        example6_GetStatistics()
        println()
        
        example7_FilterLines()
        println()
        
        example8_GroupByCategory()
        println()
        
        example9_CheckLineExists()
        println()
        
        example10_GreekCharacterLines()
        println()
        
        println("=".repeat(60))
    }
}

/**
 * Entry point to run examples
 * You can call this from your main() function or from a test
 */
fun main() {
    TransitLineRepositoryExample.runAllExamples()
}
