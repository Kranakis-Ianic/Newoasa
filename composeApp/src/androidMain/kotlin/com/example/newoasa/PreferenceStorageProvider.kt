package com.example.newoasa

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.newoasa.data.AndroidPreferenceStorage
import com.example.newoasa.data.PreferenceStorage

/**
 * Provides platform-specific PreferenceStorage
 * On Android, uses SharedPreferences
 */
@Composable
actual fun rememberPreferenceStorage(): PreferenceStorage {
    val context = LocalContext.current
    return remember {
        AndroidPreferenceStorage(context.applicationContext)
    }
}
