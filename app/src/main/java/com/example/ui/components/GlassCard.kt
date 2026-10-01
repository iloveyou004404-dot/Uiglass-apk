package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalGlassTokens

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    cornerRadius: Dp? = null,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    tintColor: Color? = null,
    glowAccent: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val tokens = LocalGlassTokens.current
    val effectiveCornerRadius = cornerRadius ?: tokens.cornerRadius.dp
    val effectiveShape = shape ?: RoundedCornerShape(effectiveCornerRadius)
    
    val baseSurface = tintColor ?: if (tokens.isDark) tokens.surfaceColor else tokens.surfaceColor
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            tokens.highlightColor.copy(alpha = if (tokens.isDark) 0.55f else 0.85f),
            tokens.borderColor.copy(alpha = if (tokens.isDark) 0.15f else 0.35f),
            (glowAccent ?: tokens.accentColor).copy(alpha = 0.20f),
            tokens.borderColor.copy(alpha = if (tokens.isDark) 0.08f else 0.20f)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val surfaceBrush = Brush.verticalGradient(
        colors = listOf(
            baseSurface.copy(alpha = (baseSurface.alpha * 1.15f).coerceAtMost(0.95f)),
            baseSurface.copy(alpha = (baseSurface.alpha * 0.85f).coerceAtLeast(0.15f))
        )
    )

    Box(
        modifier = modifier
            .clip(effectiveShape)
            .background(surfaceBrush, effectiveShape)
            .border(BorderStroke(borderWidth, borderBrush), effectiveShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .drawBehind {
                // Subtle top specular highlight ribbon
                drawLine(
                    color = Color.White.copy(alpha = if (tokens.isDark) 0.18f else 0.40f),
                    start = Offset(size.width * 0.15f, 1f),
                    end = Offset(size.width * 0.85f, 1f),
                    strokeWidth = 2f
                )
            },
        content = content
    )
}
