package com.example.newoasa

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("Map not implemented for iOS")
    }
}
