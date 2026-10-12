package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.ui.theme.AppTheme

@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val (color, label) = when (difficulty) {
        Difficulty.BEGINNER -> Pair(tokens.success, "Beginner")
        Difficulty.INTERMEDIATE -> Pair(tokens.secondary, "Intermediate")
        Difficulty.ADVANCED -> Pair(tokens.primary, "Advanced")
        Difficulty.EXPERT -> Pair(tokens.warning, "Expert")
    }

    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.2.sp
        )
    }
}
