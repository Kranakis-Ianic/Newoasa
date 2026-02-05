package com.example.newoasa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MapScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.Map) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { /* Handle click */ }
                )
                NavigationDrawerItem(
                    label = { Text("About") },
                    selected = false,
                    onClick = { /* Handle click */ }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopSearchBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
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
                    BottomNavItem.Map -> MapView(modifier = Modifier.fillMaxSize())
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
