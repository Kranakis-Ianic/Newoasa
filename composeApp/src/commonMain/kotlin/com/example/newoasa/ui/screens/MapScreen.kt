package com.example.newoasa.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.utils.MapView
import com.example.newoasa.data.TransitLine

@Composable
fun MapScreen(isDark: Boolean, selectedLine: TransitLine? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        MapView(isDark = isDark, selectedLine = selectedLine)
    }
}
