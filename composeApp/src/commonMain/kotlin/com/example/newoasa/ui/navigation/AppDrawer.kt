package com.example.newoasa.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newoasa.AppThemeMode
import com.example.newoasa.theme.ThemeOption

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
            
            Divider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Theme selection
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ThemeOption(
                    icon = Icons.Default.Settings,
                    label = "Auto (System)",
                    isSelected = currentThemeMode == AppThemeMode.Auto,
                    onClick = { onThemeChange(AppThemeMode.Auto) }
                )
                ThemeOption(
                    icon = Icons.Default.LightMode,
                    label = "Light",
                    isSelected = currentThemeMode == AppThemeMode.Light,
                    onClick = { onThemeChange(AppThemeMode.Light) }
                )
                ThemeOption(
                    icon = Icons.Default.DarkMode,
                    label = "Dark",
                    isSelected = currentThemeMode == AppThemeMode.Dark,
                    onClick = { onThemeChange(AppThemeMode.Dark) }
                )
            }
        }
    }
}
