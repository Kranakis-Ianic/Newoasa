package com.example.newoasa.utils

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic back handler
 * Handles back button press on Android, swipe back on iOS
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
