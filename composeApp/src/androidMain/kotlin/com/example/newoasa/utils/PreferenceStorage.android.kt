package com.example.newoasa.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.newoasa.data.PreferenceStorage

/**
 * Android implementation using SharedPreferences
 */
class AndroidPreferenceStorage(context: Context) : PreferenceStorage {
    private val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    
    override fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }
    
    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
    
    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
    
    override fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }
    
    override fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}

@Composable
actual fun rememberPreferenceStorage(): PreferenceStorage {
    val context = LocalContext.current
    return remember { AndroidPreferenceStorage(context) }
}
