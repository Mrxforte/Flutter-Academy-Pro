package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// =========================================================================
// SAGE GREEN + WARM NEUTRAL DESIGN SYSTEM PALETTE
// =========================================================================

// Primary Light Theme: Whiter Background, Whiter Cards & Crisp Black Text
val SagePrimary = Color(0xFFCDEBB9)         // Primary sage green
val SageSoft = Color(0xFFE8F5DF)            // Lighter soft sage green tint
val SageDeep = Color(0xFFA9CF91)            // Deep sage green
val WarmBeige = Color(0xFFF7F5F3)           // Very light warm neutral accent
val PureWhiteBg = Color(0xFFFFFFFF)         // Pure clean white background
val PureWhiteSurface = Color(0xFFFFFFFF)    // Pure white card surface
val PureWhiteSecondary = Color(0xFFF7F8FA)  // Whiter crisp secondary surface
val LightTextPrimary = Color(0xFF000000)    // Pure black text
val LightTextSecondary = Color(0xFF141414)  // Dark solid black text
val LightTextMuted = Color(0xFF262626)      // Dark black/charcoal text
val LightBorder = Color(0xFFE2E4E8)         // Clean crisp subtle border
val LightButtonPrimary = Color(0xFF000000)  // Solid black primary button
val LightButtonPrimaryText = Color(0xFFFFFFFF)
val LightButtonSecondary = Color(0xFFF3F4F6)// Clean neutral secondary button
val LightSuccess = Color(0xFF15803D)        // High contrast success green
val LightWarning = Color(0xFFB45309)        // High contrast amber warning
val LightError = Color(0xFFDC2626)          // High contrast error red
val LightCodeBackground = Color(0xFF14181F) // Deep code editor background
val LightCodeText = Color(0xFFF1F5F9)

// Complementary Dark Theme: Deep Muted Dark Green
val DarkMainBackground = Color(0xFF141614)  // Main dark background
val DarkPrimarySurface = Color(0xFF1C201C)  // Primary surface
val DarkSecondarySurface = Color(0xFF252A24)// Secondary surface
val DarkElevatedSurface = Color(0xFF2D332B) // Elevated surface
val DarkTextPrimary = Color(0xFFF5F4EF)     // Near-white text
val DarkTextSecondary = Color(0xFFB9BDB4)   // Secondary text
val DarkTextMuted = Color(0xFF898F84)       // Muted text
val DarkBorder = Color(0xFF353B32)          // Dark border
val DarkPrimaryAccent = Color(0xFFCDEBB9)   // Sage accent
val DarkSecondaryAccent = Color(0xFFA9CF91) // Deep sage accent
val DarkButtonPrimary = Color(0xFFCDEBB9)   // Sage button in dark mode
val DarkButtonPrimaryText = Color(0xFF141614) // Dark text on sage button
val DarkButtonSecondary = Color(0xFF252A24)
val DarkCodeBackground = Color(0xFF101210)  // Deep code editor background
val DarkCodeText = Color(0xFFE8EDE4)
val DarkSuccess = Color(0xFF79D69B)
val DarkWarning = Color(0xFFF0C777)
val DarkError = Color(0xFFFF8585)

// Code Syntax Highlighting Palette - Dark
val CodeKeywordDark = Color(0xFFCDEBB9)
val CodeTypeDark = Color(0xFFA9CF91)
val CodeStringDark = Color(0xFFD7EDC9)
val CodeCommentDark = Color(0xFF898F84)
val CodeNumberDark = Color(0xFFF0C777)
val CodeFunctionDark = Color(0xFF79D69B)

// Code Syntax Highlighting Palette - Light
val CodeKeywordLight = Color(0xFF79D69B)
val CodeTypeLight = Color(0xFFA9CF91)
val CodeStringLight = Color(0xFFD7EDC9)
val CodeCommentLight = Color(0xFF898F84)
val CodeNumberLight = Color(0xFFF0C777)
val CodeFunctionLight = Color(0xFFCDEBB9)

// Semantic Design Tokens representation
@Immutable
data class AppDesignTokens(
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val primary: Color,              // Sage primary
    val secondary: Color,            // Deep sage
    val sagePrimary: Color,          // #CDEBB9
    val sageSoft: Color,             // #D7EDC9
    val sageDeep: Color,             // #A9CF91
    val warmBeige: Color,            // #E5D8D0
    val warmIvory: Color,            // #FAF6F4
    val buttonPrimary: Color,        // Black (Light) / Sage (Dark)
    val buttonPrimaryText: Color,    // White (Light) / Dark (Dark)
    val buttonSecondary: Color,
    val buttonSecondaryText: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val codeBackground: Color,
    val codeText: Color,
    val isDark: Boolean
)

val DarkDesignTokens = AppDesignTokens(
    background = DarkMainBackground,
    surface = DarkPrimarySurface,
    surfaceSecondary = DarkSecondarySurface,
    surfaceElevated = DarkElevatedSurface,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textMuted = DarkTextMuted,
    border = DarkBorder,
    primary = DarkPrimaryAccent,
    secondary = DarkSecondaryAccent,
    sagePrimary = SagePrimary,
    sageSoft = SageSoft,
    sageDeep = SageDeep,
    warmBeige = Color(0xFF262D24),
    warmIvory = DarkPrimarySurface,
    buttonPrimary = DarkButtonPrimary,
    buttonPrimaryText = DarkButtonPrimaryText,
    buttonSecondary = DarkButtonSecondary,
    buttonSecondaryText = DarkTextPrimary,
    success = DarkSuccess,
    warning = DarkWarning,
    error = DarkError,
    codeBackground = DarkCodeBackground,
    codeText = DarkCodeText,
    isDark = true
)

val LightDesignTokens = AppDesignTokens(
    background = PureWhiteBg,
    surface = PureWhiteSurface,
    surfaceSecondary = PureWhiteSecondary,
    surfaceElevated = PureWhiteSurface,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightTextMuted,
    border = LightBorder,
    primary = SagePrimary,
    secondary = SageDeep,
    sagePrimary = SagePrimary,
    sageSoft = SageSoft,
    sageDeep = SageDeep,
    warmBeige = WarmBeige,
    warmIvory = PureWhiteBg,
    buttonPrimary = LightButtonPrimary,
    buttonPrimaryText = LightButtonPrimaryText,
    buttonSecondary = LightButtonSecondary,
    buttonSecondaryText = LightTextPrimary,
    success = LightSuccess,
    warning = LightWarning,
    error = LightError,
    codeBackground = LightCodeBackground,
    codeText = LightCodeText,
    isDark = false
)

// Legacy compatibility aliases for existing references
val FlutterSkyBlue = SagePrimary
val FlutterCyan = SageDeep
val FlutterBluePrimary = SagePrimary
val FlutterNavyDeep = DarkMainBackground
val FlutterNavySurface = DarkPrimarySurface
val FlutterNavyCard = DarkSecondarySurface
val FlutterNavyBorder = DarkBorder
val DartOrange = LightWarning
val SuccessGreen = LightSuccess
val WarningAmber = LightWarning
val ErrorRed = LightError
val CodeBackgroundDark = DarkCodeBackground
val CodeKeyword = CodeKeywordDark
val CodeType = CodeTypeDark
val CodeString = CodeStringDark
val CodeComment = CodeCommentDark
val CodeNumber = CodeNumberDark
val CodeFunction = CodeFunctionDark
