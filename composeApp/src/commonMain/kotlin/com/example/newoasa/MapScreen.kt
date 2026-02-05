package com.example.newoasa

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
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    currentThemeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit,
    isDark: Boolean // Calculated effective theme
) {
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.Map) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false, // Sidebar cannot be opened by sliding
        drawerContent = {
            AppDrawer(
                currentThemeMode = currentThemeMode,
                onThemeChange = { 
                    onThemeChange(it)
                    // Optional: Close drawer on selection? Maybe not, user might want to toggle back.
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopSearchBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
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
                        isDark = isDark
                    )
                    BottomNavItem.Trip -> CenteredText("Trip Planner")
                    BottomNavItem.Favorites -> CenteredText("Favorites")
                }
            }
        }
    }
}
