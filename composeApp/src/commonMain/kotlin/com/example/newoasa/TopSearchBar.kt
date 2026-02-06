package com.example.newoasa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newoasa.data.TransitLine
import com.example.newoasa.data.TransitLineRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    onMenuClick: () -> Unit = {},
    onLineSelected: (TransitLine) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<TransitLine>>(emptyList()) }

    SearchBar(
        query = query,
        onQueryChange = { newQuery ->
            query = newQuery
            searchResults = if (newQuery.isNotEmpty()) {
                TransitLineRepository.searchLines(newQuery)
            } else {
                emptyList()
            }
        },
        onSearch = { 
            active = false
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search transit lines") },
        leadingIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search results
        LazyColumn {
            items(searchResults) { line ->
                ListItem(
                    headlineContent = { 
                        Text(
                            text = line.displayName,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = { 
                        Text("${line.category} • ${line.routeIds.size} route(s)")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLineSelected(line)
                            active = false
                            query = line.lineNumber
                        }
                )
                HorizontalDivider()
            }
            
            if (query.isNotEmpty() && searchResults.isEmpty()) {
                item {
                    ListItem(
                        headlineContent = { Text("No results found") },
                        supportingContent = { Text("Try a different search term") }
                    )
                }
            }
        }
    }
}
