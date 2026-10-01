package com.example.ui.settings

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClockStyle
import com.example.data.GlassThemeMode
import com.example.data.UserPreferences
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassDialog
import com.example.ui.components.GlassHorizontalSlider
import com.example.ui.components.GlassToggle
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.LocalGlassTokens
import com.example.utils.BatteryInfo
import com.example.utils.SystemSettingsHelper

enum class SettingsSubscreen {
    NONE,
    DISPLAY_GLASS,
    NETWORK,
    SOUNDS_HAPTICS,
    HOME_SCREEN,
    LOCK_SCREEN,
    PRIVACY,
    BATTERY,
    ABOUT
}

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    batteryInfo: BatteryInfo,
    onBack: () -> Unit,
    onUpdatePreferences: (UserPreferences) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsHelper = remember { SystemSettingsHelper(context) }
    val tokens = LocalGlassTokens.current
    var currentSubscreen by remember { mutableStateOf(SettingsSubscreen.NONE) }

    BackHandler {
        if (currentSubscreen != SettingsSubscreen.NONE) {
            currentSubscreen = SettingsSubscreen.NONE
        } else {
            onBack()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (tokens.isDark) Color(0xFF090D14) else Color(0xFFF1F5F9))
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("settings_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(tokens.surfaceElevatedColor.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (tokens.isDark) Color.White else Color(0xFF0F172A)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section: Liquid Glass Theme Hero
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 24.dp,
                        glowAccent = tokens.accentColor
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "LIQUID GLASS DESIGN",
                                color = tokens.accentColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Translucent Optics Engine",
                                color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Modern glassmorphism with refraction shaders, dynamic contrast, and real-time Android system bridges.",
                                color = if (tokens.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF475569),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Section 1: Network & Connectivity
                item {
                    SettingsGroupCard(title = "CONNECTIVITY & RADIOS") {
                        SettingsRow(
                            icon = Icons.Default.Wifi,
                            iconColor = LiquidCyan,
                            title = "Wi-Fi",
                            subtitle = "Connected to 5GHz Ultra",
                            onClick = { settingsHelper.openWifiSettings() }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.Bluetooth,
                            iconColor = LiquidCyan,
                            title = "Bluetooth",
                            subtitle = "On",
                            onClick = { settingsHelper.openBluetoothSettings() }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.AirplanemodeActive,
                            iconColor = LiquidAmber,
                            title = "Airplane Mode",
                            subtitle = "System Network Panel",
                            onClick = { settingsHelper.openAirplaneModeSettings() }
                        )
                    }
                }

                // Section 2: Display, Glass & Appearance
                item {
                    SettingsGroupCard(title = "DISPLAY & LIQUID GLASS") {
                        SettingsRow(
                            icon = Icons.Default.Brightness6,
                            iconColor = LiquidCyan,
                            title = "Glass Opacity & Refraction",
                            subtitle = "${(preferences.glassOpacity * 100).toInt()}% • Dynamic Blur",
                            onClick = { currentSubscreen = SettingsSubscreen.DISPLAY_GLASS }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.Lock,
                            iconColor = LiquidPink,
                            title = "Lock Screen Customization",
                            subtitle = "${preferences.clockStyle.displayName} • Widgets",
                            onClick = { currentSubscreen = SettingsSubscreen.LOCK_SCREEN }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.GridView,
                            iconColor = LiquidViolet,
                            title = "Home Screen & App Grid",
                            subtitle = "${preferences.gridColumns} Columns • ${preferences.iconSizeDp}dp Icons",
                            onClick = { currentSubscreen = SettingsSubscreen.HOME_SCREEN }
                        )
                    }
                }

                // Section 3: Sounds, Haptics & Notifications
                item {
                    SettingsGroupCard(title = "SOUNDS & FEEDBACK") {
                        SettingsRow(
                            icon = Icons.Default.VolumeUp,
                            iconColor = LiquidPink,
                            title = "Sounds & Haptics",
                            subtitle = if (preferences.hapticFeedbackEnabled) "Liquid Haptics Enabled" else "Disabled",
                            onClick = { currentSubscreen = SettingsSubscreen.SOUNDS_HAPTICS }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.Notifications,
                            iconColor = LiquidGreen,
                            title = "Notifications & Listener",
                            subtitle = "Mirror real incoming alerts",
                            onClick = { settingsHelper.openNotificationListenerSettings() }
                        )
                    }
                }

                // Section 4: Battery & Privacy
                item {
                    SettingsGroupCard(title = "SYSTEM & PRIVACY") {
                        SettingsRow(
                            icon = Icons.Default.BatteryChargingFull,
                            iconColor = LiquidGreen,
                            title = "Battery & Health",
                            subtitle = "${batteryInfo.level}% • ${if (batteryInfo.isCharging) "Charging" else "Discharging"}",
                            onClick = { currentSubscreen = SettingsSubscreen.BATTERY }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.Security,
                            iconColor = LiquidCyan,
                            title = "Privacy & Permissions",
                            subtitle = "Zero tracking • Local storage only",
                            onClick = { currentSubscreen = SettingsSubscreen.PRIVACY }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Default.Info,
                            iconColor = Color.Gray,
                            title = "About Liquid Glass UI",
                            subtitle = "Version 1.0 (Build 27)",
                            onClick = { currentSubscreen = SettingsSubscreen.ABOUT }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        // Subscreen Dialogs
        when (currentSubscreen) {
            SettingsSubscreen.DISPLAY_GLASS -> {
                DisplayGlassDialog(
                    preferences = preferences,
                    onDismiss = { currentSubscreen = SettingsSubscreen.NONE },
                    onUpdate = onUpdatePreferences
                )
            }
            SettingsSubscreen.LOCK_SCREEN -> {
                LockScreenSettingsDialog(
                    preferences = preferences,
                    onDismiss = { currentSubscreen = SettingsSubscreen.NONE },
                    onUpdate = onUpdatePreferences
                )
            }
            SettingsSubscreen.HOME_SCREEN -> {
                HomeScreenSettingsDialog(
                    preferences = preferences,
                    onDismiss = { currentSubscreen = SettingsSubscreen.NONE },
                    onUpdate = onUpdatePreferences
                )
            }
            SettingsSubscreen.SOUNDS_HAPTICS -> {
                SoundsHapticsDialog(
                    preferences = preferences,
                    onDismiss = { currentSubscreen = SettingsSubscreen.NONE },
                    onUpdate = onUpdatePreferences
                )
            }
            SettingsSubscreen.BATTERY -> {
                BatteryDialog(
                    batteryInfo = batteryInfo,
                    onDismiss = { currentSubscreen = SettingsSubscreen.NONE }
                )
            }
            SettingsSubscreen.PRIVACY -> {
                PrivacyDialog(onDismiss = { currentSubscreen = SettingsSubscreen.NONE })
            }
            SettingsSubscreen.ABOUT -> {
                AboutDialog(onDismiss = { currentSubscreen = SettingsSubscreen.NONE })
            }
            else -> {}
        }
    }
}

@Composable
private fun SettingsGroupCard(
    title: String,
    content: @Composable () -> Unit
) {
    val tokens = LocalGlassTokens.current
    Column {
        Text(
            text = title,
            color = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
        )
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val tokens = LocalGlassTokens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconColor.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                color = if (tokens.isDark) Color.White.copy(alpha = 0.55f) else Color(0xFF64748B),
                fontSize = 11.5.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = if (tokens.isDark) Color.White.copy(alpha = 0.35f) else Color(0xFF94A3B8),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun SettingsDivider() {
    val tokens = LocalGlassTokens.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 58.dp)
            .background(tokens.borderColor.copy(alpha = 0.15f))
    )
}

@Composable
private fun DisplayGlassDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onUpdate: (UserPreferences) -> Unit
) {
    val tokens = LocalGlassTokens.current
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Glass Appearance",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Glass Opacity: ${(preferences.glassOpacity * 100).toInt()}%",
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassHorizontalSlider(
                value = preferences.glassOpacity,
                onValueChange = { onUpdate(preferences.copy(glassOpacity = it)) },
                range = 0.3f..0.95f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Blur Strength: ${preferences.blurStrength.toInt()}px",
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassHorizontalSlider(
                value = preferences.blurStrength,
                onValueChange = { onUpdate(preferences.copy(blurStrength = it)) },
                range = 5f..50f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Corner Curvature: ${preferences.cornerRadius.toInt()}dp",
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassHorizontalSlider(
                value = preferences.cornerRadius,
                onValueChange = { onUpdate(preferences.copy(cornerRadius = it)) },
                range = 12f..36f
            )

            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(
                onClick = onDismiss,
                isPrimary = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Apply & Close", color = Color.Black)
            }
        }
    }
}

@Composable
private fun LockScreenSettingsDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onUpdate: (UserPreferences) -> Unit
) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Lock Screen Clock Style",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            ClockStyle.values().forEach { style ->
                val isSelected = preferences.clockStyle == style
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) LiquidCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { onUpdate(preferences.copy(clockStyle = style)) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = style.displayName,
                        color = if (isSelected) LiquidCyan else Color.White,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(LiquidCyan)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            GlassButton(
                onClick = onDismiss,
                isPrimary = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Done", color = Color.Black)
            }
        }
    }
}

@Composable
private fun HomeScreenSettingsDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onUpdate: (UserPreferences) -> Unit
) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Home Screen Grid & Icons",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Grid Columns: ${preferences.gridColumns}", color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(3, 4, 5).forEach { cols ->
                    val isSelected = preferences.gridColumns == cols
                    GlassButton(
                        onClick = { onUpdate(preferences.copy(gridColumns = cols)) },
                        isPrimary = isSelected,
                        cornerRadius = 12.dp
                    ) {
                        Text("$cols Cols", color = if (isSelected) Color.Black else Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show App Labels", color = Color.White, fontSize = 14.sp)
                GlassToggle(
                    checked = preferences.showAppLabels,
                    onCheckedChange = { onUpdate(preferences.copy(showAppLabels = it)) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(
                onClick = onDismiss,
                isPrimary = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Done", color = Color.Black)
            }
        }
    }
}

@Composable
private fun SoundsHapticsDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onUpdate: (UserPreferences) -> Unit
) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Sounds & Haptics", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Haptic Feedback", color = Color.White, fontSize = 14.sp)
                GlassToggle(
                    checked = preferences.hapticFeedbackEnabled,
                    onCheckedChange = { onUpdate(preferences.copy(hapticFeedbackEnabled = it)) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Haptic Intensity", color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            GlassHorizontalSlider(
                value = preferences.hapticIntensity,
                onValueChange = { onUpdate(preferences.copy(hapticIntensity = it)) },
                range = 0.2f..1.0f
            )

            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(
                onClick = onDismiss,
                isPrimary = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Done", color = Color.Black)
            }
        }
    }
}

@Composable
private fun BatteryDialog(
    batteryInfo: BatteryInfo,
    onDismiss: () -> Unit
) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Battery & Power", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(14.dp))
            Text("Level: ${batteryInfo.level}%", color = Color.White, fontSize = 14.sp)
            Text("Status: ${if (batteryInfo.isCharging) "Fast Charging" else "Discharging"}", color = LiquidGreen, fontSize = 14.sp)
            Text("Temperature: ${batteryInfo.temperatureCelsius}°C", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text("Power Saver: ${if (batteryInfo.isPowerSaveMode) "Active" else "Normal"}", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(onClick = onDismiss, isPrimary = true, modifier = Modifier.align(Alignment.End)) {
                Text("Close", color = Color.Black)
            }
        }
    }
}

@Composable
private fun PrivacyDialog(onDismiss: () -> Unit) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Privacy & Security", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "• Zero Cloud Telemetry: All configuration and preferences are stored strictly in local DataStore.\n\n" +
                        "• Transparent Notification Service: NotificationListenerService is strictly used to display glass cards locally and is never recorded or transmitted.\n\n" +
                        "• Camera / Flashlight: Used exclusively for hardware torch and direct camera launch.\n\n" +
                        "• Android Policy Compliant: No hidden accessibility exploits or system lock bypass.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(onClick = onDismiss, isPrimary = true, modifier = Modifier.align(Alignment.End)) {
                Text("Got It", color = Color.Black)
            }
        }
    }
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    GlassDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Liquid Glass UI", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Version 1.0 (Build 27) • Android Edition", color = LiquidCyan, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Liquid Glass UI is an original Android experience inspired by translucent optics, responsive spring physics, and fluid surfaces. Built exclusively with Kotlin and Jetpack Compose.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(onClick = onDismiss, isPrimary = true, modifier = Modifier.align(Alignment.End)) {
                Text("Close", color = Color.Black)
            }
        }
    }
}
