package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine

/**
 * Platform-specific map view component
 */
@Composable
expect fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
)
