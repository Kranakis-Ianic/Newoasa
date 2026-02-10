package com.example.newoasa.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newoasa.AppThemeMode
import com.example.newoasa.theme.ThemeSegmentedSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentThemeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "New OASA",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            HorizontalDivider()
            
            // Push theme selector to bottom
            Spacer(modifier = Modifier.weight(1f))
            
            // Theme selector at bottom
            Column {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                ThemeSegmentedSelector(
                    currentMode = currentThemeMode,
                    onModeChange = onThemeChange
                )
            }
        }
    }
}
