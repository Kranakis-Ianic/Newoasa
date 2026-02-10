package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import com.example.newoasa.data.PreferenceStorage

/**
 * Platform-specific composable function to get preference storage
 * Uses SharedPreferences on Android, UserDefaults on iOS
 */
@Composable
expect fun rememberPreferenceStorage(): PreferenceStorage
