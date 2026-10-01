package com.example.ui.lockscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ClockStyle
import com.example.data.NotificationItem
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassCircleControl
import com.example.ui.components.GlassNotificationCard
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.widgets.BatteryWidget
import com.example.ui.widgets.MusicWidget
import com.example.utils.BatteryInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun LockScreenView(
    clockStyle: ClockStyle,
    isClockLarge: Boolean,
    batteryInfo: BatteryInfo,
    notifications: List<NotificationItem>,
    isFlashlightOn: Boolean,
    onToggleFlashlight: () -> Unit,
    onLaunchCamera: () -> Unit,
    onUnlock: () -> Unit,
    onDismissNotification: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentDate by remember { mutableStateOf(Date()) }
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            currentDate = Date()
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("lock_screen_view")
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
            .draggable(
                state = rememberDraggableState { delta ->
                    scope.launch {
                        val newY = (offsetY.value + delta).coerceAtMost(0f)
                        offsetY.snapTo(newY)
                    }
                },
                orientation = Orientation.Vertical,
                onDragStopped = { velocity ->
                    if (offsetY.value < -250f || velocity < -500f) {
                        scope.launch {
                            offsetY.animateTo(-1500f, spring())
                            onUnlock()
                        }
                    } else {
                        scope.launch {
                            offsetY.animateTo(0f, spring())
                        }
                    }
                }
            )
    ) {
        // Wallpaper Layer
        Image(
            painter = painterResource(id = R.drawable.liquid_wallpaper),
            contentDescription = "Liquid Wallpaper",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark translucent scrim for glass contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x77000000),
                            Color(0x33000000),
                            Color(0x99000000)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Clock & Date
            LockScreenClock(
                date = currentDate,
                style = clockStyle,
                isLarge = isClockLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Info Pill (Weather + Battery)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassCard(
                    cornerRadius = 16.dp,
                    tintColor = Color(0x33000000)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFD60A),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "72° Mostly Clear",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                GlassCard(
                    cornerRadius = 16.dp,
                    tintColor = Color(0x33000000)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (batteryInfo.isCharging) "⚡ ${batteryInfo.level}%" else "${batteryInfo.level}%",
                            color = if (batteryInfo.isCharging) LiquidGreen else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Notifications List Preview
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (notifications.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Older Notifications",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    items(notifications.take(3), key = { it.id }) { notif ->
                        GlassNotificationCard(
                            notification = notif,
                            onDismiss = onDismissNotification
                        )
                    }
                }
            }

            // Bottom Shortcuts (Flashlight & Camera) & Unlock prompt
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Swipe up indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Swipe up",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Swipe up to unlock",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassCircleControl(
                        icon = Icons.Default.FlashlightOn,
                        contentDescription = "Flashlight",
                        isActive = isFlashlightOn,
                        onClick = onToggleFlashlight,
                        activeColor = Color(0xFFFFD60A),
                        testTag = "lock_flashlight_btn"
                    )

                    // Home indicator bar
                    Box(
                        modifier = Modifier
                            .width(135.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                    )

                    GlassCircleControl(
                        icon = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        isActive = false,
                        onClick = onLaunchCamera,
                        testTag = "lock_camera_btn"
                    )
                }
            }
        }
    }
}
