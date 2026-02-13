package com.example.newoasa.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.example.newoasa.data.TransitLine
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.UIKit.UIView
import cocoapods.MapLibre.MLNMapView
import cocoapods.MapLibre.MLNMapViewDelegateProtocol
import cocoapods.MapLibre.MLNStyle
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean,
    selectedLine: TransitLine?,
    onMapReady: () -> Unit
) {
    val mapView = remember {
        MLNMapView().apply {
            // Set style URL based on theme
            val styleURL = if (isDark) {
                "https://tiles.openfreemap.org/styles/dark"
            } else {
                "https://tiles.openfreemap.org/styles/bright"
            }
            
            setStyleURL(platform.Foundation.NSURL.URLWithString(styleURL)!!)
            
            // Set initial camera position to Athens
            val athensCoordinate = CLLocationCoordinate2DMake(
                latitude = 37.9838,
                longitude = 23.7275
            )
            
            setCenterCoordinate(
                coordinate = athensCoordinate,
                zoomLevel = 11.0,
                animated = false
            )
            
            // Create and set delegate
            val delegate = object : NSObject(), MLNMapViewDelegateProtocol {
                override fun mapView(mapView: MLNMapView, didFinishLoadingStyle: MLNStyle) {
                    onMapReady()
                }
            }
            setDelegate(delegate)
        }
    }

    UIKitView(
        factory = { mapView },
        modifier = modifier
    )
}