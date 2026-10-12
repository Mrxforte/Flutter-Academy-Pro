package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schema
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppTheme

@Composable
fun ArchitectureDiagramView(
    title: String,
    asciiDiagram: String,
    modifier: Modifier = Modifier
) {
    if (asciiDiagram.isEmpty()) return
    val tokens = AppTheme.tokens

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schema,
                    contentDescription = "Architecture Diagram",
                    tint = tokens.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = tokens.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.codeBackground, RoundedCornerShape(8.dp))
                    .border(1.dp, tokens.border, RoundedCornerShape(8.dp))
                    .horizontalScroll(scrollState)
                    .padding(12.dp)
            ) {
                Text(
                    text = asciiDiagram,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    color = tokens.secondary
                )
            }
        }
    }
}
