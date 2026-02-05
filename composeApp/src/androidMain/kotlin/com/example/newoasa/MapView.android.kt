package com.example.newoasa

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

@Composable
actual fun MapView(modifier: Modifier) {
    val athens = LatLng(37.9838, 23.7275)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(athens, 12f)
    }
    
    val mapProperties = MapProperties(
        isTrafficEnabled = false, // Removed traffic as requested
        isMyLocationEnabled = false
    )
    
    // Explicitly enable gestures to ensure interaction works well
    val mapUiSettings = MapUiSettings(
        myLocationButtonEnabled = false,
        scrollGesturesEnabled = true,
        zoomGesturesEnabled = true,
        tiltGesturesEnabled = true,
        rotationGesturesEnabled = true,
        zoomControlsEnabled = false // Hide default zoom buttons for cleaner UI
    )

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings
    )
}
