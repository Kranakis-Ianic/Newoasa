package com.example.newoasa

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS usually relies on Swipe-to-back or UI buttons, but if needed we can hook into UIGestureRecognizer
    // For now, this is a no-op as standard back handling differs.
}
