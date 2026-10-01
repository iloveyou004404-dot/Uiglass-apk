package com.example.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppItem
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.LocalGlassTokens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIconItem(
    app: AppItem,
    iconSize: Dp,
    showLabel: Boolean,
    isEditMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onHide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = LocalGlassTokens.current

    // Jiggle animation during edit mode
    val infiniteTransition = rememberInfiniteTransition(label = "jiggle")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(140),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_rot"
    )

    val effectiveRotation = if (isEditMode) rotation else 0f
    val iconVector = getIconVectorForPackage(app.packageName, app.name)
    val iconGradient = getGradientForPackage(app.packageName, app.name)

    Column(
        modifier = modifier
            .testTag("app_item_${app.id}")
            .rotate(effectiveRotation)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            // Squircle Glass Icon Container
            val iconShape = RoundedCornerShape(17.dp)
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(iconShape)
                    .background(iconGradient, iconShape)
                    .border(1.dp, Color.White.copy(alpha = 0.35f), iconShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = app.name,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize * 0.52f)
                )
            }

            // Notification Badge
            if (app.badgeCount > 0 && !isEditMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3B30))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${app.badgeCount}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Delete / Hide badge during edit mode
            if (isEditMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC000000))
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        .combinedClickable(onClick = onHide),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Hide app",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.name,
                color = Color.White,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(iconSize + 16.dp)
            )
        }
    }
}

private fun getIconVectorForPackage(pkg: String, name: String): ImageVector {
    val lower = "$pkg $name".lowercase()
    return when {
        lower.contains("camera") -> Icons.Default.CameraAlt
        lower.contains("photo") || lower.contains("gallery") -> Icons.Default.Photo
        lower.contains("chat") || lower.contains("message") -> Icons.Default.Chat
        lower.contains("mail") || lower.contains("email") -> Icons.Default.Mail
        lower.contains("music") || lower.contains("audio") || lower.contains("spotify") -> Icons.Default.MusicNote
        lower.contains("map") || lower.contains("nav") -> Icons.Default.Map
        lower.contains("video") || lower.contains("youtube") -> Icons.Default.Videocam
        lower.contains("web") || lower.contains("browser") || lower.contains("chrome") -> Icons.Default.Language
        lower.contains("setting") -> Icons.Default.Settings
        lower.contains("folder") -> Icons.Default.Folder
        else -> Icons.Default.Apps
    }
}

private fun getGradientForPackage(pkg: String, name: String): Brush {
    val lower = "$pkg $name".lowercase()
    return when {
        lower.contains("camera") -> Brush.linearGradient(listOf(Color(0xFF708090), Color(0xFF2C3E50)))
        lower.contains("photo") || lower.contains("gallery") -> Brush.linearGradient(listOf(LiquidPink, LiquidAmber))
        lower.contains("chat") || lower.contains("message") -> Brush.linearGradient(listOf(LiquidGreen, Color(0xFF1E824C)))
        lower.contains("mail") || lower.contains("email") -> Brush.linearGradient(listOf(Color(0xFF007AFF), LiquidCyan))
        lower.contains("music") || lower.contains("spotify") -> Brush.linearGradient(listOf(LiquidPink, LiquidViolet))
        lower.contains("map") -> Brush.linearGradient(listOf(LiquidGreen, LiquidCyan))
        lower.contains("video") || lower.contains("youtube") -> Brush.linearGradient(listOf(Color(0xFFFF334B), Color(0xFF900C3F)))
        lower.contains("web") || lower.contains("chrome") -> Brush.linearGradient(listOf(LiquidCyan, LiquidViolet))
        lower.contains("setting") -> Brush.linearGradient(listOf(Color(0xFF8E8E93), Color(0xFF48484A)))
        else -> Brush.linearGradient(listOf(LiquidViolet, LiquidCyan))
    }
}
