package com.example.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassDialog
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidViolet

@Composable
fun OnboardingDialog(
    isOpen: Boolean,
    onFinish: () -> Unit
) {
    if (!isOpen) return

    GlassDialog(onDismissRequest = onFinish) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LiquidCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = LiquidCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Welcome to Liquid Glass",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Translucent Android Experience",
                        color = LiquidCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OnboardingBullet(
                icon = Icons.Default.CheckCircle,
                iconColor = LiquidGreen,
                title = "Original Translucent Architecture",
                description = "Not an iOS clone. An original Liquid Glass design with dynamic refraction and high-performance spring physics."
            )

            Spacer(modifier = Modifier.height(12.dp))

            OnboardingBullet(
                icon = Icons.Default.NotificationsActive,
                iconColor = LiquidViolet,
                title = "Optional Notification Access",
                description = "Grant Android Notification Access to render live incoming notifications into liquid glass cards. Kept strictly on-device."
            )

            Spacer(modifier = Modifier.height(12.dp))

            OnboardingBullet(
                icon = Icons.Default.Shield,
                iconColor = LiquidCyan,
                title = "Privacy-First & Secure",
                description = "No remote telemetry, zero analytics tracking, and no system lock bypass. All settings stored locally in DataStore."
            )

            Spacer(modifier = Modifier.height(24.dp))

            GlassButton(
                onClick = onFinish,
                isPrimary = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Get Started",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OnboardingBullet(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
