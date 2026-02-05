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
    // We keep the theme check if we want to add overlay adjustments later,
    // but for the base map, we'll stick to the lighter Voyager style as requested.
    val isDark = isSystemInDarkTheme()
    
    // Initialize OSMDroid configuration
    remember {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // CartoDB Voyager (Clean, pastel, readable) - Used for BOTH modes now
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
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(37.9838, 23.7275)) // Athens
            setTileSource(voyagerTileSource)
        }
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
