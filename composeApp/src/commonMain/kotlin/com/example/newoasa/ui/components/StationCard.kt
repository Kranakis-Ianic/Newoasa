package com.example.newoasa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newoasa.model.TransitStation
import com.example.newoasa.theme.LineColors

@Composable
fun StationCard(
    station: TransitStation,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = station.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!station.nameEn.isNullOrEmpty() && station.nameEn != station.name) {
                        Text(
                            text = station.nameEn,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                IconButton(onClick = onDismiss) {
                    Text(
                        text = "✕",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Lines section
            Text(
                text = "Lines",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Line badges - sorted by category and line number
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                station.lines.sortedWith(
                    compareBy<TransitStation.StationLine> { 
                        when(it.category.lowercase()) {
                            "metro" -> 0
                            "tram" -> 1
                            "suburban" -> 2
                            "trolleys" -> 3
                            else -> 4
                        }
                    }.thenBy { it.lineNumber }
                ).forEach { line ->
                    LineBadge(
                        lineNumber = line.lineNumber,
                        category = line.category
                    )
                }
            }
        }
    }
}

@Composable
private fun LineBadge(
    lineNumber: String,
    category: String
) {
    // Get the proper line color using LineColors utility
    val lineCode = when (category.lowercase()) {
        "metro" -> "M$lineNumber"
        "tram" -> "T$lineNumber"
        "suburban" -> lineNumber // Suburban uses A1, A2, etc.
        else -> lineNumber
    }
    
    // Get official color for this specific line
    val backgroundColor = LineColors.getColorForLine(lineCode)
    
    // Determine text color based on background brightness
    // Using relative luminance formula: (0.299*R + 0.587*G + 0.114*B)
    val red = (backgroundColor.value shr 16 and 0xFF) / 255f
    val green = (backgroundColor.value shr 8 and 0xFF) / 255f
    val blue = (backgroundColor.value and 0xFF) / 255f
    val luminance = 0.299f * red + 0.587f * green + 0.114f * blue
    val textColor = if (luminance > 0.5f) Color.Black else Color.White
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val prefix = when (category.lowercase()) {
                "metro" -> "M"
                "tram" -> "T"
                "suburban" -> "" // Suburban lines already have A1, A2 format
                "trolleys" -> ""
                else -> ""
            }
            
            val displayText = if (prefix.isNotEmpty() && !lineNumber.startsWith(prefix)) {
                "$prefix$lineNumber"
            } else {
                lineNumber
            }
            
            Text(
                text = displayText,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // Simple FlowRow implementation
    // For production, use androidx.compose.foundation.layout.FlowRow
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}