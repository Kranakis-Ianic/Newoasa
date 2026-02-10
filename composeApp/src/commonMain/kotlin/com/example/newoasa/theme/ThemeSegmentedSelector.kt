package com.example.newoasa.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newoasa.AppThemeMode

/**
 * Material 3 segmented button selector for theme mode (Light / Auto / Dark)
 * Displays three options side-by-side in a single row with icons and labels
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSegmentedSelector(
    currentMode: AppThemeMode,
    onModeChange: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        Triple(AppThemeMode.Light, Icons.Default.LightMode, "Light"),
        Triple(AppThemeMode.Auto, Icons.Default.Settings, "Auto"),
        Triple(AppThemeMode.Dark, Icons.Default.DarkMode, "Dark")
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, (mode, icon, label) ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                selected = mode == currentMode,
                onClick = { onModeChange(mode) }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = label)
                }
            }
        }
    }
}
