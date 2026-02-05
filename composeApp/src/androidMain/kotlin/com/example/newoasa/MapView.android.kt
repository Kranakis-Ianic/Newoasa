package com.example.newoasa

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
actual fun MapView(
    modifier: Modifier,
    isDark: Boolean
) {
    val context = LocalContext.current

    // Initialize MapLibre
    remember {
        MapLibre.getInstance(context)
    }

    // Styles for OpenFreeMap
    val lightStyle = "https://tiles.openfreemap.org/styles/positron"
    val darkStyle = "https://tiles.openfreemap.org/styles/dark"

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    var locationPermissionGranted by remember { mutableStateOf(hasLocationPermission()) }
    var followUser by remember { mutableStateOf(false) }

    val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        locationPermissionGranted = fine || coarse
    }

    // Keep reference to the map object to update style / camera
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }

    @SuppressLint("MissingPermission")
    fun enableUserLocation(map: MapLibreMap, style: Style, track: Boolean) {
        val locationComponentOptions = LocationComponentOptions.builder(context)
            .pulseEnabled(true)
            .build()

        val activationOptions = LocationComponentActivationOptions
            .builder(context, style)
            .locationComponentOptions(locationComponentOptions)
            .useDefaultLocationEngine(true)
            .build()

        val locationComponent = map.locationComponent
        locationComponent.activateLocationComponent(activationOptions)
        locationComponent.isLocationComponentEnabled = true
        locationComponent.renderMode = RenderMode.COMPASS
        locationComponent.cameraMode = if (track) CameraMode.TRACKING else CameraMode.NONE
    }

    fun applyStyle(map: MapLibreMap) {
        val url = if (isDark) darkStyle else lightStyle
        map.setStyle(url) { style ->
            if (locationPermissionGranted) {
                enableUserLocation(map, style, track = followUser)
            }
        }
    }

    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    // Handle Style Switching
    LaunchedEffect(isDark, mapRef.value, locationPermissionGranted, followUser) {
        mapRef.value?.let { applyStyle(it) }
    }

    DisposableEffect(mapView) {
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        mapRef.value = map
                        
                        // Initial Camera Position (Athens)
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(37.9838, 23.7275))
                            .zoom(12.0)
                            .build()

                        map.uiSettings.isLogoEnabled = false
                        map.uiSettings.isAttributionEnabled = true
                        map.uiSettings.isCompassEnabled = false
                        map.uiSettings.isRotateGesturesEnabled = true
                        map.uiSettings.isTiltGesturesEnabled = true

                        applyStyle(map)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Locate Me
            SmallFloatingActionButton(
                onClick = {
                    followUser = true
                    if (!locationPermissionGranted) {
                        requestLocationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    } else {
                        val map = mapRef.value
                        val style = map?.style
                        if (map != null && style != null) {
                            enableUserLocation(map, style, track = true)
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }

            // Zoom In
            SmallFloatingActionButton(
                onClick = {
                    val map = mapRef.value ?: return@SmallFloatingActionButton
                    val currentZoom = map.cameraPosition.zoom
                    map.animateCamera(
                        org.maplibre.android.camera.CameraUpdateFactory.newLatLngZoom(
                            map.cameraPosition.target,
                            currentZoom + 1
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            // Zoom Out
            SmallFloatingActionButton(
                onClick = {
                    val map = mapRef.value ?: return@SmallFloatingActionButton
                    val currentZoom = map.cameraPosition.zoom
                    map.animateCamera(
                        org.maplibre.android.camera.CameraUpdateFactory.newLatLngZoom(
                            map.cameraPosition.target,
                            currentZoom - 1
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }
        }
    }
}
