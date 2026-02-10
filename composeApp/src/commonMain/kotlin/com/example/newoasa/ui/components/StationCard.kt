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
            
            // Line badges
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
    val backgroundColor = when (category.lowercase()) {
        "metro" -> Color(0xFF007A33) // Metro green
        "tram" -> Color(0xFFFFD100) // Tram yellow
        "suburban" -> Color(0xFF0066CC) // Suburban blue
        "trolleys" -> Color(0xFFFF6600) // Trolley orange
        else -> Color(0xFF666666)
    }
    
    val textColor = if (category.lowercase() == "tram") Color.Black else Color.White
    
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
                "suburban" -> "S"
                else -> ""
            }
            
            Text(
                text = if (prefix.isNotEmpty()) "$prefix$lineNumber" else lineNumber,
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