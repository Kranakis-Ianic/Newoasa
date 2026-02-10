package com.example.newoasa.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.TransitLine
import com.example.newoasa.data.TransitLineRepository
import com.example.newoasa.ui.components.TransitLineItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    onMenuClick: () -> Unit,
    onLineSelected: (TransitLine) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filteredLines by remember { mutableStateOf<List<TransitLine>>(emptyList()) }

    // Filter logic
    LaunchedEffect(searchQuery) {
        filteredLines = if (searchQuery.isBlank()) {
            emptyList()
        } else {
            TransitLineRepository.searchLines(searchQuery)
        }
    }

    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = { isSearchActive = false },
        active = isSearchActive,
        onActiveChange = { isSearchActive = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isSearchActive) 0.dp else 16.dp),
        placeholder = { Text("Search lines or stops") },
        leadingIcon = {
            if (isSearchActive) {
                IconButton(onClick = {
                    isSearchActive = false
                    searchQuery = ""
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Close search")
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        },
        trailingIcon = {
            if (isSearchActive && searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            } else if (!isSearchActive) {
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    ) {
        // Search results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (filteredLines.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "No lines found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Group lines by category
                val groupedLines = filteredLines.groupBy { it.category }
                
                groupedLines.forEach { (category, lines) ->
                    item {
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    items(lines) { line ->
                        TransitLineItem(
                            transitLine = line,
                            onClick = {
                                onLineSelected(line)
                                isSearchActive = false
                                searchQuery = ""
                            }
                        )
                    }
                }
            }
        }
    }
}
