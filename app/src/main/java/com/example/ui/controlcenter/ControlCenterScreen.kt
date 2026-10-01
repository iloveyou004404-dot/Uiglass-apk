package com.example.ui.controlcenter

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassCircleControl
import com.example.ui.components.GlassControlTile
import com.example.ui.components.GlassVerticalSlider
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.LocalGlassTokens
import com.example.utils.SystemSettingsHelper

@Composable
fun ControlCenterScreen(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    isFlashlightOn: Boolean,
    onToggleFlashlight: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val settingsHelper = remember { SystemSettingsHelper(context) }
    val tokens = LocalGlassTokens.current

    var brightness by remember { mutableFloatStateOf(0.75f) }
    var volume by remember { mutableFloatStateOf(settingsHelper.getMediaVolume()) }

    var isWifiActive by remember { mutableStateOf(true) }
    var isBluetoothActive by remember { mutableStateOf(true) }
    var isMobileDataActive by remember { mutableStateOf(false) }
    var isAirplaneActive by remember { mutableStateOf(false) }
    var isRotationLockActive by remember { mutableStateOf(false) }
    var isDndActive by remember { mutableStateOf(false) }
    var isHotspotActive by remember { mutableStateOf(false) }
    var isLocationActive by remember { mutableStateOf(true) }
    var isBatterySaverActive by remember { mutableStateOf(false) }

    var isMediaPlaying by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .testTag("control_center_screen")
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        ) {
            // Background Wallpaper with Frosted Glass Overlay
            Image(
                painter = painterResource(id = R.drawable.liquid_wallpaper),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Blur & Dark frosted tint
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xCC090E17),
                                Color(0xE60D121F)
                            )
                        )
                    )
            )

            // Control Center Grid
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* absorb taps inside content */ }
            ) {
                // Top Header Row with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Control Center",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(tokens.surfaceElevatedColor.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Control Center",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Row 1: Connectivity Quad Card (Left) & Media Player Card (Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Connectivity 2x2 Glass Card
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp),
                        cornerRadius = 24.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                GlassCircleControl(
                                    icon = Icons.Default.AirplanemodeActive,
                                    contentDescription = "Airplane Mode",
                                    isActive = isAirplaneActive,
                                    onClick = {
                                        isAirplaneActive = !isAirplaneActive
                                        settingsHelper.openAirplaneModeSettings()
                                    },
                                    activeColor = LiquidAmber,
                                    testTag = "cc_airplane_btn"
                                )
                                GlassCircleControl(
                                    icon = Icons.Default.Wifi,
                                    contentDescription = "Wi-Fi",
                                    isActive = isWifiActive,
                                    onClick = {
                                        isWifiActive = !isWifiActive
                                        settingsHelper.openWifiSettings()
                                    },
                                    activeColor = LiquidCyan,
                                    testTag = "cc_wifi_btn"
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                GlassCircleControl(
                                    icon = Icons.Default.Bluetooth,
                                    contentDescription = "Bluetooth",
                                    isActive = isBluetoothActive,
                                    onClick = {
                                        isBluetoothActive = !isBluetoothActive
                                        settingsHelper.openBluetoothSettings()
                                    },
                                    activeColor = LiquidCyan,
                                    testTag = "cc_bluetooth_btn"
                                )
                                GlassCircleControl(
                                    icon = Icons.Default.WifiTethering,
                                    contentDescription = "Hotspot",
                                    isActive = isHotspotActive,
                                    onClick = {
                                        isHotspotActive = !isHotspotActive
                                        settingsHelper.openHotspotSettings()
                                    },
                                    activeColor = LiquidGreen,
                                    testTag = "cc_hotspot_btn"
                                )
                            }
                        }
                    }

                    // Media Player Glass Card
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp),
                        cornerRadius = 24.dp,
                        glowAccent = LiquidViolet
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(LiquidViolet.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Liquid Groove",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Ambient Echo",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { isMediaPlaying = !isMediaPlaying },
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {
                                    Icon(
                                        imageVector = if (isMediaPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play/Pause",
                                        tint = Color.Black
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Row 2: Sliders (Brightness & Volume) & 2x2 Quick Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Rotation lock & DND tiles
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GlassControlTile(
                            icon = Icons.Default.ScreenRotation,
                            label = "Rotation",
                            subLabel = if (isRotationLockActive) "Locked" else "Portrait",
                            isActive = isRotationLockActive,
                            onClick = {
                                isRotationLockActive = !isRotationLockActive
                                settingsHelper.openDisplaySettings()
                            },
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                        GlassControlTile(
                            icon = Icons.Default.DoNotDisturbOn,
                            label = "Focus",
                            subLabel = if (isDndActive) "Do Not Disturb" else "Off",
                            isActive = isDndActive,
                            activeColor = LiquidViolet,
                            onClick = {
                                isDndActive = !isDndActive
                                settingsHelper.openNotificationListenerSettings()
                            },
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }

                    // Brightness vertical slider
                    GlassVerticalSlider(
                        value = brightness,
                        onValueChange = {
                            brightness = it
                            settingsHelper.setInAppBrightness(activity, it)
                        },
                        icon = Icons.Default.BrightnessMedium,
                        label = "Brightness",
                        modifier = Modifier
                            .weight(0.5f)
                            .height(160.dp)
                    )

                    // Volume vertical slider
                    GlassVerticalSlider(
                        value = volume,
                        onValueChange = {
                            volume = it
                            settingsHelper.setMediaVolume(it)
                        },
                        icon = Icons.Default.VolumeUp,
                        label = "Volume",
                        modifier = Modifier
                            .weight(0.5f)
                            .height(160.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Row 3: Utility Control Tiles Grid (Flashlight, Dark Mode, Battery Saver, Camera, Calculator, Settings)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                    GlassCircleControl(
                        icon = Icons.Default.Calculate,
                        contentDescription = "Calculator",
                        isActive = false,
                        onClick = { settingsHelper.openCalculatorApp() }
                    )
                    GlassCircleControl(
                        icon = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        isActive = false,
                        onClick = { settingsHelper.openCameraApp() }
                    )
                    GlassCircleControl(
                        icon = Icons.Default.Settings,
                        contentDescription = "Settings",
                        isActive = false,
                        onClick = onOpenSettings
                    )
                }
            }
        }
    }
}
