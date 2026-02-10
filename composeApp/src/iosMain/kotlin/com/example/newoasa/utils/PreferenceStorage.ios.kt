package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.newoasa.data.PreferenceStorage
import platform.Foundation.NSUserDefaults

/**
 * iOS implementation using NSUserDefaults
 */
class IosPreferenceStorage : PreferenceStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    
    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }
    
    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.boolForKey(key)
        } else {
            defaultValue
        }
    }
    
    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }
    
    override fun getInt(key: String, defaultValue: Int): Int {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.integerForKey(key).toInt()
        } else {
            defaultValue
        }
    }
    
    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }
}

@Composable
actual fun rememberPreferenceStorage(): PreferenceStorage {
    return remember { IosPreferenceStorage() }
}
