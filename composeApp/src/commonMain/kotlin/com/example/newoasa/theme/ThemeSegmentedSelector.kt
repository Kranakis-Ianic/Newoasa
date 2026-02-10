package com.example.newoasa.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newoasa.AppThemeMode

/**
 * Material 3 segmented button selector for theme mode (Light / Auto / Dark)
 * Displays three options side-by-side in a single row
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSegmentedSelector(
    currentMode: AppThemeMode,
    onModeChange: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        AppThemeMode.Light to "Light",
        AppThemeMode.Auto to "Auto",
        AppThemeMode.Dark to "Dark"
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, (mode, label) ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                selected = mode == currentMode,
                onClick = { onModeChange(mode) },
            ) {
                Text(label)
            }
        }
    }
}
