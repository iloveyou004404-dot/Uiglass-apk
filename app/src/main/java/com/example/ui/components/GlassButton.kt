package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalGlassTokens

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    cornerRadius: Dp = 18.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    testTag: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    val tokens = LocalGlassTokens.current
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "glass_button_scale"
    )

    val backgroundBrush = if (isPrimary) {
        Brush.horizontalGradient(
            colors = listOf(
                tokens.accentColor.copy(alpha = 0.90f),
                tokens.accentColor.copy(alpha = 0.75f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                tokens.surfaceElevatedColor.copy(alpha = 0.85f),
                tokens.surfaceColor.copy(alpha = 0.60f)
            )
        )
    }

    val borderBrush = if (isPrimary) {
        Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = 0.60f),
                tokens.accentColor.copy(alpha = 0.30f)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                tokens.highlightColor.copy(alpha = 0.60f),
                tokens.borderColor.copy(alpha = 0.20f)
            )
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(backgroundBrush, shape)
            .border(BorderStroke(1.dp, borderBrush), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val textColor = if (isPrimary) Color.Black else if (tokens.isDark) Color.White else Color(0xFF0F172A)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge.copy(color = textColor)) {
                content()
            }
        }
    }
}
