package com.example.ui.lockscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClockStyle
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidViolet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LockScreenClock(
    date: Date,
    style: ClockStyle,
    modifier: Modifier = Modifier,
    isLarge: Boolean = true
) {
    val timeFormatted = remember(date) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }
    val dateFormatted = remember(date) {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(date)
    }

    val fontSize = if (isLarge) 82.sp else 64.sp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date line
        Text(
            text = dateFormatted,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )

        when (style) {
            ClockStyle.FUTURISTIC_GLASS -> {
                Text(
                    text = timeFormatted,
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraLight,
                    letterSpacing = (-2).sp,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.drawBehind {
                        // Subtle liquid reflection underline
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent)
                            ),
                            start = Offset(0f, size.height - 2f),
                            end = Offset(size.width, size.height - 2f),
                            strokeWidth = 2f
                        )
                    }
                )
            }
            ClockStyle.MINIMAL_SERIF -> {
                Text(
                    text = timeFormatted,
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Normal
                )
            }
            ClockStyle.DIGITAL_BOLD -> {
                Text(
                    text = timeFormatted,
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            ClockStyle.NEON_GLOW -> {
                Text(
                    text = timeFormatted,
                    color = LiquidCyan,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            ClockStyle.LIQUID_CURVED -> {
                Text(
                    text = timeFormatted,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    }
}
