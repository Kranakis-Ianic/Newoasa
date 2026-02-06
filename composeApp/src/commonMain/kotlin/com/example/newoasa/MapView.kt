package com.example.newoasa

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine

@Composable
expect fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
)
