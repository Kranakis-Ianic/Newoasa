package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.newoasa.data.PreferenceStorage

@Composable
expect fun rememberPreferenceStorage(): PreferenceStorage
