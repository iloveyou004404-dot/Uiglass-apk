package com.example.ui.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppItem
import com.example.data.FolderItem
import com.example.data.IslandState
import com.example.data.IslandType
import com.example.data.UserPreferences
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSearchBar
import com.example.ui.dynamicisland.DynamicIslandArea
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LocalGlassTokens
import com.example.ui.widgets.BatteryWidget
import com.example.ui.widgets.CalendarWidget
import com.example.ui.widgets.ClockWidget
import com.example.ui.widgets.MusicWidget
import com.example.ui.widgets.NotesWidget
import com.example.ui.widgets.QuickControlsWidget
import com.example.ui.widgets.SystemInfoWidget
import com.example.ui.widgets.WeatherWidget
import com.example.utils.BatteryInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    preferences: UserPreferences,
    apps: List<AppItem>,
    folders: List<FolderItem>,
    batteryInfo: BatteryInfo,
    islandState: IslandState,
    onExpandIsland: () -> Unit,
    onIslandAction: (String) -> Unit,
    onOpenControlCenter: () -> Unit,
    onOpenNotificationCenter: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenLockScreen: () -> Unit,
    isFlashlightOn: Boolean,
    onToggleFlashlight: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tokens = LocalGlassTokens.current
    val scope = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFolder by remember { mutableStateOf<FolderItem?>(null) }

    val pagerState = rememberPagerState(pageCount = { 3 })

    fun launchApp(app: AppItem) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            when (app.id) {
                "settings" -> onOpenSettings()
                "lockscreen" -> onOpenLockScreen()
                else -> Toast.makeText(context, "Opening ${app.name}...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Wallpaper Layer
        Image(
            painter = painterResource(id = R.drawable.liquid_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Frosted Glass Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x33000000),
                            Color(0x1A000000),
                            Color(0x55000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dynamic Island Area at the top
            DynamicIslandArea(
                islandState = islandState,
                onExpandToggle = onExpandIsland,
                onAction = onIslandAction
            )

            // Top Status & Quick Access Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Control Center Access Pill
                GlassCard(
                    cornerRadius = 18.dp,
                    tintColor = Color(0x33000000),
                    onClick = onOpenControlCenter
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Control Center",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Controls", color = Color.White, fontSize = 11.5.sp)
                    }
                }

                // Edit Mode Indicator or Notification Pill
                if (isEditMode) {
                    GlassButton(
                        onClick = { isEditMode = false },
                        isPrimary = true,
                        cornerRadius = 16.dp,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Done", tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Done", color = Color.Black, fontSize = 12.sp)
                    }
                } else {
                    GlassCard(
                        cornerRadius = 18.dp,
                        tintColor = Color(0x33000000),
                        onClick = onOpenNotificationCenter
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Center", color = Color.White, fontSize = 11.5.sp)
                        }
                    }
                }
            }

            // Main Content Pager (Page 0: Widgets Hub, Page 1: Main App Grid, Page 2: App Library)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> {
                        // Page 0: Liquid Glass Widgets Dashboard
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                WeatherWidget(modifier = Modifier.weight(1f))
                                BatteryWidget(
                                    level = batteryInfo.level,
                                    isCharging = batteryInfo.isCharging,
                                    isPowerSave = batteryInfo.isPowerSaveMode,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CalendarWidget(modifier = Modifier.weight(1f))
                                ClockWidget(modifier = Modifier.weight(1f))
                            }
                            MusicWidget(modifier = Modifier.fillMaxWidth())
                        }
                    }

                    1 -> {
                        // Page 1: Main Home Screen App Grid
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp)
                        ) {
                            // Top Quick Controls Widget
                            QuickControlsWidget(
                                isWifiOn = true,
                                isBluetoothOn = true,
                                isFlashlightOn = isFlashlightOn,
                                isDarkMode = isDarkMode,
                                onToggleWifi = {},
                                onToggleBluetooth = {},
                                onToggleFlashlight = onToggleFlashlight,
                                onToggleDarkMode = onToggleDarkMode
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // App Icons Grid
                            val visibleApps = apps.filter { !it.isHidden }
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(preferences.gridColumns),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(visibleApps, key = { it.id }) { app ->
                                    AppIconItem(
                                        app = app,
                                        iconSize = preferences.iconSizeDp.dp,
                                        showLabel = preferences.showAppLabels,
                                        isEditMode = isEditMode,
                                        onClick = { launchApp(app) },
                                        onLongClick = { isEditMode = true },
                                        onHide = { /* hide action */ }
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        // Page 2: App Library
                        AppLibraryView(
                            apps = apps,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            onAppClick = { launchApp(it) }
                        )
                    }
                }
            }

            // Page Indicator Dots
            Row(
                modifier = Modifier
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(3) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.35f)
                            )
                            .clickable {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            }
                    )
                }
            }

            // Bottom Dock Glass Card (4 Pinned Apps)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 32.dp,
                    tintColor = Color(0x38000000)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val dockApps = apps.take(4)
                        dockApps.forEach { app ->
                            AppIconItem(
                                app = app,
                                iconSize = 54.dp,
                                showLabel = false,
                                isEditMode = isEditMode,
                                onClick = { launchApp(app) },
                                onLongClick = { isEditMode = true },
                                onHide = {}
                            )
                        }
                    }
                }
            }
        }

        // Folder Dialog if open
        AppFolderDialog(
            folder = selectedFolder,
            onDismiss = { selectedFolder = null },
            onAppClick = { launchApp(it) }
        )
    }
}
