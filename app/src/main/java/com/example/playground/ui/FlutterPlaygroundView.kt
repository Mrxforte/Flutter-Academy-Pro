package com.example.playground.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DartSyntaxHighlighter
import com.example.playground.engine.*
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FlutterPlaygroundView(
    initialCode: String? = null,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val starterTemplates = remember {
        listOf(
            "Container Box" to """
Container(
  width: 280,
  height: 180,
  color: Colors.blue,
  padding: EdgeInsets.all(16.0),
  borderRadius: BorderRadius.circular(16.0),
  child: Column(
    mainAxisAlignment: MainAxisAlignment.center,
    children: [
      Icon(Icons.star, color: Colors.white, size: 36),
      SizedBox(height: 12),
      Text('Dynamic Flutter Container', color: Colors.white, fontSize: 16),
      Text('Edit my color, size or text!', color: Colors.amber, fontSize: 13),
    ],
  ),
)
            """.trimIndent(),
            "Flex Column & Row" to """
Column(
  mainAxisAlignment: MainAxisAlignment.center,
  crossAxisAlignment: CrossAxisAlignment.center,
  children: [
    Text('Flex Layout Playground', fontSize: 18, color: Colors.purple),
    SizedBox(height: 16),
    Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        ElevatedButton(child: Text('Button A')),
        ElevatedButton(child: Text('Button B'), color: Colors.teal),
        IconButton(icon: Icons.favorite, color: Colors.red),
      ],
    ),
    SizedBox(height: 16),
    Text('Live rendered in real-time!', fontSize: 13, color: Colors.grey),
  ],
)
            """.trimIndent(),
            "Material Card" to """
Card(
  elevation: 4,
  borderRadius: BorderRadius.circular(16.0),
  color: Colors.white,
  child: Padding(
    padding: EdgeInsets.all(16.0),
    child: Column(
      children: [
        Row(
          children: [
            CircleAvatar(radius: 20, backgroundColor: Colors.purple),
            SizedBox(width: 12),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Sardor Umarov', fontSize: 15, color: Colors.black),
                Text('Senior Flutter Lead', fontSize: 12, color: Colors.grey),
              ],
            ),
          ],
        ),
        SizedBox(height: 16),
        Text('Real dynamic Flutter widget tree rendered on mobile canvas.', color: Colors.black),
        SizedBox(height: 12),
        ElevatedButton(child: Text('Connect Profile'), color: Colors.purple),
      ],
    ),
  ),
)
            """.trimIndent(),
            "Stack & Badges" to """
Stack(
  alignment: Alignment.center,
  children: [
    Container(
      width: 240,
      height: 150,
      color: Colors.indigo,
      borderRadius: BorderRadius.circular(14.0),
      child: Center(
        child: Text('Underlying Canvas', color: Colors.white),
      ),
    ),
    Container(
      width: 120,
      height: 40,
      color: Colors.amber,
      borderRadius: BorderRadius.circular(20.0),
      child: Center(
        child: Text('PRO BADGE', color: Colors.black),
      ),
    ),
  ],
)
            """.trimIndent()
        )
    }

    var codeText by remember(initialCode) {
        mutableStateOf(initialCode ?: starterTemplates[0].second)
    }

    var selectedWorkspaceTab by remember { mutableIntStateOf(1) } // 0: Editor, 1: Live Preview, 2: Console
    var selectedDeviceSize by remember { mutableStateOf(PreviewDeviceSize.STANDARD) }
    var canvasThemeMode by remember { mutableStateOf(CanvasThemeMode.DARK) }

    var lastSuccessfulRoot by remember {
        mutableStateOf(FlutterCodeParser.parse(codeText).rootNode)
    }
    var diagnostics by remember { mutableStateOf<List<CompileDiagnostic>>(emptyList()) }
    var compileTimeMs by remember { mutableLongStateOf(18) }
    var interactionLogs by remember { mutableStateOf<List<String>>(emptyList()) }
    var isCompiling by remember { mutableStateOf(false) }

    // Compiler function
    fun compileAndRun() {
        isCompiling = true
        val start = System.currentTimeMillis()
        val parseResult = FlutterCodeParser.parse(codeText)
        val elapsed = (System.currentTimeMillis() - start).coerceAtLeast(12)
        compileTimeMs = elapsed
        diagnostics = parseResult.diagnostics

        if (parseResult.rootNode != null) {
            lastSuccessfulRoot = parseResult.rootNode
            interactionLogs = interactionLogs + "✓ Rebuilt frame successfully in ${elapsed}ms (${parseResult.rootNode.type})"
            selectedWorkspaceTab = 1 // Switch to Live Preview
        } else {
            interactionLogs = interactionLogs + "✕ Compilation error: ${parseResult.diagnostics.firstOrNull()?.message}"
            selectedWorkspaceTab = 2 // Switch to Console
        }
        isCompiling = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
    ) {
        // Workspace Top Bar: Device Size, Theme Toggle, Workspace Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surface)
                .border(1.dp, tokens.border, RoundedCornerShape(0.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Workspace Mode Segmented Control
            Row(
                modifier = Modifier
                    .background(tokens.surfaceSecondary, RoundedCornerShape(8.dp))
                    .border(1.dp, tokens.border, RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                listOf("Editor", "Live Canvas", "Console").forEachIndexed { idx, label ->
                    val isSelected = selectedWorkspaceTab == idx
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) tokens.surface else tokens.surfaceSecondary)
                            .clickable { selectedWorkspaceTab = idx }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("workspace_tab_$idx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) tokens.primary else tokens.textSecondary
                        )
                    }
                }
            }

            // Canvas Controls: Size & Theme
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Device Size Toggle
                IconButton(
                    onClick = {
                        selectedDeviceSize = when (selectedDeviceSize) {
                            PreviewDeviceSize.COMPACT -> PreviewDeviceSize.STANDARD
                            PreviewDeviceSize.STANDARD -> PreviewDeviceSize.TABLET
                            PreviewDeviceSize.TABLET -> PreviewDeviceSize.COMPACT
                        }
                    },
                    modifier = Modifier.size(32.dp).testTag("device_size_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Default.Devices,
                        contentDescription = "Device size: ${selectedDeviceSize.displayName}",
                        tint = tokens.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Canvas Light/Dark Toggle
                IconButton(
                    onClick = {
                        canvasThemeMode = if (canvasThemeMode == CanvasThemeMode.DARK) CanvasThemeMode.LIGHT else CanvasThemeMode.DARK
                    },
                    modifier = Modifier.size(32.dp).testTag("canvas_theme_toggle")
                ) {
                    Icon(
                        imageVector = if (canvasThemeMode == CanvasThemeMode.DARK) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Canvas theme",
                        tint = tokens.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Run Action Button: Sage-green Run button
                Button(
                    onClick = { compileAndRun() },
                    modifier = Modifier.height(30.dp).testTag("playground_compile_button"),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tokens.sagePrimary,
                        contentColor = Color(0xFF141614)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(15.dp), tint = Color(0xFF141614))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Run Live", fontSize = 11.sp, color = Color(0xFF141614), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Quick Starter Templates Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surfaceSecondary)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Templates:",
                style = MaterialTheme.typography.labelSmall,
                color = tokens.textMuted
            )
            starterTemplates.forEach { (title, templateCode) ->
                SuggestionChip(
                    onClick = {
                        codeText = templateCode
                        compileAndRun()
                    },
                    label = { Text(title, fontSize = 11.sp) },
                    shape = RoundedCornerShape(6.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = tokens.surface,
                        labelColor = tokens.textPrimary
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = tokens.border
                    )
                )
            }
        }

        // Content Area based on Selected Tab
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedWorkspaceTab) {
                0 -> {
                    // TAB 0: Professional Dart Code Editor
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dart / Flutter Source (Editable)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.textPrimary
                            )
                            Row {
                                TextButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(codeText))
                                        Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy", fontSize = 11.sp)
                                }
                                TextButton(
                                    onClick = {
                                        codeText = starterTemplates[0].second
                                        compileAndRun()
                                    }
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reset", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = codeText,
                            onValueChange = { codeText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .testTag("flutter_code_editor"),
                            textStyle = LocalTextStyle.current.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                lineHeight = 19.sp,
                                color = tokens.textPrimary
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = tokens.codeBackground,
                                unfocusedContainerColor = tokens.codeBackground,
                                focusedBorderColor = tokens.primary,
                                unfocusedBorderColor = tokens.border
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Lines: ${codeText.lines().size} • Supports Container, Column, Row, Stack, Card, Buttons, Text",
                                style = MaterialTheme.typography.labelSmall,
                                color = tokens.textMuted
                            )
                            Button(
                                onClick = { compileAndRun() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = tokens.buttonPrimary,
                                    contentColor = tokens.buttonPrimaryText
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Rebuild Live Preview", style = MaterialTheme.typography.labelMedium, color = tokens.buttonPrimaryText)
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: Real Dynamic Flutter Widget Rendering Canvas
                    // Creamy-white in light mode, Muted dark-green in dark mode
                    val canvasBg = if (canvasThemeMode == CanvasThemeMode.DARK) Color(0xFF1C201C) else Color(0xFFFFFFFF)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Device frame header
                        Row(
                            modifier = Modifier
                                .width(selectedDeviceSize.widthDp.dp)
                                .background(tokens.surfaceSecondary, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .border(1.dp, tokens.border, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(tokens.secondary))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${selectedDeviceSize.displayName} • Live Flutter Canvas",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tokens.textSecondary
                                )
                            }
                            Text(
                                text = "${compileTimeMs}ms",
                                style = MaterialTheme.typography.labelSmall,
                                color = tokens.textMuted
                            )
                        }

                        // The Real Rendered Canvas Body
                        Box(
                            modifier = Modifier
                                .width(selectedDeviceSize.widthDp.dp)
                                .weight(1f)
                                .background(canvasBg, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                                .border(1.dp, tokens.border, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val root = lastSuccessfulRoot
                            if (root != null) {
                                RenderFlutterWidget(
                                    node = root,
                                    onInteractiveAction = { log ->
                                        interactionLogs = interactionLogs + log
                                    }
                                )
                            } else {
                                Text("No widget rendered. Click 'Run Live' to build.", color = tokens.textMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Diagnostics Banner (if errors exist)
                        if (diagnostics.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, tokens.error.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = tokens.error.copy(alpha = 0.1f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = tokens.error, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Preserving previous successful frame: ${diagnostics.first().message}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tokens.error
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: Diagnostic Compiler Console
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Compiler Diagnostic Console",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.textPrimary
                            )
                            Text(
                                text = "Execution logs, frame rebuild timings, and AST validation messages.",
                                style = MaterialTheme.typography.bodySmall,
                                color = tokens.textSecondary
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, tokens.border, RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = tokens.codeBackground)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = ">_ System Build Logs:",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.secondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (interactionLogs.isEmpty()) {
                                        Text(
                                            text = "Ready. Tap 'Run Live' to parse and compile Flutter widgets.",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = tokens.textMuted
                                        )
                                    } else {
                                        interactionLogs.takeLast(10).forEach { log ->
                                            Text(
                                                text = log,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = if (log.startsWith("✕")) tokens.error else tokens.textPrimary
                                            )
                                        }
                                    }

                                    if (diagnostics.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Compiler Diagnostics (${diagnostics.size} issues):",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.error
                                        )
                                        diagnostics.forEach { diag ->
                                            Text(
                                                text = "Line ${diag.line}: ${diag.message}",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = tokens.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
