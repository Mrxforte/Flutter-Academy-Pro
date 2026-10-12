package com.example.engine

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.*

object DartSyntaxHighlighter {

    private val KEYWORDS = setOf(
        "abstract", "as", "assert", "async", "await", "break", "case", "catch", "class",
        "const", "continue", "default", "do", "dynamic", "else", "enum", "export",
        "extends", "extension", "external", "factory", "false", "final", "finally",
        "for", "get", "if", "implements", "import", "in", "interface", "is", "late",
        "library", "mixin", "new", "null", "on", "operator", "part", "required",
        "rethrow", "return", "sealed", "set", "show", "static", "super", "switch",
        "sync", "this", "throw", "true", "try", "typedef", "var", "void", "while",
        "with", "yield"
    )

    private val TYPES = setOf(
        "Widget", "StatelessWidget", "StatefulWidget", "State", "BuildContext",
        "MaterialApp", "Scaffold", "AppBar", "Text", "Container", "Row", "Column",
        "ListView", "Stack", "Positioned", "Expanded", "Flexible", "SizedBox",
        "Padding", "Center", "ElevatedButton", "TextButton", "OutlinedButton",
        "IconButton", "FloatingActionButton", "Card", "TextField", "TextFormField",
        "GestureDetector", "InkWell", "Image", "Icon", "Icons", "Colors", "Color",
        "EdgeInsets", "TextStyle", "FontWeight", "Key", "String", "int", "double",
        "num", "bool", "List", "Map", "Set", "Future", "Stream", "StreamController",
        "Duration", "DateTime", "Navigator", "MaterialPageRoute", "Provider",
        "ChangeNotifier", "Bloc", "Cubit", "Consumer", "CustomPainter", "Canvas", "Paint"
    )

    fun highlight(code: String, isDark: Boolean = true): AnnotatedString {
        val keywordColor = if (isDark) CodeKeywordDark else CodeKeywordLight
        val typeColor = if (isDark) CodeTypeDark else CodeTypeLight
        val stringColor = if (isDark) CodeStringDark else CodeStringLight
        val commentColor = if (isDark) CodeCommentDark else CodeCommentLight
        val numberColor = if (isDark) CodeNumberDark else CodeNumberLight
        val functionColor = if (isDark) CodeFunctionDark else CodeFunctionLight

        return buildAnnotatedString {
            append(code)

            var i = 0
            val len = code.length

            while (i < len) {
                // Single line comment
                if (i + 1 < len && code[i] == '/' && code[i + 1] == '/') {
                    val endOfLine = code.indexOf('\n', i).let { if (it == -1) len else it }
                    addStyle(SpanStyle(color = commentColor), i, endOfLine)
                    i = endOfLine
                    continue
                }

                // Block comment
                if (i + 1 < len && code[i] == '/' && code[i + 1] == '*') {
                    val endOfComment = code.indexOf("*/", i + 2).let { if (it == -1) len else it + 2 }
                    addStyle(SpanStyle(color = commentColor), i, endOfComment)
                    i = endOfComment
                    continue
                }

                // Strings with quotes (", ')
                if (code[i] == '"' || code[i] == '\'') {
                    val quote = code[i]
                    val start = i
                    i++
                    while (i < len && code[i] != quote) {
                        if (code[i] == '\\' && i + 1 < len) i++
                        if (code[i] == '\n') break
                        i++
                    }
                    val end = if (i < len && code[i] == quote) i + 1 else i
                    addStyle(SpanStyle(color = stringColor), start, end)
                    continue
                }

                // Numbers
                if (code[i].isDigit()) {
                    val start = i
                    while (i < len && (code[i].isDigit() || code[i] == '.')) {
                        i++
                    }
                    addStyle(SpanStyle(color = numberColor), start, i)
                    continue
                }

                // Words
                if (code[i].isLetter() || code[i] == '_' || code[i] == '@') {
                    val start = i
                    while (i < len && (code[i].isLetterOrDigit() || code[i] == '_')) {
                        i++
                    }
                    val word = code.substring(start, i)
                    when {
                        word.startsWith("@") -> {
                            addStyle(SpanStyle(color = typeColor, fontWeight = FontWeight.Bold), start, i)
                        }
                        KEYWORDS.contains(word) -> {
                            addStyle(SpanStyle(color = keywordColor, fontWeight = FontWeight.Bold), start, i)
                        }
                        TYPES.contains(word) || (word.isNotEmpty() && word[0].isUpperCase()) -> {
                            addStyle(SpanStyle(color = typeColor, fontWeight = FontWeight.SemiBold), start, i)
                        }
                        i < len && code[i] == '(' -> {
                            addStyle(SpanStyle(color = functionColor), start, i)
                        }
                    }
                    continue
                }

                i++
            }
        }
    }
}
