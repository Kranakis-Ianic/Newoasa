package com.example.newoasa

import androidx.compose.runtime.Composable
import com.example.newoasa.data.PreferenceStorage

/**
 * Platform-specific PreferenceStorage provider
 * Each platform implements this to provide its own storage mechanism
 */
@Composable
expect fun rememberPreferenceStorage(): PreferenceStorage
