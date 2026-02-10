package com.example.newoasa

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.newoasa.data.ThemePreferences
import com.example.newoasa.data.TransitLine
import com.example.newoasa.data.rememberThemeMode
import com.example.newoasa.theme.NewOasaTheme
import com.example.newoasa.ui.navigation.AppDrawer
import com.example.newoasa.ui.navigation.BottomNavBar
import com.example.newoasa.ui.navigation.BottomNavItem
import com.example.newoasa.ui.navigation.TopSearchBar
import com.example.newoasa.ui.screens.FavoritesScreen
import com.example.newoasa.ui.screens.MapScreen
import com.example.newoasa.ui.screens.TripScreen
import com.example.newoasa.utils.BackHandler
import com.example.newoasa.utils.rememberPreferenceStorage
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    // Get platform-specific preference storage
    val preferenceStorage = rememberPreferenceStorage()
    
    // Create theme preferences manager
    val themePreferences = remember { 
        ThemePreferences(preferenceStorage) 
    }
    
    // Load saved theme mode and persist changes automatically
    var themeMode by rememberThemeMode(themePreferences)
    
    // Calculate if we should use Dark Theme features (e.g. Map style)
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.Auto -> isSystemDark
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }

    NewOasaTheme(useDarkTheme = isDark) {
        // App State
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
                    currentThemeMode = themeMode,
                    onThemeChange = { themeMode = it }
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
                        BottomNavItem.Map -> MapScreen(
                            isDark = isDark,
                            selectedLine = selectedTransitLine
                        )
                        BottomNavItem.Trip -> TripScreen()
                        BottomNavItem.Favorites -> FavoritesScreen()
                    }
                }
            }
        }
    }
}
