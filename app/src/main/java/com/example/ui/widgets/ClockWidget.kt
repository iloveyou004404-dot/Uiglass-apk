package com.example.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LocalGlassTokens
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockWidget(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalGlassTokens.current
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    val timeStr = remember(currentTime) {
        SimpleDateFormat("h:mm", Locale.getDefault()).format(currentTime)
    }
    val amPm = remember(currentTime) {
        SimpleDateFormat("a", Locale.getDefault()).format(currentTime)
    }

    val cal = remember(currentTime) {
        Calendar.getInstance().apply { time = currentTime }
    }
    val hour = cal.get(Calendar.HOUR)
    val minute = cal.get(Calendar.MINUTE)
    val second = cal.get(Calendar.SECOND)

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "WORLD CLOCK",
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = timeStr,
                        color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " $amPm",
                        color = tokens.accentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Text(
                    text = "Cupertino • PDT",
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF334155),
                    fontSize = 12.sp
                )
            }

            // Minimalist Analog Clock Dial
            Box(
                modifier = Modifier.size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                val accent = tokens.accentColor
                val handColor = if (tokens.isDark) Color.White else Color(0xFF0F172A)

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2

                    // Dial ring
                    drawCircle(
                        color = handColor.copy(alpha = 0.15f),
                        radius = radius,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )

                    // Hour hand
                    val hourAngle = Math.toRadians(((hour + minute / 60f) * 30 - 90).toDouble())
                    val hourEnd = Offset(
                        (center.x + (radius * 0.5f) * cos(hourAngle)).toFloat(),
                        (center.y + (radius * 0.5f) * sin(hourAngle)).toFloat()
                    )
                    drawLine(
                        color = handColor,
                        start = center,
                        end = hourEnd,
                        strokeWidth = 3.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Minute hand
                    val minAngle = Math.toRadians((minute * 6 - 90).toDouble())
                    val minEnd = Offset(
                        (center.x + (radius * 0.72f) * cos(minAngle)).toFloat(),
                        (center.y + (radius * 0.72f) * sin(minAngle)).toFloat()
                    )
                    drawLine(
                        color = handColor.copy(alpha = 0.85f),
                        start = center,
                        end = minEnd,
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Second hand
                    val secAngle = Math.toRadians((second * 6 - 90).toDouble())
                    val secEnd = Offset(
                        (center.x + (radius * 0.82f) * cos(secAngle)).toFloat(),
                        (center.y + (radius * 0.82f) * sin(secAngle)).toFloat()
                    )
                    drawLine(
                        color = accent,
                        start = center,
                        end = secEnd,
                        strokeWidth = 1.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Center pin
                    drawCircle(color = accent, radius = 3.dp.toPx(), center = center)
                }
            }
        }
    }
}
