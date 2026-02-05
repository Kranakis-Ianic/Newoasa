package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
actual fun MapView(modifier: Modifier) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    
    // Initialize OSMDroid configuration
    remember {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Light Mode: CartoDB Voyager
    val lightTileSource = remember {
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

    // Dark Mode: CartoDB Dark Matter
    val darkTileSource = remember {
        XYTileSource(
            "CartoDBDarkMatter",
            0, 19, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/rastertiles/dark_all/",
                "https://b.basemaps.cartocdn.com/rastertiles/dark_all/",
                "https://c.basemaps.cartocdn.com/rastertiles/dark_all/",
                "https://d.basemaps.cartocdn.com/rastertiles/dark_all/"
            )
        )
    }

    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            isTilesScaledToDpi = true
            minZoomLevel = 10.0
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(37.9838, 23.7275)) // Athens
        }
    }

    // Update tiles when theme changes
    LaunchedEffect(isDark) {
        mapView.setTileSource(if (isDark) darkTileSource else lightTileSource)
        mapView.invalidate() // Redraw
    }

    DisposableEffect(mapView) {
        onDispose {
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
