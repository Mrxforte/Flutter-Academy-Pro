package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// =========================================================================
// PREMIUM LIGHT THEME DESIGN SYSTEM PALETTE
// Starting palette defined by the user:
// Main App Background:      #F7F8FC
// Primary Card Background:  #FFFFFF
// Secondary Surface:        #F0F3F9
// Elevated Panels/Dialogs:  #FFFFFF
// Primary Text:             #172033
// Secondary Text:           #667085
// Muted Text & Hints:       #98A2B3
// Borders & Dividers:       #E5E9F2
// Primary Accent:           #5B5FEF
// Secondary Accent:         #7C83FF
// Subtle Accent Background: #EEF0FF
// Success:                  #16A36A
// Warning:                  #EAA12B
// Error:                    #E5484D
// =========================================================================

// Global Light Theme Tokens
val PaletteMainBg = Color(0xFFF7F8FC)
val PaletteCardBg = Color(0xFFFFFFFF)
val PaletteSecondarySurface = Color(0xFFF0F3F9)
val PaletteElevated = Color(0xFFFFFFFF)

val PaletteTextPrimary = Color(0xFF172033)
val PaletteTextSecondary = Color(0xFF667085)
val PaletteTextMuted = Color(0xFF98A2B3)
val PaletteBorder = Color(0xFFE5E9F2)

val PaletteAccentPrimary = Color(0xFF5B5FEF)
val PaletteAccentSecondary = Color(0xFF7C83FF)
val PaletteAccentSubtleBg = Color(0xFFEEF0FF)

val PaletteSuccess = Color(0xFF16A36A)
val PaletteWarning = Color(0xFFEAA12B)
val PaletteError = Color(0xFFE5484D)

// Code Syntax Highlighting Palette - Light Mode (Clean, readable on #F8FAFC)
val LightCodeBg = Color(0xFFF8FAFC)
val LightCodeBorder = Color(0xFFE2E8F0)
val LightCodeText = Color(0xFF1E293B)
val LightCodeKeyword = Color(0xFF4F46E5)     // Indigo keyword
val LightCodeType = Color(0xFF0284C7)        // Sky/Cyan type
val LightCodeString = Color(0xFF0D9488)      // Teal string
val LightCodeComment = Color(0xFF94A3B8)     // Slate comment
val LightCodeNumber = Color(0xFFD97706)      // Amber number
val LightCodeFunction = Color(0xFF2563EB)    // Blue function

// Dark Theme Tokens (Preserved for dark mode)
val DarkMainBackground = Color(0xFF0F172A)
val DarkPrimarySurface = Color(0xFF1E293B)
val DarkSecondarySurface = Color(0xFF334155)
val DarkElevatedSurface = Color(0xFF1E293B)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkTextMuted = Color(0xFF64748B)
val DarkBorder = Color(0xFF334155)
val DarkPrimaryAccent = Color(0xFF818CF8)
val DarkSecondaryAccent = Color(0xFFA5B4FC)
val DarkButtonPrimary = Color(0xFF6366F1)
val DarkButtonPrimaryText = Color(0xFFFFFFFF)
val DarkButtonSecondary = Color(0xFF1E293B)
val DarkCodeBackground = Color(0xFF090D16)
val DarkCodeText = Color(0xFFE2E8F0)
val DarkSuccess = Color(0xFF34D399)
val DarkWarning = Color(0xFFFBBF24)
val DarkError = Color(0xFFF87171)

// Code Syntax Highlighting Palette - Dark Mode
val CodeKeywordDark = Color(0xFFA5B4FC)
val CodeTypeDark = Color(0xFF38BDF8)
val CodeStringDark = Color(0xFF4ADE80)
val CodeCommentDark = Color(0xFF64748B)
val CodeNumberDark = Color(0xFFFBBF24)
val CodeFunctionDark = Color(0xFF818CF8)

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
    val primary: Color,              // Primary accent #5B5FEF
    val secondary: Color,            // Secondary accent #7C83FF
    val subtleAccentBg: Color,       // #EEF0FF
    val sagePrimary: Color,          // Alias for backward compatibility
    val sageSoft: Color,             // Alias for subtle accent
    val sageDeep: Color,             // Alias for secondary accent
    val warmBeige: Color,            // Neutral secondary surface
    val warmIvory: Color,            // Surface background
    val buttonPrimary: Color,        // #5B5FEF in Light mode
    val buttonPrimaryText: Color,    // White in Light mode
    val buttonSecondary: Color,      // Light neutral surface
    val buttonSecondaryText: Color,  // Primary text
    val success: Color,              // #16A36A
    val warning: Color,              // #EAA12B
    val error: Color,                // #E5484D
    val codeBackground: Color,       // Clean #F8FAFC in Light mode
    val codeBorder: Color,           // #E2E8F0 in Light mode
    val codeText: Color,             // Crisp text
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
    subtleAccentBg = Color(0xFF1E1B4B),
    sagePrimary = DarkPrimaryAccent,
    sageSoft = Color(0xFF1E293B),
    sageDeep = DarkSecondaryAccent,
    warmBeige = DarkSecondarySurface,
    warmIvory = DarkPrimarySurface,
    buttonPrimary = DarkButtonPrimary,
    buttonPrimaryText = DarkButtonPrimaryText,
    buttonSecondary = DarkButtonSecondary,
    buttonSecondaryText = DarkTextPrimary,
    success = DarkSuccess,
    warning = DarkWarning,
    error = DarkError,
    codeBackground = DarkCodeBackground,
    codeBorder = DarkBorder,
    codeText = DarkCodeText,
    isDark = true
)

val LightDesignTokens = AppDesignTokens(
    background = PaletteMainBg,               // #F7F8FC - Soft light-gray page background
    surface = PaletteCardBg,                 // #FFFFFF - Pure crisp white card background
    surfaceSecondary = PaletteSecondarySurface,// #F0F3F9 - Secondary light surface
    surfaceElevated = PaletteElevated,       // #FFFFFF - Elevated panels and dialogs
    textPrimary = PaletteTextPrimary,        // #172033 - Deep dark readable text
    textSecondary = PaletteTextSecondary,    // #667085 - Secondary descriptive text
    textMuted = PaletteTextMuted,            // #98A2B3 - Muted hints
    border = PaletteBorder,                  // #E5E9F2 - Soft subtle border
    primary = PaletteAccentPrimary,          // #5B5FEF - Modern vibrant primary accent
    secondary = PaletteAccentSecondary,      // #7C83FF - Secondary accent
    subtleAccentBg = PaletteAccentSubtleBg,  // #EEF0FF - Light tinted accent surface
    sagePrimary = PaletteAccentPrimary,      // Compatibility
    sageSoft = PaletteAccentSubtleBg,        // Compatibility
    sageDeep = PaletteAccentSecondary,       // Compatibility
    warmBeige = PaletteSecondarySurface,
    warmIvory = PaletteCardBg,
    buttonPrimary = PaletteAccentPrimary,    // #5B5FEF - Modern primary button
    buttonPrimaryText = Color.White,
    buttonSecondary = PaletteSecondarySurface,
    buttonSecondaryText = PaletteTextPrimary,
    success = PaletteSuccess,                // #16A36A
    warning = PaletteWarning,                // #EAA12B
    error = PaletteError,                    // #E5484D
    codeBackground = LightCodeBg,            // Clean #F8FAFC
    codeBorder = LightCodeBorder,            // #E2E8F0
    codeText = LightCodeText,                // #1E293B
    isDark = false
)

// Legacy compatibility aliases for existing references
val FlutterSkyBlue = PaletteAccentSecondary
val FlutterCyan = PaletteAccentPrimary
val FlutterBluePrimary = PaletteAccentPrimary
val FlutterNavyDeep = DarkMainBackground
val FlutterNavySurface = DarkPrimarySurface
val FlutterNavyCard = DarkSecondarySurface
val FlutterNavyBorder = DarkBorder
val DartOrange = PaletteWarning
val SuccessGreen = PaletteSuccess
val WarningAmber = PaletteWarning
val ErrorRed = PaletteError
val CodeBackgroundDark = DarkCodeBackground
val CodeKeyword = CodeKeywordDark
val CodeType = CodeTypeDark
val CodeString = CodeStringDark
val CodeComment = CodeCommentDark
val CodeNumber = CodeNumberDark
val CodeFunction = CodeFunctionDark
