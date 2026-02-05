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
    
    // Enable traffic layer which is the closest to "transport" layer in SDK
    val mapProperties = MapProperties(
        isTrafficEnabled = true,
        isMyLocationEnabled = false // Need permission check for true
    )
    
    val mapUiSettings = MapUiSettings(
        myLocationButtonEnabled = false // Need permission check for true
    )

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings
    )
}
