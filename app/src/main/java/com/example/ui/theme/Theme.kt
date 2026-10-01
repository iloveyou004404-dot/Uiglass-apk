package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class GlassThemeTokens(
    val surfaceColor: Color = GlassDarkSurface,
    val surfaceElevatedColor: Color = GlassDarkSurfaceElevated,
    val borderColor: Color = GlassDarkBorder,
    val borderSubtleColor: Color = GlassDarkBorderSubtle,
    val highlightColor: Color = GlassDarkHighlight,
    val isDark: Boolean = true,
    val glassOpacity: Float = 0.65f,
    val blurStrength: Float = 25f,
    val cornerRadius: Float = 24f,
    val accentColor: Color = LiquidCyan
)

val LocalGlassTokens = staticCompositionLocalOf { GlassThemeTokens() }

private val DarkColorScheme = darkColorScheme(
    primary = LiquidCyan,
    onPrimary = Color.Black,
    primaryContainer = LiquidCyanSoft,
    onPrimaryContainer = LiquidCyan,
    secondary = LiquidViolet,
    onSecondary = Color.White,
    secondaryContainer = LiquidVioletSoft,
    onSecondaryContainer = LiquidViolet,
    tertiary = LiquidPink,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = Color(0xFF1E2638),
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = LiquidBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = LiquidBlue,
    secondary = LiquidViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE9DDFF),
    onSecondaryContainer = LiquidViolet,
    tertiary = LiquidPink,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = Color(0xFFE2E8F0),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    glassOpacity: Float = 0.70f,
    blurStrength: Float = 25f,
    cornerRadius: Float = 24f,
    accentColor: Color = LiquidCyan,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val glassTokens = if (darkTheme) {
        GlassThemeTokens(
            surfaceColor = GlassDarkSurface.copy(alpha = glassOpacity * 0.45f),
            surfaceElevatedColor = GlassDarkSurfaceElevated.copy(alpha = glassOpacity * 0.65f),
            borderColor = GlassDarkBorder.copy(alpha = glassOpacity * 0.5f),
            borderSubtleColor = GlassDarkBorderSubtle,
            highlightColor = GlassDarkHighlight,
            isDark = true,
            glassOpacity = glassOpacity,
            blurStrength = blurStrength,
            cornerRadius = cornerRadius,
            accentColor = accentColor
        )
    } else {
        GlassThemeTokens(
            surfaceColor = GlassLightSurface.copy(alpha = glassOpacity * 0.85f),
            surfaceElevatedColor = GlassLightSurfaceElevated.copy(alpha = glassOpacity * 0.95f),
            borderColor = GlassLightBorder.copy(alpha = glassOpacity * 0.7f),
            borderSubtleColor = GlassLightBorderSubtle,
            highlightColor = GlassLightHighlight,
            isDark = false,
            glassOpacity = glassOpacity,
            blurStrength = blurStrength,
            cornerRadius = cornerRadius,
            accentColor = accentColor
        )
    }

    CompositionLocalProvider(LocalGlassTokens provides glassTokens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
