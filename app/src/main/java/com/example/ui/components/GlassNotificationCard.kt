package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NotificationItem
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LocalGlassTokens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GlassNotificationCard(
    notification: NotificationItem,
    onDismiss: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier,
    onActionClick: ((NotificationItem) -> Unit)? = null,
    testTag: String = "notification_card"
) {
    val tokens = LocalGlassTokens.current
    var isExpanded by remember { mutableStateOf(false) }
    val timeFormatted = remember(notification.timestamp) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(Date(notification.timestamp))
    }

    GlassCard(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring()),
        cornerRadius = 20.dp,
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // App Icon badge
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(tokens.accentColor.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = notification.appName,
                        tint = tokens.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = notification.appName.uppercase(Locale.getDefault()),
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.65f) else Color(0xFF475569),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = timeFormatted,
                    color = if (tokens.isDark) Color.White.copy(alpha = 0.45f) else Color(0xFF64748B),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { onDismiss(notification) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss notification",
                        tint = if (tokens.isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Title
            Text(
                text = notification.title,
                color = if (tokens.isDark) Color.White else Color(0xFF0F172A),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Content
            Text(
                text = notification.content,
                color = if (tokens.isDark) Color.White.copy(alpha = 0.75f) else Color(0xFF334155),
                fontSize = 13.sp,
                maxLines = if (isExpanded) 8 else 2,
                overflow = TextOverflow.Ellipsis
            )

            // Optional action button or expanded controls
            if (isExpanded && notification.actionName != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    GlassButton(
                        onClick = { onActionClick?.invoke(notification) },
                        cornerRadius = 12.dp,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = notification.actionName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
