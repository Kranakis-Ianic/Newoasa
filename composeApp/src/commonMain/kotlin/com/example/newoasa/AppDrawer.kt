package com.example.newoasa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentThemeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(300.dp),
        drawerContainerColor = OasaDeepBlue,
        drawerContentColor = OasaWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            // Header
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = OasaWhite
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Theme Section
            Text(
                text = "Map Theme",
                style = MaterialTheme.typography.titleMedium,
                color = OasaAccentBlue
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ThemeOption(
                label = "Light",
                selected = currentThemeMode == AppThemeMode.Light,
                onClick = { onThemeChange(AppThemeMode.Light) }
            )
            ThemeOption(
                label = "Dark",
                selected = currentThemeMode == AppThemeMode.Dark,
                onClick = { onThemeChange(AppThemeMode.Dark) }
            )
            ThemeOption(
                label = "Auto (System)",
                selected = currentThemeMode == AppThemeMode.Auto,
                onClick = { onThemeChange(AppThemeMode.Auto) }
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Language Section
            Text(
                text = "Language",
                style = MaterialTheme.typography.titleMedium,
                color = OasaAccentBlue
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* Change Language Logic */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OasaAccentBlue,
                    contentColor = OasaWhite
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Language, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change Language")
            }
        }
    }
}
