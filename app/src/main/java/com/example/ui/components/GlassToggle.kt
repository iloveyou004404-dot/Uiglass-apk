package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalGlassTokens
import kotlin.math.roundToInt

@Composable
fun GlassToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "glass_toggle"
) {
    val tokens = LocalGlassTokens.current
    val trackWidth = 52.dp
    val trackHeight = 32.dp
    val thumbSize = 26.dp
    val shape = RoundedCornerShape(16.dp)

    val thumbOffsetPercent by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 500f),
        label = "glass_toggle_offset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) tokens.accentColor.copy(alpha = 0.85f) else tokens.surfaceElevatedColor.copy(alpha = 0.5f),
        label = "glass_toggle_track_color"
    )

    val thumbGlow by animateColorAsState(
        targetValue = if (checked) Color.White else Color(0xFFE2E8F0),
        label = "glass_toggle_thumb_glow"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .width(trackWidth)
            .height(trackHeight)
            .clip(shape)
            .background(trackColor)
            .border(1.dp, tokens.highlightColor.copy(alpha = 0.35f), shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!checked) }
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalTravelDp = (52 - 32).toFloat() // 20dp
        val density = androidx.compose.ui.platform.LocalDensity.current
        val travelPx = with(density) { totalTravelDp.dp.toPx() }

        Box(
            modifier = Modifier
                .offset { IntOffset(x = (thumbOffsetPercent * travelPx).roundToInt(), y = 0) }
                .size(thumbSize)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            thumbGlow,
                            thumbGlow.copy(alpha = 0.9f)
                        )
                    )
                )
                .border(0.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
        )
    }
}
