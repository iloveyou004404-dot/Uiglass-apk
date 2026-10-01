package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalGlassTokens

@Composable
fun GlassHorizontalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    testTag: String = "glass_slider"
) {
    val tokens = LocalGlassTokens.current
    var widthPx by remember { mutableFloatStateOf(1f) }
    val normalized = ((value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)

    val animatedPercent by animateFloatAsState(targetValue = normalized, label = "slider_progress")

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(28.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.surfaceElevatedColor.copy(alpha = 0.45f))
            .border(1.dp, tokens.highlightColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .onGloballyPositioned { widthPx = it.size.width.toFloat() }
            .pointerInput(range) {
                detectTapGestures { offset ->
                    val ratio = (offset.x / widthPx).coerceIn(0f, 1f)
                    val newValue = range.start + ratio * (range.endInclusive - range.start)
                    onValueChange(newValue)
                }
            }
            .pointerInput(range) {
                detectDragGestures { change, _ ->
                    val ratio = (change.position.x / widthPx).coerceIn(0f, 1f)
                    val newValue = range.start + ratio * (range.endInclusive - range.start)
                    onValueChange(newValue)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Active fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedPercent)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            tokens.accentColor.copy(alpha = 0.65f),
                            tokens.accentColor.copy(alpha = 0.90f)
                        )
                    )
                )
        )
    }
}

@Composable
fun GlassVerticalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String = "glass_vertical_slider"
) {
    val tokens = LocalGlassTokens.current
    var heightPx by remember { mutableFloatStateOf(1f) }
    val normalized = value.coerceIn(0f, 1f)
    val animatedPercent by animateFloatAsState(targetValue = normalized, label = "vert_slider_progress")

    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(22.dp))
            .background(tokens.surfaceElevatedColor.copy(alpha = 0.45f))
            .border(1.dp, tokens.highlightColor.copy(alpha = 0.25f), RoundedCornerShape(22.dp))
            .onGloballyPositioned { heightPx = it.size.height.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val ratio = 1f - (offset.y / heightPx).coerceIn(0f, 1f)
                    onValueChange(ratio)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val ratio = 1f - (change.position.y / heightPx).coerceIn(0f, 1f)
                    onValueChange(ratio)
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Active fill upwards
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedPercent)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.75f),
                            Color.White.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Overlay Icon & Percentage
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val contentColor = if (normalized > 0.85f) Color.Black else if (tokens.isDark) Color.White else Color(0xFF0F172A)
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
            val percentInt = (normalized * 100).toInt()
            Text(
                text = "$percentInt%",
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}
