package com.example.newoasa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.StationInfo
import com.example.newoasa.theme.LineColors

@Composable
fun StationCard(
    station: StationInfo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with station name and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = station.nameEn ?: station.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (station.nameEn != null && station.nameEn != station.name) {
                            Text(
                                text = station.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lines section
            Text(
                text = "Lines stopping here",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // List of lines
            station.lines.forEach { line ->
                LineItem(
                    lineRef = line.ref,
                    lineName = line.name,
                    lineColor = line.colour,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            // Wheelchair accessibility
            if (station.wheelchair) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "♿",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Wheelchair accessible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LineItem(
    lineRef: String,
    lineName: String?,
    lineColor: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Line badge
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(parseHexColor(lineColor)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = lineRef,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Line name
        Text(
            text = extractLineDescription(lineName, lineRef),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Parse a hex color string to a Compose Color
 * Supports formats: #RGB, #RRGGBB, #AARRGGBB
 */
private fun parseHexColor(colorString: String?): Color {
    if (colorString.isNullOrBlank()) {
        return Color(0xFF666666) // Default gray
    }
    
    return try {
        val hex = colorString.removePrefix("#")
        
        when (hex.length) {
            3 -> {
                // #RGB -> #RRGGBB
                val r = hex[0].toString().repeat(2)
                val g = hex[1].toString().repeat(2)
                val b = hex[2].toString().repeat(2)
                Color((0xFF000000L or (r.toLong(16) shl 16) or (g.toLong(16) shl 8) or b.toLong(16)))
            }
            6 -> {
                // #RRGGBB -> #FFRRGGBB
                Color((0xFF000000L or hex.toLong(16)))
            }
            8 -> {
                // #AARRGGBB
                Color(hex.toLong(16))
            }
            else -> Color(0xFF666666) // Invalid format, use gray
        }
    } catch (e: Exception) {
        Color(0xFF666666) // Error parsing, use gray
    }
}

/**
 * Extract a user-friendly description from the full line name
 */
private fun extractLineDescription(fullName: String?, lineRef: String): String {
    if (fullName == null) return lineRef
    
    // Try to extract the English name part or direction
    val parts = fullName.split(":")
    return if (parts.size > 1) {
        parts[1].trim().replace("→", "to")
    } else {
        fullName
    }
}
