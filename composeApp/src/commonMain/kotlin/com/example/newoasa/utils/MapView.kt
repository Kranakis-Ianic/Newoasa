package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.style.BaseStyle

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
) {
    val cameraState = rememberCameraState()

    MaplibreMap(
        modifier = modifier.fillMaxSize(),
        cameraState = cameraState,
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json")
    )
    
    LaunchedEffect(Unit) {
        onMapReady()
    }
}
