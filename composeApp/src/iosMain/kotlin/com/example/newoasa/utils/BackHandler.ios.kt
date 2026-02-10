package com.example.newoasa.utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS handles back navigation via swipe gestures by default
    // No additional implementation needed
}
