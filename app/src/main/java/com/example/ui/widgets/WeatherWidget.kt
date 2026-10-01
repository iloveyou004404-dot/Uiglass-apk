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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LocalGlassTokens

@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier,
    location: String = "San Francisco",
    temperature: String = "72°",
    condition: String = "Mostly Clear",
    highLow: String = "H: 75° L: 58°",
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalGlassTokens.current

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp,
        glowAccent = LiquidCyan,
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
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = location,
                        color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = temperature,
                        color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(tokens.accentColor.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = condition,
                        tint = tokens.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = condition,
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.8f) else Color(0xFF334155),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = highLow,
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                    fontSize = 11.sp
                )
            }
        }
    }
}
