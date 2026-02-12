package com.example.newoasa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newoasa.theme.LineColors

/**
 * LineBox - A reusable composable component that displays a transit line badge
 * with the appropriate color from line_info.json.
 *
 * This component is used in StationCard and can be reused anywhere line badges are needed.
 *
 * @param lineNumber The line identifier (e.g., "M1", "T6", "A1", "Μ1", "1")
 * @param modifier Optional modifier for customizing the box appearance
 * @param cornerRadius The corner radius of the box (default 8.dp)
 * @param horizontalPadding Horizontal padding inside the box (default 12.dp)
 * @param verticalPadding Vertical padding inside the box (default 6.dp)
 * @param textColor The text color (default white)
 */
@Composable
fun LineBox(
    lineNumber: String,
    modifier: Modifier = Modifier,
    cornerRadius: androidx.compose.ui.unit.Dp = 8.dp,
    horizontalPadding: androidx.compose.ui.unit.Dp = 12.dp,
    verticalPadding: androidx.compose.ui.unit.Dp = 6.dp,
    textColor: Color = Color.White
) {
    // Get the actual line color from LineColors utility (reads from line_info.json)
    val lineColor = LineColors.getColorForLine(lineNumber)
    
    // Display text - normalize the line number for display
    val displayText = normalizeLineNumber(lineNumber)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(lineColor)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/**
 * Normalize line number for display
 * Converts various formats to standard display format:
 * - "Μ1" (Greek) or "M1" (Latin) → "M1" for metro
 * - "1" → "M1" for metro lines 1-4
 * - "6", "7" → "T6", "T7" for tram lines
 * - Already formatted lines (M1, T6, A1) are returned as-is
 *
 * @param lineNumber The input line number in any format
 * @return Normalized line number for display
 */
private fun normalizeLineNumber(lineNumber: String): String {
    val normalized = lineNumber.trim()
    
    // If it starts with Greek Μ (mu), replace with Latin M
    val withLatinM = normalized.replace("Μ", "M")
    
    // If it's already in good format (M1, T6, A1, etc.), return it
    if (withLatinM.matches(Regex("^[MTA]\\d+$"))) {
        return withLatinM
    }
    
    // If it's just a number, try to infer the prefix based on Athens line numbering
    if (withLatinM.matches(Regex("^\\d+$"))) {
        val num = withLatinM.toIntOrNull()
        return when (num) {
            1, 2, 3, 4 -> "M$withLatinM" // Metro lines
            6, 7 -> "T$withLatinM" // Tram lines
            else -> withLatinM // Keep as is for other numbers
        }
    }
    
    return withLatinM
}

/**
 * LineBadge - Alias for LineBox to maintain backward compatibility
 * @deprecated Use LineBox instead
 */
@Deprecated(
    "Use LineBox instead",
    ReplaceWith("LineBox(lineNumber, modifier)", "com.example.newoasa.ui.components.LineBox")
)
@Composable
fun LineBadge(
    lineNumber: String,
    modifier: Modifier = Modifier
) {
    LineBox(lineNumber = lineNumber, modifier = modifier)
}
