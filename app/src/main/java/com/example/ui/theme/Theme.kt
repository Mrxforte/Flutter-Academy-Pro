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
    primary = LightButtonPrimary,         // Black CTA as primary in light mode
    onPrimary = LightButtonPrimaryText,   // White text on primary CTA
    primaryContainer = SageSoft,          // Soft sage container
    onPrimaryContainer = LightTextPrimary,
    secondary = SageDeep,
    onSecondary = LightTextPrimary,
    secondaryContainer = PureWhiteSecondary,
    onSecondaryContainer = LightTextPrimary,
    tertiary = LightWarning,
    onTertiary = Color.White,
    background = PureWhiteBg,             // Clean white background
    onBackground = LightTextPrimary,
    surface = PureWhiteSurface,           // Pure white cards & surfaces
    onSurface = LightTextPrimary,
    surfaceVariant = PureWhiteSecondary,  // Clean secondary surfaces
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFE5E7EB),
    error = LightError,
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
