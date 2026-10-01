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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LocalGlassTokens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalendarWidget(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalGlassTokens.current
    val today = remember { Date() }
    val dayOfWeek = remember { SimpleDateFormat("EEEE", Locale.getDefault()).format(today) }
    val dayOfMonth = remember { SimpleDateFormat("d", Locale.getDefault()).format(today) }
    val monthName = remember { SimpleDateFormat("MMMM", Locale.getDefault()).format(today) }

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp,
        glowAccent = LiquidPink,
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
                    text = dayOfWeek.uppercase(Locale.getDefault()),
                    color = LiquidPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(LiquidPink.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = LiquidPink,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = dayOfMonth,
                color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                fontSize = 44.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 44.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(tokens.accentColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sync & Deep Work • 2:00 PM",
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.75f) else Color(0xFF334155),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
