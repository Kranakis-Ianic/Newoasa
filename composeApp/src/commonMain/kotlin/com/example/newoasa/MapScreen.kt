package com.example.newoasa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.newoasa.data.TransitLine
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    currentThemeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit,
    isDark: Boolean // Calculated effective theme
) {
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.Map) }
    var selectedTransitLine by remember { mutableStateOf<TransitLine?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle Back Press to close drawer if open
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        // Allow gestures (sliding) ONLY when the drawer is already open (to close it)
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            AppDrawer(
                currentThemeMode = currentThemeMode,
                onThemeChange = { 
                    onThemeChange(it)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopSearchBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onLineSelected = { line ->
                        selectedTransitLine = line
                        // Switch to map view if not already there
                        if (selectedBottomItem != BottomNavItem.Map) {
                            selectedBottomItem = BottomNavItem.Map
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(
                    selectedItem = selectedBottomItem,
                    onItemSelected = { selectedBottomItem = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedBottomItem) {
                    BottomNavItem.Map -> MapView(
                        modifier = Modifier.fillMaxSize(),
                        isDark = isDark,
                        selectedLine = selectedTransitLine
                    )
                    BottomNavItem.Trip -> CenteredText("Trip Planner")
                    BottomNavItem.Favorites -> CenteredText("Favorites")
                }
            }
        }
    }
}

@Composable
fun CenteredText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}
