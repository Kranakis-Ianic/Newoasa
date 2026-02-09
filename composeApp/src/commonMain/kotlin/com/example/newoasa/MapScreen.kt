package com.example.newoasa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine

@Composable
fun MapScreen(
    isDark: Boolean,
    selectedLine: TransitLine?,
    modifier: Modifier = Modifier
) {
    MapView(
        modifier = modifier.fillMaxSize(),
        isDark = isDark,
        selectedLine = selectedLine
    )
}
