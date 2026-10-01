package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalGlassTokens

@Composable
fun GlassControlTile(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subLabel: String? = null,
    activeColor: Color? = null,
    testTag: String = "control_tile"
) {
    val tokens = LocalGlassTokens.current
    val effectiveActiveColor = activeColor ?: tokens.accentColor
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
        label = "tile_scale"
    )

    val tileBgColor by animateColorAsState(
        targetValue = if (isActive) effectiveActiveColor.copy(alpha = 0.85f) else tokens.surfaceElevatedColor.copy(alpha = 0.45f),
        label = "tile_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isActive) Color.Black else if (tokens.isDark) Color.White else Color(0xFF0F172A),
        label = "tile_content"
    )

    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .clip(shape)
            .background(tileBgColor, shape)
            .border(
                BorderStroke(
                    1.dp,
                    if (isActive) Color.White.copy(alpha = 0.5f) else tokens.highlightColor.copy(alpha = 0.2f)
                ),
                shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) Color.Black.copy(alpha = 0.12f) else tokens.surfaceElevatedColor.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = label,
                    color = contentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subLabel.isNullOrBlank()) {
                    Text(
                        text = subLabel,
                        color = contentColor.copy(alpha = 0.75f),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun GlassCircleControl(
    icon: ImageVector,
    contentDescription: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color? = null,
    testTag: String = "circle_control"
) {
    val tokens = LocalGlassTokens.current
    val effectiveActive = activeColor ?: tokens.accentColor
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
        label = "circle_scale"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isActive) effectiveActive else tokens.surfaceElevatedColor.copy(alpha = 0.55f),
        label = "circle_bg"
    )

    val tint by animateColorAsState(
        targetValue = if (isActive) Color.Black else if (tokens.isDark) Color.White else Color(0xFF0F172A),
        label = "circle_tint"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .size(52.dp)
            .clip(CircleShape)
            .background(bgColor, CircleShape)
            .border(1.dp, tokens.highlightColor.copy(alpha = 0.35f), CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}
