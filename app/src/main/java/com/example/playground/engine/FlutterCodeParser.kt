package com.example.playground.engine

class ParseResult(
    val rootNode: FlutterWidgetNode?,
    val diagnostics: List<CompileDiagnostic>
)

object FlutterCodeParser {

    private val KNOWN_WIDGETS = setOf(
        "Container", "Text", "Column", "Row", "Stack", "Card", "Center",
        "Padding", "SizedBox", "ElevatedButton", "TextButton", "OutlinedButton",
        "IconButton", "Icon", "Scaffold", "AppBar", "FloatingActionButton",
        "CircleAvatar", "Chip", "Divider", "Spacer", "MaterialApp", "Expanded",
        "Align", "Wrap", "ListTile", "TextField", "TextFormField", "Checkbox",
        "Switch", "ListView", "CircularProgressIndicator", "LinearProgressIndicator"
    )

    fun parse(sourceCode: String): ParseResult {
        val diagnostics = mutableListOf<CompileDiagnostic>()
        val trimmed = sourceCode.trim()

        if (trimmed.isEmpty()) {
            diagnostics.add(CompileDiagnostic(1, 1, "Source code is empty.", isError = true))
            return ParseResult(null, diagnostics)
        }

        // Check matching parentheses and brackets
        var parenCount = 0
        var bracketCount = 0
        var braceCount = 0
        var lineNum = 1

        for ((index, char) in sourceCode.withIndex()) {
            if (char == '\n') lineNum++
            when (char) {
                '(' -> parenCount++
                ')' -> {
                    parenCount--
                    if (parenCount < 0) {
                        diagnostics.add(CompileDiagnostic(lineNum, index, "Unexpected closing parenthesis ')' without matching '('", isError = true))
                        return ParseResult(null, diagnostics)
                    }
                }
                '[' -> bracketCount++
                ']' -> {
                    bracketCount--
                    if (bracketCount < 0) {
                        diagnostics.add(CompileDiagnostic(lineNum, index, "Unexpected closing bracket ']' without matching '['", isError = true))
                        return ParseResult(null, diagnostics)
                    }
                }
                '{' -> braceCount++
                '}' -> braceCount--
            }
        }

        if (parenCount > 0) {
            diagnostics.add(CompileDiagnostic(lineNum, 1, "Syntax Error: $parenCount unclosed parenthesis '(' detected.", isError = true))
            return ParseResult(null, diagnostics)
        }
        if (bracketCount > 0) {
            diagnostics.add(CompileDiagnostic(lineNum, 1, "Syntax Error: $bracketCount unclosed list bracket '[' detected.", isError = true))
            return ParseResult(null, diagnostics)
        }

        // Locate widget expression
        val widgetStart = findFirstWidgetIndex(trimmed)
        if (widgetStart == -1) {
            diagnostics.add(
                CompileDiagnostic(
                    1, 1,
                    "No supported Flutter widget constructor found (e.g. Container, Text, Column, Row, Card).",
                    isError = true
                )
            )
            return ParseResult(null, diagnostics)
        }

        return try {
            val parser = WidgetExpressionParser(trimmed, widgetStart)
            val root = parser.parseWidget()
            if (root == null) {
                diagnostics.add(CompileDiagnostic(1, 1, "Failed to parse root widget expression.", isError = true))
            }
            ParseResult(root, diagnostics)
        } catch (e: Exception) {
            diagnostics.add(CompileDiagnostic(1, 1, "Compiler Exception: ${e.message ?: "Malformed Dart syntax"}", isError = true))
            ParseResult(null, diagnostics)
        }
    }

    private fun findFirstWidgetIndex(code: String): Int {
        // Look for return Widget(...); or runApp(Widget(...)); or direct Widget(...)
        val returnIdx = code.indexOf("return ")
        if (returnIdx != -1) {
            for (widget in KNOWN_WIDGETS) {
                val idx = code.indexOf(widget, returnIdx)
                if (idx != -1 && isConstructorCall(code, idx, widget)) return idx
            }
        }
        val runAppIdx = code.indexOf("runApp(")
        if (runAppIdx != -1) {
            for (widget in KNOWN_WIDGETS) {
                val idx = code.indexOf(widget, runAppIdx)
                if (idx != -1 && isConstructorCall(code, idx, widget)) return idx
            }
        }
        // General first occurrence of any known widget
        var earliest = -1
        for (widget in KNOWN_WIDGETS) {
            val idx = code.indexOf(widget)
            if (idx != -1 && isConstructorCall(code, idx, widget)) {
                if (earliest == -1 || idx < earliest) earliest = idx
            }
        }
        return earliest
    }

    private fun isConstructorCall(code: String, idx: Int, widgetName: String): Boolean {
        val end = idx + widgetName.length
        if (end < code.length && code[end] == '(') {
            if (idx == 0 || !code[idx - 1].isLetterOrDigit()) return true
        }
        return false
    }

    private class WidgetExpressionParser(private val text: String, startIndex: Int) {
        private var pos = startIndex

        fun parseWidget(): FlutterWidgetNode? {
            skipWhitespaceAndComments()
            if (pos >= text.length) return null

            val nameStart = pos
            while (pos < text.length && (text[pos].isLetterOrDigit() || text[pos] == '_')) {
                pos++
            }
            val widgetName = text.substring(nameStart, pos)
            skipWhitespaceAndComments()

            if (pos >= text.length || text[pos] != '(') return null
            pos++ // skip '('

            val properties = mutableMapOf<String, FlutterPropValue>()
            val childrenList = mutableListOf<FlutterWidgetNode>()

            while (pos < text.length) {
                skipWhitespaceAndComments()
                if (pos >= text.length || text[pos] == ')') {
                    if (pos < text.length) pos++ // skip ')'
                    break
                }

                // Positional child or text string: e.g. Text('Hello') or Center(Container(...))
                val lookahead = text.substring(pos, (pos + 30).coerceAtMost(text.length))
                if (!lookahead.contains(":") || (lookahead.indexOf(":") > lookahead.indexOf("(") && lookahead.contains("("))) {
                    // Positional argument
                    if (text[pos] == '\'' || text[pos] == '"') {
                        val strVal = parseStringLiteral()
                        properties["text"] = FlutterPropValue.StringVal(strVal)
                    } else if (text[pos].isDigit()) {
                        val num = parseNumber()
                        properties["value"] = FlutterPropValue.NumberVal(num)
                    } else {
                        // Positional widget or icon
                        val childWidget = parseWidget()
                        if (childWidget != null) {
                            properties["child"] = FlutterPropValue.WidgetVal(childWidget)
                            childrenList.add(childWidget)
                        }
                    }
                    skipWhitespaceAndComments()
                    if (pos < text.length && text[pos] == ',') pos++
                    continue
                }

                // Named property: key: value
                val keyStart = pos
                while (pos < text.length && (text[pos].isLetterOrDigit() || text[pos] == '_')) {
                    pos++
                }
                val propKey = text.substring(keyStart, pos)
                skipWhitespaceAndComments()

                if (pos < text.length && text[pos] == ':') {
                    pos++ // skip ':'
                    skipWhitespaceAndComments()

                    // Parse property value
                    val value = parsePropertyValue(propKey)
                    if (value != null) {
                        properties[propKey] = value
                        if (value is FlutterPropValue.WidgetVal) {
                            childrenList.add(value.node)
                        } else if (value is FlutterPropValue.ListVal) {
                            value.items.filterIsInstance<FlutterPropValue.WidgetVal>().forEach {
                                childrenList.add(it.node)
                            }
                        }
                    }
                }

                skipWhitespaceAndComments()
                if (pos < text.length && text[pos] == ',') pos++
            }

            return FlutterWidgetNode(
                type = widgetName,
                properties = properties,
                children = childrenList
            )
        }

        private fun parsePropertyValue(propKey: String): FlutterPropValue? {
            skipWhitespaceAndComments()
            if (pos >= text.length) return null

            // 1. String literal: '...' or "..."
            if (text[pos] == '\'' || text[pos] == '"') {
                return FlutterPropValue.StringVal(parseStringLiteral())
            }

            // 2. Number: 120, 16.0
            if (text[pos].isDigit() || (text[pos] == '-' && pos + 1 < text.length && text[pos + 1].isDigit())) {
                return FlutterPropValue.NumberVal(parseNumber())
            }

            // 3. Boolean: true, false
            if (text.startsWith("true", pos)) {
                pos += 4
                return FlutterPropValue.BooleanVal(true)
            }
            if (text.startsWith("false", pos)) {
                pos += 5
                return FlutterPropValue.BooleanVal(false)
            }

            // 4. Children List: [ ... ]
            if (text[pos] == '[') {
                pos++ // skip '['
                val items = mutableListOf<FlutterPropValue>()
                while (pos < text.length) {
                    skipWhitespaceAndComments()
                    if (pos >= text.length || text[pos] == ']') {
                        if (pos < text.length) pos++ // skip ']'
                        break
                    }
                    val child = parseWidget()
                    if (child != null) {
                        items.add(FlutterPropValue.WidgetVal(child))
                    }
                    skipWhitespaceAndComments()
                    if (pos < text.length && text[pos] == ',') pos++
                }
                return FlutterPropValue.ListVal(items)
            }

            // 5. Colors: Colors.blue, Colors.red[400], Color(0xFF123456)
            if (text.startsWith("Colors.", pos)) {
                pos += 7
                val colorStart = pos
                while (pos < text.length && text[pos].isLetter()) pos++
                val colorName = text.substring(colorStart, pos)
                // Check shade
                var shade = 500
                skipWhitespaceAndComments()
                if (pos < text.length && text[pos] == '[') {
                    pos++
                    val shadeStart = pos
                    while (pos < text.length && text[pos].isDigit()) pos++
                    shade = text.substring(shadeStart, pos).toIntOrNull() ?: 500
                    if (pos < text.length && text[pos] == ']') pos++
                } else if (text.startsWith(".shade", pos)) {
                    pos += 6
                    val shadeStart = pos
                    while (pos < text.length && text[pos].isDigit()) pos++
                    shade = text.substring(shadeStart, pos).toIntOrNull() ?: 500
                }
                val hex = mapNamedColor(colorName, shade)
                return FlutterPropValue.ColorVal(hex)
            }

            if (text.startsWith("Color(", pos)) {
                pos += 6
                val hexStart = pos
                while (pos < text.length && text[pos] != ')') pos++
                val rawHex = text.substring(hexStart, pos).trim()
                if (pos < text.length) pos++ // skip ')'
                val hexLong = parseHexLong(rawHex)
                return FlutterPropValue.ColorVal(hexLong)
            }

            // 6. EdgeInsets: EdgeInsets.all(16.0), EdgeInsets.symmetric(horizontal: 12, vertical: 8)
            if (text.startsWith("EdgeInsets.", pos)) {
                pos += 11
                if (text.startsWith("all(", pos)) {
                    pos += 4
                    val num = parseNumber()
                    if (pos < text.length && text[pos] == ')') pos++
                    return FlutterPropValue.EdgeInsetsVal(num, num, num, num)
                } else if (text.startsWith("symmetric(", pos)) {
                    pos += 10
                    var h = 0.0
                    var v = 0.0
                    while (pos < text.length && text[pos] != ')') {
                        skipWhitespaceAndComments()
                        if (text.startsWith("horizontal:", pos)) {
                            pos += 11
                            h = parseNumber()
                        } else if (text.startsWith("vertical:", pos)) {
                            pos += 9
                            v = parseNumber()
                        } else pos++
                        if (pos < text.length && text[pos] == ',') pos++
                    }
                    if (pos < text.length && text[pos] == ')') pos++
                    return FlutterPropValue.EdgeInsetsVal(v, h, v, h)
                }
            }

            // 7. BorderRadius: BorderRadius.circular(16.0)
            if (text.startsWith("BorderRadius.circular(", pos)) {
                pos += 22
                val radius = parseNumber()
                if (pos < text.length && text[pos] == ')') pos++
                return FlutterPropValue.BorderRadiusVal(radius)
            }

            // 8. BoxDecoration: BoxDecoration(color: Colors.blue, borderRadius: BorderRadius.circular(12))
            if (text.startsWith("BoxDecoration(", pos)) {
                pos += 14
                var boxColor: Long? = null
                var boxRadius: Double? = null
                while (pos < text.length && text[pos] != ')') {
                    skipWhitespaceAndComments()
                    if (text.startsWith("color:", pos)) {
                        pos += 6
                        val c = parsePropertyValue("color") as? FlutterPropValue.ColorVal
                        if (c != null) boxColor = c.colorHex
                    } else if (text.startsWith("borderRadius:", pos)) {
                        pos += 13
                        val r = parsePropertyValue("borderRadius") as? FlutterPropValue.BorderRadiusVal
                        if (r != null) boxRadius = r.radius
                    } else pos++
                    if (pos < text.length && text[pos] == ',') pos++
                }
                if (pos < text.length && text[pos] == ')') pos++
                if (boxColor != null) {
                    return FlutterPropValue.ColorVal(boxColor)
                }
                if (boxRadius != null) {
                    return FlutterPropValue.BorderRadiusVal(boxRadius)
                }
            }

            // 9. Icons: Icons.star, Icons.home, Icons.favorite
            if (text.startsWith("Icons.", pos)) {
                pos += 6
                val iconStart = pos
                while (pos < text.length && (text[pos].isLetterOrDigit() || text[pos] == '_')) pos++
                val iconName = text.substring(iconStart, pos)
                return FlutterPropValue.IconVal(iconName)
            }

            // 10. Alignment & MainAxisAlignment / CrossAxisAlignment
            if (text.startsWith("MainAxisAlignment.", pos) || text.startsWith("CrossAxisAlignment.", pos) || text.startsWith("Alignment.", pos)) {
                val alignStart = pos
                while (pos < text.length && (text[pos].isLetterOrDigit() || text[pos] == '.')) pos++
                val alignStr = text.substring(alignStart, pos)
                return FlutterPropValue.StringVal(alignStr)
            }

            // 11. Child widget: e.g. child: Container(...)
            val widget = parseWidget()
            if (widget != null) {
                return FlutterPropValue.WidgetVal(widget)
            }

            // Skip unknown expression to next comma or closing parenthesis
            while (pos < text.length && text[pos] != ',' && text[pos] != ')' && text[pos] != ']') {
                pos++
            }
            return null
        }

        private fun parseStringLiteral(): String {
            val quote = text[pos]
            pos++ // skip opening quote
            val sb = StringBuilder()
            while (pos < text.length && text[pos] != quote) {
                if (text[pos] == '\\' && pos + 1 < text.length) {
                    pos++
                }
                sb.append(text[pos])
                pos++
            }
            if (pos < text.length && text[pos] == quote) pos++
            return sb.toString()
        }

        private fun parseNumber(): Double {
            skipWhitespaceAndComments()
            val start = pos
            if (pos < text.length && text[pos] == '-') pos++
            while (pos < text.length && (text[pos].isDigit() || text[pos] == '.')) {
                pos++
            }
            val numStr = text.substring(start, pos)
            return numStr.toDoubleOrNull() ?: 0.0
        }

        private fun skipWhitespaceAndComments() {
            while (pos < text.length) {
                if (text[pos].isWhitespace()) {
                    pos++
                    continue
                }
                if (pos + 1 < text.length && text[pos] == '/' && text[pos + 1] == '/') {
                    while (pos < text.length && text[pos] != '\n') pos++
                    continue
                }
                break
            }
        }

        private fun parseHexLong(raw: String): Long {
            return try {
                if (raw.startsWith("0x", ignoreCase = true)) {
                    raw.substring(2).toLong(16)
                } else {
                    raw.toLong()
                }
            } catch (e: Exception) {
                0xFF2196F3L
            }
        }

        private fun mapNamedColor(name: String, shade: Int): Long {
            return when (name.lowercase()) {
                "blue" -> if (shade >= 700) 0xFF1976D2L else if (shade <= 200) 0xFF90CAF9L else 0xFF2196F3L
                "red" -> if (shade >= 700) 0xFFD32F2FL else 0xFFF44336L
                "green" -> if (shade >= 700) 0xFF388E3CL else 0xFF4CAF50L
                "amber" -> 0xFFFFC107L
                "orange" -> 0xFFFF9800L
                "purple" -> 0xFF9C27B0L
                "deeppurple" -> 0xFF673AB7L
                "indigo" -> 0xFF3F51B5L
                "cyan" -> 0xFF00BCD4L
                "teal" -> 0xFF009688L
                "pink" -> 0xFFE91E63L
                "white" -> 0xFFFFFFFFL
                "black" -> 0xFF000000L
                "grey", "gray" -> 0xFF9E9E9EL
                else -> 0xFF2196F3L
            }
        }
    }
}
