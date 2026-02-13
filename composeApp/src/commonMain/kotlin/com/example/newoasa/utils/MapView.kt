package com.example.newoasa.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import org.maplibre.compose.MaplibreMap
import org.maplibre.compose.ramani.MapLibre
import org.maplibre.compose.rememberSaveableMapViewCameraState

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    selectedLine: TransitLine? = null,
    onMapReady: () -> Unit = {}
) {
    val cameraState = rememberSaveableMapViewCameraState()

    MaplibreMap(
        modifier = modifier.fillMaxSize(),
        cameraState = cameraState
    )
    
    LaunchedEffect(Unit) {
        onMapReady()
    }

}