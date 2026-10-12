package com.example.engine

data class ExecutionResult(
    val output: String,
    val executionTimeMs: Long,
    val isSuccess: Boolean,
    val widgetTreePreview: String? = null
)

object DartSimulatorEngine {

    fun execute(code: String): ExecutionResult {
        val startTime = System.currentTimeMillis()
        val trimmed = code.trim()

        if (trimmed.isEmpty()) {
            return ExecutionResult(
                output = "Error: No code provided to execute.",
                executionTimeMs = 0,
                isSuccess = false
            )
        }

        val lines = trimmed.lines()
        val stdoutLines = mutableListOf<String>()
        val variables = mutableMapOf<String, String>()
        var foundFlutterWidget = false

        // Look for Flutter widgets
        if (code.contains("runApp(") || code.contains("StatelessWidget") || code.contains("StatefulWidget") || code.contains("Scaffold(")) {
            foundFlutterWidget = true
        }

        try {
            for (line in lines) {
                val cleanLine = line.trim()

                // Skip comments and imports
                if (cleanLine.startsWith("//") || cleanLine.startsWith("/*") || cleanLine.startsWith("import ")) {
                    continue
                }

                // Simple variable assignment: int a = 5; var name = "Dart"; final msg = 'Hello';
                val varRegex = Regex("""(?:var|final|const|int|double|String|bool)\s+([a-zA-Z0-9_]+)\s*=\s*(.+?);""")
                val varMatch = varRegex.find(cleanLine)
                if (varMatch != null) {
                    val varName = varMatch.groupValues[1]
                    var rawVal = varMatch.groupValues[2].trim()
                    // Strip quotes if string
                    if ((rawVal.startsWith("\"") && rawVal.endsWith("\"")) || (rawVal.startsWith("'") && rawVal.endsWith("'"))) {
                        rawVal = rawVal.substring(1, rawVal.length - 1)
                    }
                    variables[varName] = rawVal
                }

                // Simple for loop: for (var i = 0; i < 3; i++) print(...)
                val simpleForRegex = Regex("""for\s*\(\s*(?:var|int)\s*([a-zA-Z_]+)\s*=\s*(\d+)\s*;\s*\1\s*<\s*(\d+)\s*;\s*\1\+\+\s*\)\s*print\((.+?)\);""")
                val forMatch = simpleForRegex.find(cleanLine)
                if (forMatch != null) {
                    val iterVar = forMatch.groupValues[1]
                    val startVal = forMatch.groupValues[2].toIntOrNull() ?: 0
                    val endVal = forMatch.groupValues[3].toIntOrNull() ?: 3
                    val innerExpr = forMatch.groupValues[4]
                    for (i in startVal until endVal) {
                        val rendered = evaluateExpression(innerExpr, variables + (iterVar to i.toString()))
                        stdoutLines.add(rendered)
                    }
                    continue
                }

                // Direct print statement: print(...);
                val printRegex = Regex("""print\s*\((.+?)\);""")
                val printMatches = printRegex.findAll(cleanLine)
                for (match in printMatches) {
                    val rawExpr = match.groupValues[1].trim()
                    val rendered = evaluateExpression(rawExpr, variables)
                    stdoutLines.add(rendered)
                }
            }

            // If it's a Flutter app and no explicit prints, render widget tree launch log
            val widgetPreview = if (foundFlutterWidget) {
                generateWidgetPreview(code)
            } else null

            if (stdoutLines.isEmpty() && foundFlutterWidget) {
                stdoutLines.add("flutter: [INFO] Launching lib/main.dart on Android Device in debug mode...")
                stdoutLines.add("flutter: [INFO] Flutter Engine initialized. Frame rendered at 60 fps.")
                stdoutLines.add("flutter: [HOT RELOAD] Ready. Syncing files with device completed.")
            } else if (stdoutLines.isEmpty()) {
                stdoutLines.add("Program exited with code 0 (no output written to console).")
            }

            val execTime = (System.currentTimeMillis() - startTime).coerceAtLeast(18)
            return ExecutionResult(
                output = stdoutLines.joinToString("\n"),
                executionTimeMs = execTime,
                isSuccess = true,
                widgetTreePreview = widgetPreview
            )

        } catch (e: Exception) {
            return ExecutionResult(
                output = "Unhandled Exception in Dart VM: ${e.message ?: "Syntax or runtime error"}",
                executionTimeMs = (System.currentTimeMillis() - startTime).coerceAtLeast(10),
                isSuccess = false
            )
        }
    }

    private fun evaluateExpression(expr: String, vars: Map<String, String>): String {
        var str = expr.trim()

        // String literals with double or single quotes
        if ((str.startsWith("\"") && str.endsWith("\"")) || (str.startsWith("'") && str.endsWith("'"))) {
            var content = str.substring(1, str.length - 1)
            // Replace interpolated variables: $var or ${var}
            for ((key, value) in vars) {
                content = content.replace("\${$key}", value)
                content = content.replace("$$key", value)
            }
            return content
        }

        // String concatenation with '+': e.g. "Hello " + name or "Total: " + 42
        if (str.contains("+") && (str.contains("\"") || str.contains("'") || vars.keys.any { str.contains(it) })) {
            val parts = str.split("+")
            val sb = StringBuilder()
            for (part in parts) {
                sb.append(evaluateExpression(part.trim(), vars))
            }
            return sb.toString()
        }

        // Variable lookup
        if (vars.containsKey(str)) {
            return vars[str] ?: str
        }

        // Simple arithmetic: 5 + 3, 10 * 2, etc.
        val arithRegex = Regex("""(\d+)\s*([\+\-\*\/])\s*(\d+)""")
        val arithMatch = arithRegex.find(str)
        if (arithMatch != null) {
            val a = arithMatch.groupValues[1].toIntOrNull() ?: 0
            val op = arithMatch.groupValues[2]
            val b = arithMatch.groupValues[3].toIntOrNull() ?: 0
            val res = when (op) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> if (b != 0) a / b else 0
                else -> 0
            }
            return res.toString()
        }

        return str
    }

    private fun generateWidgetPreview(code: String): String {
        val tree = StringBuilder()
        tree.append("📱 [Simulated Flutter Render Tree]\n")
        if (code.contains("MaterialApp")) {
            tree.append("  └─ MaterialApp (theme: ThemeData(primarySwatch: Colors.blue))\n")
        }
        if (code.contains("Scaffold")) {
            tree.append("      └─ Scaffold\n")
            if (code.contains("AppBar")) {
                tree.append("          ├─ AppBar(title: Text(...))\n")
            }
            if (code.contains("body:")) {
                tree.append("          └─ body: ")
                when {
                    code.contains("Column") -> tree.append("Column [vertical children]\n")
                    code.contains("Row") -> tree.append("Row [horizontal children]\n")
                    code.contains("ListView") -> tree.append("ListView (scrollable viewport)\n")
                    code.contains("Center") -> tree.append("Center -> Container/Text\n")
                    else -> tree.append("Widget hierarchy rendered successfully\n")
                }
            }
        }
        return tree.toString()
    }
}
