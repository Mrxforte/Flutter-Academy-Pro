package com.example.playground.engine

enum class PreviewDeviceSize(val displayName: String, val widthDp: Int, val heightDp: Int) {
    COMPACT("Mobile S (320dp)", 320, 520),
    STANDARD("Mobile (375dp)", 375, 580),
    TABLET("Tablet (520dp)", 520, 580);
}

enum class CanvasThemeMode {
    LIGHT,
    DARK
}

data class CompileDiagnostic(
    val line: Int,
    val column: Int,
    val message: String,
    val isError: Boolean = true
)

data class PlaygroundState(
    val code: String,
    val parsedRootNode: FlutterWidgetNode?,
    val isCompiling: Boolean = false,
    val compileTimeMs: Long = 0,
    val diagnostics: List<CompileDiagnostic> = emptyList(),
    val hasError: Boolean = false,
    val activeDeviceSize: PreviewDeviceSize = PreviewDeviceSize.STANDARD,
    val canvasTheme: CanvasThemeMode = CanvasThemeMode.DARK,
    val executionLog: List<String> = emptyList()
)
