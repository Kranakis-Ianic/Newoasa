package com.example.newoasa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.TransitLine
import com.example.newoasa.theme.LineColors

/**
 * A list item component for displaying a transit line in search results
 */
@Composable
fun TransitLineItem(
    transitLine: TransitLine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lineColor = LineColors.getColorForCategory(transitLine.category, transitLine.isBus)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Line number badge
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(lineColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transitLine.lineNumber,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Line details
        Column {
            Text(
                text = transitLine.displayName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${transitLine.routeIds.size} route${if (transitLine.routeIds.size > 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
