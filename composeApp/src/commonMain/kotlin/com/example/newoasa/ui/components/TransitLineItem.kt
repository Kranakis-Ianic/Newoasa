package com.example.newoasa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.TransitLine
import com.example.newoasa.theme.LineColors

/**
 * A list item component for displaying a transit line in search results
 * Shows the line number in a colored square badge with the line name
 */
@Composable
fun TransitLineItem(
    transitLine: TransitLine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get the appropriate color for this line - always use line number
    val lineColor = LineColors.getColorForLine(transitLine.lineNumber)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Line number badge - colored square
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp)) // Small corner radius for square-ish look
                .background(lineColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transitLine.lineNumber,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Line name and details
        Column {
            Text(
                text = transitLine.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${transitLine.routeIds.size} route${if (transitLine.routeIds.size > 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
