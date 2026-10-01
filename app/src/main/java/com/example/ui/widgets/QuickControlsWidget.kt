package com.example.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassCircleControl
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LocalGlassTokens

@Composable
fun QuickControlsWidget(
    isWifiOn: Boolean,
    isBluetoothOn: Boolean,
    isFlashlightOn: Boolean,
    isDarkMode: Boolean,
    onToggleWifi: () -> Unit,
    onToggleBluetooth: () -> Unit,
    onToggleFlashlight: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = LocalGlassTokens.current

    GlassCard(
        modifier = modifier.height(150.dp),
        cornerRadius = 24.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "QUICK TOGGLES",
                color = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassCircleControl(
                    icon = Icons.Default.Wifi,
                    contentDescription = "Wi-Fi",
                    isActive = isWifiOn,
                    onClick = onToggleWifi,
                    activeColor = LiquidCyan
                )
                GlassCircleControl(
                    icon = Icons.Default.Bluetooth,
                    contentDescription = "Bluetooth",
                    isActive = isBluetoothOn,
                    onClick = onToggleBluetooth,
                    activeColor = tokens.accentColor
                )
                GlassCircleControl(
                    icon = Icons.Default.FlashlightOn,
                    contentDescription = "Flashlight",
                    isActive = isFlashlightOn,
                    onClick = onToggleFlashlight,
                    activeColor = Color(0xFFFFD60A)
                )
                GlassCircleControl(
                    icon = Icons.Default.Nightlight,
                    contentDescription = "Dark Mode",
                    isActive = isDarkMode,
                    onClick = onToggleDarkMode,
                    activeColor = tokens.accentColor
                )
            }
        }
    }
}
