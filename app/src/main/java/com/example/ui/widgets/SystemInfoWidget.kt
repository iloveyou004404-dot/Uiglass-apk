package com.example.ui.widgets

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LocalGlassTokens

@Composable
fun SystemInfoWidget(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalGlassTokens.current
    val osInfo = remember { "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})" }
    val deviceModel = remember { "${Build.MANUFACTURER.replaceFirstChar { it.uppercase() }} ${Build.MODEL}" }

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp,
        glowAccent = tokens.accentColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYSTEM STATUS",
                    color = tokens.accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "120 Hz ProMotion",
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column {
                Text(
                    text = deviceModel,
                    color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = osInfo,
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF475569),
                    fontSize = 12.sp
                )
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Memory Efficiency",
                        color = if (tokens.isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "4.2 GB / 8.0 GB",
                        color = if (tokens.isDark) Color.White.copy(alpha = 0.8f) else Color(0xFF334155),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { 0.52f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = tokens.accentColor,
                    trackColor = tokens.surfaceElevatedColor.copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}
