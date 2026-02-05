package com.example.newoasa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MapScreen() {
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.Map) }

    Scaffold(
        topBar = {
            TopSearchBar()
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

@Composable
fun CenteredText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}
