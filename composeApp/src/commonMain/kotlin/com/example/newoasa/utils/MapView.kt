package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine

@Composable
expect fun MapView(isDark: Boolean, selectedLine: TransitLine? = null, modifier: Modifier = Modifier)
