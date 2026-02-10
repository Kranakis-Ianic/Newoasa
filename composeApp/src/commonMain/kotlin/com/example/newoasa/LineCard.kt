package com.example.newoasa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.TransitLine

/**
 * Card component for displaying transit line information with color-coded badge
 */
@Composable
fun LineCard(
    line: TransitLine,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    showRouteCount: Boolean = true
) {
    val lineColor = LineColors.getColorForCategory(line.category, line.isBus)
    
    Surface(
        onClick = onClick ?: {},
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        enabled = onClick != null
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Line number badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = lineColor.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .size(width = 64.dp, height = 48.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = line.lineNumber,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = lineColor
                        )
                    }
                }
                
                // Line details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = line.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Category badge
                    Text(
                        text = line.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = lineColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (showRouteCount) {
                        Text(
                            text = "${line.routeIds.size} Route${if (line.routeIds.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact line badge component for minimal displays
 */
@Composable
fun LineBadge(
    line: TransitLine,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 56.dp
) {
    val lineColor = LineColors.getColorForCategory(line.category, line.isBus)
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = lineColor.copy(alpha = 0.2f)
        ),
        modifier = modifier
            .size(width = size, height = size * 0.7f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = line.lineNumber,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = lineColor
            )
        }
    }
}
