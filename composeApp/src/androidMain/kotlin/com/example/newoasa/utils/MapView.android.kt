package com.example.newoasa.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    LaunchedEffect(Unit) {
        onMapReady()
    }
    
    // Placeholder map view
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🗺️ Athens Transit Map\n\nCentered: 37.9838°N, 23.7275°E\nZoom: 11\n\nInteractive map coming soon",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
