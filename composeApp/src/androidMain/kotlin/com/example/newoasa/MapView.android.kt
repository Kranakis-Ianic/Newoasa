package com.example.newoasa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
    
    // Initialize OSMDroid configuration
    remember {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Define Public Transport Tile Source (ÖPNVKarte)
    val transportTileSource = remember {
        XYTileSource(
            "PublicTransport",
            0, 18, 256, ".png",
            arrayOf("https://tileserver.memomaps.de/tilegen/")
        )
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(transportTileSource)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(37.9838, 23.7275)) // Athens
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
