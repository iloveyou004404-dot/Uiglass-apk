package com.example.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LocalGlassTokens

@Composable
fun BatteryWidget(
    level: Int,
    isCharging: Boolean,
    modifier: Modifier = Modifier,
    isPowerSave: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalGlassTokens.current
    val progress = (level / 100f).coerceIn(0f, 1f)
    val batteryColor = when {
        isCharging -> LiquidGreen
        level <= 20 -> Color(0xFFFF453A)
        level <= 40 -> Color(0xFFFF9F0A)
        else -> LiquidGreen
    }

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp,
        glowAccent = batteryColor,
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Battery",
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$level%",
                    color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isCharging) "Charging fast" else if (isPowerSave) "Power Saver On" else "Liquid Power",
                    color = batteryColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier.size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = tokens.surfaceElevatedColor.copy(alpha = 0.5f),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = batteryColor,
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round
                )
                if (isCharging) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Charging",
                        tint = batteryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
