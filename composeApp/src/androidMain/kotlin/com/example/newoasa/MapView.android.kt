package com.example.newoasa

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView

@Composable
actual fun MapView(modifier: Modifier) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    
    // Initialize OSMDroid configuration
    remember {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // CartoDB Voyager Tile Source
    val voyagerTileSource = remember {
        XYTileSource(
            "CartoDBVoyager",
            0, 19, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://c.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://d.basemaps.cartocdn.com/rastertiles/voyager/"
            )
        )
    }

    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            isTilesScaledToDpi = true
            minZoomLevel = 10.0
            
            // Disable default zoom buttons
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(37.9838, 23.7275)) // Athens
            setTileSource(voyagerTileSource)
        }
    }

    // Apply Grayscale Filter in Dark Mode to make map "Grey"
    LaunchedEffect(isDark) {
        if (isDark) {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f) // Make it grayscale (Grey)
            
            // Optional: Invert it to make it dark grey? 
            // The user asked for "Grey" after finding "Dark Matter" too dark.
            // A simple Grayscale Voyager is a nice light/mid-grey.
            // If they want "Dark Grey", we could invert, but let's stick to "Grey".
            
            val filter = ColorMatrixColorFilter(matrix)
            mapView.overlayManager.tilesOverlay.setColorFilter(filter)
        } else {
            mapView.overlayManager.tilesOverlay.setColorFilter(null)
        }
        mapView.invalidate()
    }

    DisposableEffect(mapView) {
        onDispose {
            mapView.onDetach()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
        
        // Custom Zoom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { mapView.controller.zoomIn() },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }
            
            SmallFloatingActionButton(
                onClick = { mapView.controller.zoomOut() },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }
        }
    }
}
