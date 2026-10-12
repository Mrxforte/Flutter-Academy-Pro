package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DartSyntaxHighlighter
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CodeBlockView(
    code: String,
    modifier: Modifier = Modifier,
    title: String = "Dart & Flutter",
    onRunCode: (() -> Unit)? = null
) {
    val tokens = AppTheme.tokens
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isCopied by remember { mutableStateOf(false) }

    val lines = remember(code) { code.lines() }
    val highlightedText = remember(code, tokens.isDark) {
        DartSyntaxHighlighter.highlight(code, isDark = tokens.isDark)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = tokens.codeBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.surfaceSecondary)
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFEF4444).copy(alpha = 0.8f)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFF59E0B).copy(alpha = 0.8f)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFF10B981).copy(alpha = 0.8f)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = tokens.textMuted,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Copy button
                    FilledTonalButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            isCopied = true
                            Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                            coroutineScope.launch {
                                delay(2000)
                                isCopied = false
                            }
                        },
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("copy_code_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = tokens.surfaceElevated,
                            contentColor = tokens.textSecondary
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        if (isCopied) {
                            Icon(Icons.Default.Check, contentDescription = "Copied", modifier = Modifier.size(13.dp), tint = tokens.success)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copied", fontSize = 11.sp, color = tokens.success)
                        } else {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 11.sp)
                        }
                    }

                    if (onRunCode != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        FilledTonalButton(
                            onClick = onRunCode,
                            modifier = Modifier
                                .height(28.dp)
                                .testTag("run_code_header_button"),
                            contentPadding = PaddingValues(horizontal = 9.dp, vertical = 2.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = tokens.primary.copy(alpha = 0.15f),
                                contentColor = tokens.primary
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Run", fontSize = 11.sp, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            HorizontalDivider(color = tokens.border, thickness = 1.dp)

            // Code Content
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                // Line numbers
                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..lines.size) {
                        Text(
                            text = i.toString(),
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            color = tokens.textMuted
                        )
                    }
                }

                // Vertical gutter divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height((lines.size * 20).dp)
                        .background(tokens.border)
                )

                // Code text with horizontal scrolling
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(scrollState)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = highlightedText,
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = tokens.textPrimary
                    )
                }
            }
        }
    }
}
