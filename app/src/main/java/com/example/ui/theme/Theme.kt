package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val title: String) {
    DARK("Dark"),
    LIGHT("Light"),
    SYSTEM("System Default");

    companion object {
        fun fromString(value: String): AppThemeMode {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: LIGHT
        }
    }
}

val LocalAppTokens = staticCompositionLocalOf { LightDesignTokens }

object AppTheme {
    val tokens: AppDesignTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTokens.current
}

private val DarkM3ColorScheme = darkColorScheme(
    primary = DarkPrimaryAccent,
    onPrimary = DarkButtonPrimaryText,
    primaryContainer = DarkSecondarySurface,
    onPrimaryContainer = DarkTextPrimary,
    secondary = DarkSecondaryAccent,
    onSecondary = DarkMainBackground,
    secondaryContainer = DarkElevatedSurface,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = DarkWarning,
    onTertiary = DarkMainBackground,
    background = DarkMainBackground,
    onBackground = DarkTextPrimary,
    surface = DarkPrimarySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSecondarySurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF282F26),
    error = DarkError,
    onError = Color.White
)

private val LightM3ColorScheme = lightColorScheme(
    primary = PaletteAccentPrimary,             // #5B5FEF
    onPrimary = Color.White,
    primaryContainer = PaletteAccentSubtleBg,   // #EEF0FF
    onPrimaryContainer = PaletteAccentPrimary,
    secondary = PaletteAccentSecondary,         // #7C83FF
    onSecondary = Color.White,
    secondaryContainer = PaletteSecondarySurface,// #F0F3F9
    onSecondaryContainer = PaletteTextPrimary,
    tertiary = PaletteWarning,                  // #EAA12B
    onTertiary = Color.White,
    background = PaletteMainBg,                 // #F7F8FC
    onBackground = PaletteTextPrimary,          // #172033
    surface = PaletteCardBg,                    // #FFFFFF
    onSurface = PaletteTextPrimary,             // #172033
    surfaceVariant = PaletteSecondarySurface,   // #F0F3F9
    onSurfaceVariant = PaletteTextSecondary,    // #667085
    outline = PaletteBorder,                    // #E5E9F2
    outlineVariant = Color(0xFFEDF2F7),
    error = PaletteError,                       // #E5484D
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> systemDark
    }

    val tokens = if (isDark) DarkDesignTokens else LightDesignTokens
    val colorScheme = if (isDark) DarkM3ColorScheme else LightM3ColorScheme

    CompositionLocalProvider(LocalAppTokens provides tokens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
