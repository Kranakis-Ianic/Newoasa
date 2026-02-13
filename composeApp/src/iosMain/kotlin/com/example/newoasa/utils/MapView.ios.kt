package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.example.newoasa.data.TransitLine
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSURL
import platform.MapLibre.MLNMapView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val styleUrl = remember(isDark) {
        if (isDark) {
            "https://tiles.openfreemap.org/styles/dark"
        } else {
            "https://tiles.openfreemap.org/styles/bright"
        }
    }

    UIKitView(
        factory = {
            MLNMapView().apply {
                // Set map style
                val url = NSURL.URLWithString(styleUrl)
                if (url != null) {
                    setStyleURL(url)
                }

                // Center on Athens, Greece
                val athensCoordinate = CLLocationCoordinate2DMake(
                    latitude = 37.9838,
                    longitude = 23.7275
                )
                setCenterCoordinate(
                    centerCoordinate = athensCoordinate,
                    zoomLevel = 11.0,
                    animated = false
                )

                // Enable user interaction
                setZoomEnabled(true)
                setScrollEnabled(true)
                setRotateEnabled(true)
                setPitchEnabled(false)
            }
        },
        modifier = modifier,
        update = { mapView ->
            // Update style when theme changes
            val url = NSURL.URLWithString(styleUrl)
            if (url != null) {
                mapView.setStyleURL(url)
            }
        }
    )

    remember(Unit) {
        onMapReady()
        null
    }
}