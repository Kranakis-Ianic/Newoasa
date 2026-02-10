package com.example.newoasa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.Stop

/**
 * Info card for displaying transit stop/station information
 * Positioned above map center when a stop is selected
 */
@Composable
fun StopInfoCard(
    stop: Stop,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Position the card centered horizontally and in the upper portion of screen
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClose), // Close when clicking outside
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 200.dp) // Offset upward so it appears above the centered pin
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .clickable(onClick = {}), // Prevent closing when clicking inside
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with close button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stop.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Stop details
            if (stop.stopCode.isNotEmpty()) {
                Text(
                    text = "Stop Code: ${stop.stopCode}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            if (stop.order.isNotEmpty()) {
                Text(
                    text = "Order: ${stop.order}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
