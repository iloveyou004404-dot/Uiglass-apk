package com.example.ui.dynamicisland

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IslandState
import com.example.data.IslandType
import com.example.ui.components.GlassButton
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.LocalGlassTokens

@Composable
fun DynamicIslandArea(
    islandState: IslandState,
    onExpandToggle: () -> Unit,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = LocalGlassTokens.current

    // Pulsing animation for active call / recording
    val infiniteTransition = rememberInfiniteTransition(label = "island_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val shape = RoundedCornerShape(32.dp)

    Box(
        modifier = modifier
            .testTag("dynamic_island_area")
            .padding(top = 10.dp)
            .animateContentSize(animationSpec = spring(dampingRatio = 0.75f, stiffness = 450f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xEE090D14),
                            Color(0xF50D111A)
                        )
                    ),
                    shape
                )
                .border(
                    BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                tokens.highlightColor.copy(alpha = 0.40f),
                                tokens.accentColor.copy(alpha = 0.25f),
                                tokens.highlightColor.copy(alpha = 0.25f)
                            )
                        )
                    ),
                    shape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onExpandToggle
                )
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            when (islandState.type) {
                IslandType.NONE -> {
                    // Sleek idle camera & sensor pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.width(105.dp).height(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF001122))
                                .border(1.5.dp, tokens.accentColor.copy(alpha = 0.4f), CircleShape)
                        )
                    }
                }

                IslandType.INCOMING_CALL -> {
                    if (islandState.isExpanded) {
                        ExpandedCallIsland(islandState, onAction)
                    } else {
                        CompactCallIsland(islandState, pulseAlpha)
                    }
                }

                IslandType.MUSIC_PLAYBACK -> {
                    if (islandState.isExpanded) {
                        ExpandedMusicIsland(islandState, onAction)
                    } else {
                        CompactMusicIsland(islandState)
                    }
                }

                IslandType.ACTIVE_TIMER -> {
                    if (islandState.isExpanded) {
                        ExpandedTimerIsland(islandState, onAction)
                    } else {
                        CompactTimerIsland(islandState)
                    }
                }

                IslandType.BATTERY_CHARGING -> {
                    CompactChargingIsland(islandState)
                }

                IslandType.BLUETOOTH_CONNECTED -> {
                    CompactBluetoothIsland(islandState)
                }

                IslandType.NOTIFICATION_ALERT -> {
                    CompactAlertIsland(islandState)
                }
            }
        }
    }
}

@Composable
private fun CompactCallIsland(state: IslandState, pulseAlpha: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Incoming call",
            tint = LiquidGreen.copy(alpha = pulseAlpha),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = state.title.ifEmpty { "Call" },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(LiquidGreen)
        )
    }
}

@Composable
private fun ExpandedCallIsland(state: IslandState, onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(LiquidViolet.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = LiquidCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.title.ifEmpty { "Incoming Call" },
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = state.subtitle.ifEmpty { "Liquid Audio HD" },
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GlassButton(
                onClick = { onAction("decline_call") },
                cornerRadius = 24.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = "Decline", tint = LiquidPink)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Decline", color = LiquidPink)
            }
            GlassButton(
                onClick = { onAction("accept_call") },
                isPrimary = true,
                cornerRadius = 24.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = "Accept", tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Accept", color = Color.Black)
            }
        }
    }
}

@Composable
private fun CompactMusicIsland(state: IslandState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = "Music",
            tint = LiquidPink,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = state.title.ifEmpty { "Now Playing" },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(90.dp)
        )
        // Mini wave visualizer
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            listOf(14.dp, 20.dp, 10.dp, 16.dp).forEach { h ->
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(h)
                        .clip(RoundedCornerShape(1.dp))
                        .background(LiquidPink)
                )
            }
        }
    }
}

@Composable
private fun ExpandedMusicIsland(state: IslandState, onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LiquidPink.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.title.ifEmpty { "Solar Resonance" },
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = state.subtitle.ifEmpty { "Liquid Glass Audio" },
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction("prev_track") }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White)
            }
            IconButton(
                onClick = { onAction("toggle_play") },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color.Black)
            }
            IconButton(onClick = { onAction("next_track") }) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
            }
        }
    }
}

@Composable
private fun CompactTimerIsland(state: IslandState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.HourglassTop,
            contentDescription = "Timer",
            tint = LiquidAmber,
            modifier = Modifier.size(16.dp)
        )
        val mins = state.remainingSeconds / 60
        val secs = state.remainingSeconds % 60
        Text(
            text = String.format("%02d:%02d", mins, secs),
            color = LiquidAmber,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ExpandedTimerIsland(state: IslandState, onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mins = state.remainingSeconds / 60
        val secs = state.remainingSeconds % 60
        Text(
            text = String.format("%02d:%02d", mins, secs),
            color = LiquidAmber,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = state.title.ifEmpty { "Timer Active" },
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        GlassButton(
            onClick = { onAction("stop_timer") },
            cornerRadius = 16.dp
        ) {
            Text("Stop Timer", color = Color.White)
        }
    }
}

@Composable
private fun CompactChargingIsland(state: IslandState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Thunderstorm,
            contentDescription = "Charging",
            tint = LiquidGreen,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = state.title.ifEmpty { "Charging" },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = state.subtitle.ifEmpty { "85%" },
            color = LiquidGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompactBluetoothIsland(state: IslandState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Bluetooth,
            contentDescription = "Bluetooth",
            tint = LiquidCyan,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = state.title.ifEmpty { "Connected" },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CompactAlertIsland(state: IslandState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(28.dp).padding(horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(LiquidPink)
        )
        Text(
            text = state.title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(140.dp)
        )
    }
}
