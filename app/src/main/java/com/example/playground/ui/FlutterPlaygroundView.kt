package com.example.playground.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.playground.engine.*
import com.example.ui.theme.AppTheme

@Composable
fun FlutterPlaygroundView(
    initialCode: String? = null,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

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
                Text('Flutter Card Component', fontSize: 16, fontWeight: FontWeight.bold),
                Text('Subheading text description', fontSize: 13, color: Colors.grey),
              ],
            ),
          ],
        ),
        SizedBox(height: 12),
        Text('Card content dynamically created via our parser AST engine!'),
        SizedBox(height: 12),
        ElevatedButton(child: Text('Card Action'), color: Colors.purple),
      ],
    ),
  ),
)
            """.trimIndent(),
            "Form Fields" to """
Column(
  mainAxisAlignment: MainAxisAlignment.center,
  children: [
    Text('Interactive Form Elements', fontSize: 17, fontWeight: FontWeight.bold),
    SizedBox(height: 14),
    TextField(labelText: 'Email Address', hintText: 'developer@example.com'),
    SizedBox(height: 10),
    TextField(labelText: 'Password', obscureText: true),
    SizedBox(height: 16),
    ElevatedButton(child: Text('Sign In'), color: Colors.blue),
  ],
)
            """.trimIndent(),
            "Scrollable List" to """
ListView(
  children: [
    Text('Flutter Scrollable List', fontSize: 18, fontWeight: FontWeight.bold),
    SizedBox(height: 8),
    Container(width: 260, height: 44, color: Colors.teal, child: Center(child: Text('Item 1: Stateless Widget', color: Colors.white))),
    SizedBox(height: 8),
    Container(width: 260, height: 44, color: Colors.blue, child: Center(child: Text('Item 2: Stateful Widget', color: Colors.white))),
    SizedBox(height: 8),
    Container(width: 260, height: 44, color: Colors.purple, child: Center(child: Text('Item 3: InheritedWidget', color: Colors.white))),
    SizedBox(height: 8),
    Container(width: 260, height: 44, color: Colors.orange, child: Center(child: Text('Item 4: RenderBox Engine', color: Colors.white))),
  ],
)
            """.trimIndent()
        )
    }

    var codeText by remember {
        mutableStateOf(initialCode ?: starterTemplates.first().second)
    }

    var parseResult by remember {
        mutableStateOf(FlutterCodeParser.parse(codeText))
    }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live Canvas, 1: Code Editor, 2: Widget Tree
    var canvasThemeMode by remember { mutableStateOf(CanvasThemeMode.LIGHT) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    fun compileAndRun() {
        val result = FlutterCodeParser.parse(codeText)
        parseResult = result
        if (result.rootNode != null) {
            Toast.makeText(context, "Rendered successfully", Toast.LENGTH_SHORT).show()
        }
    }

    val currentRootNode = parseResult.rootNode
    val hasErrors = parseResult.diagnostics.any { it.isError }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
    ) {
        // Playground Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surface)
                .border(1.dp, tokens.border)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(tokens.subtleAccentBg, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Widgets,
                        contentDescription = null,
                        tint = tokens.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Flutter Widget Canvas",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Text(
                        text = "Live dynamic AST rendering",
                        style = MaterialTheme.typography.labelSmall,
                        color = tokens.textSecondary
                    )
                }
            }

            // Quick Actions: Run, Reset, Canvas Light/Dark
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        codeText = starterTemplates.first().second
                        compileAndRun()
                    },
                    modifier = Modifier.size(32.dp).testTag("playground_reset_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset code", tint = tokens.textSecondary, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(codeText))
                        Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy code", tint = tokens.textSecondary, modifier = Modifier.size(16.dp))
                }

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

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = { compileAndRun() },
                    modifier = Modifier.height(32.dp).testTag("playground_compile_button"),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tokens.buttonPrimary,
                        contentColor = tokens.buttonPrimaryText
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(15.dp), tint = tokens.buttonPrimaryText)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Run Live", fontSize = 11.sp, color = tokens.buttonPrimaryText, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Quick Starter Templates Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.surfaceSecondary)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp),
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
                    label = { Text(title, fontSize = 11.sp, color = tokens.textPrimary) },
                    shape = RoundedCornerShape(8.dp),
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

        // View Tabs: Canvas, Code Editor, Widget Tree
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .background(tokens.surfaceSecondary, RoundedCornerShape(10.dp))
                .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                .padding(3.dp)
        ) {
            val tabs = listOf("Preview Canvas", "Code Editor", "Widget Tree")
            tabs.forEachIndexed { index, label ->
                val isSelected = selectedTab == index
                val tabBg = if (isSelected) tokens.surface else tokens.surfaceSecondary
                val tabTextColor = if (isSelected) tokens.primary else tokens.textSecondary

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(tabBg)
                        .clickable { selectedTab = index }
                        .padding(vertical = 7.dp)
                        .testTag("playground_tab_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = tabTextColor
                    )
                }
            }
        }

        // Error Banner
        if (hasErrors) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                color = tokens.error.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, tokens.error.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = tokens.error, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = parseResult.diagnostics.firstOrNull { it.isError }?.message ?: "Parsing diagnostic error",
                        color = tokens.error,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 14.dp)
                .padding(bottom = 12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.surfaceSecondary)
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE5484D).copy(alpha = 0.85f)))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEAA12B).copy(alpha = 0.85f)))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF16A36A).copy(alpha = 0.85f)))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Flutter Simulated Device Canvas",
                                        fontSize = 11.sp,
                                        color = tokens.textSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { zoomScale = (zoomScale - 0.1f).coerceAtLeast(0.6f) }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out", tint = tokens.textSecondary, modifier = Modifier.size(14.dp))
                                    }
                                    Text(text = "${(zoomScale * 100).toInt()}%", fontSize = 10.sp, color = tokens.textSecondary)
                                    IconButton(onClick = { zoomScale = (zoomScale + 0.1f).coerceAtMost(1.5f) }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in", tint = tokens.textSecondary, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            val canvasBg = if (canvasThemeMode == CanvasThemeMode.DARK) Color(0xFF1E293B) else Color(0xFFFFFFFF)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(canvasBg)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (currentRootNode != null) {
                                    RenderFlutterWidget(
                                        node = currentRootNode,
                                        onInteractiveAction = { event ->
                                            Toast.makeText(context, event, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                } else {
                                    Text(
                                        text = "Tap 'Run Live' to compile widget code",
                                        color = tokens.textMuted,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Flutter Code Editor",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                                Button(
                                    onClick = { compileAndRun() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = tokens.buttonPrimary,
                                        contentColor = tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Apply & Run", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = codeText,
                                onValueChange = { codeText = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("playground_editor_field"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = tokens.primary,
                                    unfocusedBorderColor = tokens.border,
                                    focusedTextColor = tokens.textPrimary,
                                    unfocusedTextColor = tokens.textPrimary,
                                    focusedContainerColor = tokens.codeBackground,
                                    unfocusedContainerColor = tokens.codeBackground
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    lineHeight = 19.sp
                                )
                            )
                        }
                    }
                }
                2 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            Text(
                                text = "Flutter Widget Hierarchy Tree",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Text(
                                text = "Parsed Abstract Syntax Tree (AST) structure for Flutter runtime",
                                style = MaterialTheme.typography.bodySmall,
                                color = tokens.textSecondary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalDivider(color = tokens.border)
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    if (currentRootNode != null) {
                                        WidgetTreeNodeView(node = currentRootNode, depth = 0)
                                    } else {
                                        Text(text = "No widget tree parsed yet", color = tokens.textMuted)
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

@Composable
private fun WidgetTreeNodeView(
    node: FlutterWidgetNode,
    depth: Int
) {
    val tokens = AppTheme.tokens
    Column(modifier = Modifier.padding(start = (depth * 18).dp, top = 4.dp, bottom = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(tokens.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = tokens.subtleAccentBg
            ) {
                Text(
                    text = node.type,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = tokens.primary
                )
            }
            if (node.properties.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(${node.properties.keys.take(3).joinToString()})",
                    fontSize = 11.sp,
                    color = tokens.textSecondary,
                    maxLines = 1
                )
            }
        }
        node.children.forEach { child ->
            WidgetTreeNodeView(node = child, depth = depth + 1)
        }
    }
}
