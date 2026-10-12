package com.example.playground.engine

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RenderFlutterWidget(
    node: FlutterWidgetNode,
    modifier: Modifier = Modifier,
    onInteractiveAction: ((String) -> Unit)? = null
) {
    val context = LocalContext.current

    when (node.type) {
        "Container" -> {
            val width = node.getNumber("width")
            val height = node.getNumber("height")
            val colorHex = node.getColor("color")
            val insets = node.getEdgeInsets("padding")
            val marginInsets = node.getEdgeInsets("margin")
            val borderRadius = node.getBorderRadius("borderRadius")

            var boxModifier = modifier
            if (marginInsets != null) {
                boxModifier = boxModifier.padding(
                    start = marginInsets.start.dp,
                    top = marginInsets.top.dp,
                    end = marginInsets.end.dp,
                    bottom = marginInsets.bottom.dp
                )
            }
            if (width > 0) boxModifier = boxModifier.width(width.dp)
            if (height > 0) boxModifier = boxModifier.height(height.dp)

            val shape = if (borderRadius != null && borderRadius > 0) {
                RoundedCornerShape(borderRadius.dp)
            } else RoundedCornerShape(0.dp)

            boxModifier = boxModifier.clip(shape)

            if (colorHex != null) {
                boxModifier = boxModifier.background(Color(colorHex), shape)
            }

            if (insets != null) {
                boxModifier = boxModifier.padding(
                    start = insets.start.dp,
                    top = insets.top.dp,
                    end = insets.end.dp,
                    bottom = insets.bottom.dp
                )
            }

            Box(modifier = boxModifier) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Text" -> {
            val textStr = node.getString("text").ifEmpty {
                // Check if positional string exists
                node.properties.values.filterIsInstance<FlutterPropValue.StringVal>().firstOrNull()?.value ?: "Flutter Text"
            }
            val fontSize = node.getNumber("fontSize").let { if (it > 0) it else 14.0 }
            val colorHex = node.getColor("color")

            Text(
                text = textStr,
                fontSize = fontSize.sp,
                color = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }

        "Column" -> {
            val mainAlign = mapColumnMainAxisAlignment(node.getString("mainAxisAlignment"))
            val crossAlign = mapCrossAxisAlignment(node.getString("crossAxisAlignment"))

            Column(
                modifier = modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = mainAlign,
                horizontalAlignment = crossAlign
            ) {
                node.children.forEach { child ->
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Row" -> {
            val mainAlign = mapRowMainAxisAlignment(node.getString("mainAxisAlignment"))
            val crossAlign = mapCrossAxisAlignmentVertical(node.getString("crossAxisAlignment"))

            Row(
                modifier = modifier,
                horizontalArrangement = mainAlign,
                verticalAlignment = crossAlign
            ) {
                node.children.forEach { child ->
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Stack" -> {
            val align = mapBoxAlignment(node.getString("alignment"))
            Box(modifier = modifier, contentAlignment = align) {
                node.children.forEach { child ->
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Card" -> {
            val colorHex = node.getColor("color")
            val elevation = node.getNumber("elevation").let { if (it > 0) it else 2.0 }
            val shapeRadius = node.getBorderRadius("borderRadius") ?: 12.0

            Card(
                modifier = modifier,
                shape = RoundedCornerShape(shapeRadius.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp)
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Center" -> {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "Padding" -> {
            val insets = node.getEdgeInsets("padding")
            val padModifier = if (insets != null) {
                modifier.padding(start = insets.start.dp, top = insets.top.dp, end = insets.end.dp, bottom = insets.bottom.dp)
            } else modifier.padding(16.dp)

            Box(modifier = padModifier) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "SizedBox" -> {
            val width = node.getNumber("width")
            val height = node.getNumber("height")
            var sizeModifier = modifier
            if (width > 0) sizeModifier = sizeModifier.width(width.dp)
            if (height > 0) sizeModifier = sizeModifier.height(height.dp)

            val child = node.getChild("child")
            if (child != null) {
                Box(modifier = sizeModifier) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            } else {
                Spacer(modifier = sizeModifier)
            }
        }

        "ElevatedButton", "FilledButton" -> {
            val colorHex = node.getColor("color")
            Button(
                onClick = {
                    val msg = "Clicked: ${node.type}"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Text("Button")
                }
            }
        }

        "OutlinedButton" -> {
            OutlinedButton(
                onClick = {
                    val msg = "Clicked: OutlinedButton"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier,
                shape = RoundedCornerShape(10.dp)
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Text("Outlined")
                }
            }
        }

        "TextButton" -> {
            TextButton(
                onClick = {
                    val msg = "Clicked: TextButton"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Text("TextButton")
                }
            }
        }

        "IconButton" -> {
            IconButton(
                onClick = {
                    val msg = "Clicked: IconButton"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier
            ) {
                val child = node.getChild("icon") ?: node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
            }
        }

        "Icon" -> {
            val iconName = node.getIcon("icon") ?: node.properties.values.filterIsInstance<FlutterPropValue.IconVal>().firstOrNull()?.iconName ?: "star"
            val colorHex = node.getColor("color")
            val size = node.getNumber("size").let { if (it > 0) it.dp else 24.dp }
            val vector = mapIconVector(iconName)

            Icon(
                imageVector = vector,
                contentDescription = iconName,
                tint = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.primary,
                modifier = modifier.size(size)
            )
        }

        "CircleAvatar" -> {
            val radius = node.getNumber("radius").let { if (it > 0) it else 24.0 }
            val colorHex = node.getColor("backgroundColor") ?: 0xFF8B7CFFL

            Box(
                modifier = modifier
                    .size((radius * 2).dp)
                    .clip(CircleShape)
                    .background(Color(colorHex)),
                contentAlignment = Alignment.Center
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                }
            }
        }

        "Divider" -> {
            HorizontalDivider(modifier = modifier)
        }

        "Spacer" -> {
            Spacer(modifier = modifier)
        }

        "Scaffold" -> {
            Column(modifier = modifier.fillMaxSize()) {
                val body = node.getChild("body") ?: node.getChild("child")
                if (body != null) {
                    RenderFlutterWidget(body, modifier = Modifier.weight(1f), onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "FloatingActionButton" -> {
            val colorHex = node.getColor("backgroundColor") ?: node.getColor("color")
            FloatingActionButton(
                onClick = {
                    val msg = "Clicked: FloatingActionButton"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier,
                containerColor = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                } else {
                    Icon(Icons.Default.Add, contentDescription = "FAB")
                }
            }
        }

        "TextField", "TextFormField" -> {
            var inputVal by remember { mutableStateOf("") }
            val hint = node.getString("hintText").ifEmpty {
                node.getString("labelText").ifEmpty { "Enter text..." }
            }
            OutlinedTextField(
                value = inputVal,
                onValueChange = {
                    inputVal = it
                    onInteractiveAction?.invoke("Typed: $it")
                },
                placeholder = { Text(hint, fontSize = 13.sp) },
                label = { Text(hint, fontSize = 13.sp) },
                modifier = modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
        }

        "Checkbox" -> {
            var isChecked by remember { mutableStateOf(node.properties["value"]?.toString()?.contains("true") ?: false) }
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    val msg = "Checkbox toggled: $it"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier
            )
        }

        "Switch" -> {
            var isChecked by remember { mutableStateOf(node.properties["value"]?.toString()?.contains("true") ?: true) }
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    val msg = "Switch toggled: $it"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                modifier = modifier
            )
        }

        "ListTile" -> {
            val leading = node.getChild("leading")
            val title = node.getChild("title")
            val subtitle = node.getChild("subtitle")
            val trailing = node.getChild("trailing")

            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        val msg = "Clicked: ListTile"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        onInteractiveAction?.invoke(msg)
                    },
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leading != null) {
                        RenderFlutterWidget(leading, onInteractiveAction = onInteractiveAction)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        if (title != null) {
                            RenderFlutterWidget(title, onInteractiveAction = onInteractiveAction)
                        } else {
                            Text("ListTile Title", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        if (subtitle != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            RenderFlutterWidget(subtitle, onInteractiveAction = onInteractiveAction)
                        }
                    }
                    if (trailing != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        RenderFlutterWidget(trailing, onInteractiveAction = onInteractiveAction)
                    }
                }
            }
        }

        "ListView" -> {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                node.children.forEach { child ->
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        "CircularProgressIndicator" -> {
            val colorHex = node.getColor("color")
            CircularProgressIndicator(
                modifier = modifier.size(28.dp),
                color = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }

        "LinearProgressIndicator" -> {
            val colorHex = node.getColor("color")
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth(),
                color = if (colorHex != null) Color(colorHex) else MaterialTheme.colorScheme.primary
            )
        }

        "Chip" -> {
            SuggestionChip(
                onClick = {
                    val msg = "Clicked: Chip"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    onInteractiveAction?.invoke(msg)
                },
                label = {
                    val labelNode = node.getChild("label")
                    if (labelNode != null) {
                        RenderFlutterWidget(labelNode, onInteractiveAction = onInteractiveAction)
                    } else {
                        Text(node.getString("label").ifEmpty { "Flutter Chip" }, fontSize = 12.sp)
                    }
                },
                modifier = modifier
            )
        }

        "Align" -> {
            val align = mapBoxAlignment(node.getString("alignment"))
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = align) {
                val child = node.getChild("child")
                if (child != null) {
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }

        else -> {
            // Default fallback for general widget
            Column(modifier = modifier) {
                node.children.forEach { child ->
                    RenderFlutterWidget(child, onInteractiveAction = onInteractiveAction)
                }
            }
        }
    }
}

private fun mapColumnMainAxisAlignment(align: String): Arrangement.Vertical {
    return when {
        align.contains("spaceBetween", ignoreCase = true) -> Arrangement.SpaceBetween
        align.contains("spaceAround", ignoreCase = true) -> Arrangement.SpaceAround
        align.contains("spaceEvenly", ignoreCase = true) -> Arrangement.SpaceEvenly
        align.contains("center", ignoreCase = true) -> Arrangement.Center
        align.contains("end", ignoreCase = true) -> Arrangement.Bottom
        else -> Arrangement.Top
    }
}

private fun mapRowMainAxisAlignment(align: String): Arrangement.Horizontal {
    return when {
        align.contains("spaceBetween", ignoreCase = true) -> Arrangement.SpaceBetween
        align.contains("spaceAround", ignoreCase = true) -> Arrangement.SpaceAround
        align.contains("spaceEvenly", ignoreCase = true) -> Arrangement.SpaceEvenly
        align.contains("center", ignoreCase = true) -> Arrangement.Center
        align.contains("end", ignoreCase = true) -> Arrangement.End
        else -> Arrangement.Start
    }
}

private fun mapCrossAxisAlignment(align: String): Alignment.Horizontal {
    return when {
        align.contains("center", ignoreCase = true) -> Alignment.CenterHorizontally
        align.contains("end", ignoreCase = true) -> Alignment.End
        else -> Alignment.Start
    }
}

private fun mapCrossAxisAlignmentVertical(align: String): Alignment.Vertical {
    return when {
        align.contains("center", ignoreCase = true) -> Alignment.CenterVertically
        align.contains("bottom", ignoreCase = true) -> Alignment.Bottom
        else -> Alignment.Top
    }
}

private fun mapBoxAlignment(align: String): Alignment {
    return when {
        align.contains("topLeft", ignoreCase = true) -> Alignment.TopStart
        align.contains("topRight", ignoreCase = true) -> Alignment.TopEnd
        align.contains("bottomLeft", ignoreCase = true) -> Alignment.BottomStart
        align.contains("bottomRight", ignoreCase = true) -> Alignment.BottomEnd
        align.contains("topCenter", ignoreCase = true) -> Alignment.TopCenter
        align.contains("bottomCenter", ignoreCase = true) -> Alignment.BottomCenter
        else -> Alignment.Center
    }
}

private fun mapIconVector(name: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (name.lowercase()) {
        "favorite", "heart" -> Icons.Default.Favorite
        "home" -> Icons.Default.Home
        "person", "account" -> Icons.Default.Person
        "star" -> Icons.Default.Star
        "check" -> Icons.Default.Check
        "add" -> Icons.Default.Add
        "settings" -> Icons.Default.Settings
        "notifications" -> Icons.Default.Notifications
        "code" -> Icons.Default.Code
        "play_arrow" -> Icons.Default.PlayArrow
        "search" -> Icons.Default.Search
        "delete" -> Icons.Default.Delete
        "share" -> Icons.Default.Share
        "info" -> Icons.Default.Info
        "shopping_cart", "cart" -> Icons.Default.ShoppingCart
        else -> Icons.Default.Widgets
    }
}
