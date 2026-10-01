package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.data.AppItem
import com.example.data.ClockStyle
import com.example.data.FolderItem
import com.example.data.GlassThemeMode
import com.example.data.IslandState
import com.example.data.IslandType
import com.example.data.NotificationItem
import com.example.data.UserPreferences
import com.example.data.UserPreferencesRepository
import com.example.services.LiquidNotificationListenerService
import com.example.ui.components.GlassNavItem
import com.example.ui.components.GlassNavigationBar
import com.example.ui.controlcenter.ControlCenterScreen
import com.example.ui.home.HomeScreen
import com.example.ui.lockscreen.LockScreenView
import com.example.ui.notifications.NotificationCenterScreen
import com.example.ui.onboarding.OnboardingDialog
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.LiquidBlue
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGreen
import com.example.ui.theme.LiquidPink
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.MyApplicationTheme
import com.example.utils.BatteryHelper
import com.example.utils.FlashlightController
import com.example.utils.HapticsHelper
import com.example.utils.SystemSettingsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var prefsRepo: UserPreferencesRepository
    private lateinit var batteryHelper: BatteryHelper
    private lateinit var flashlightController: FlashlightController
    private lateinit var hapticsHelper: HapticsHelper
    private lateinit var settingsHelper: SystemSettingsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prefsRepo = UserPreferencesRepository(applicationContext)
        batteryHelper = BatteryHelper(applicationContext)
        flashlightController = FlashlightController(applicationContext)
        hapticsHelper = HapticsHelper(applicationContext)
        settingsHelper = SystemSettingsHelper(applicationContext)

        setContent {
            val preferences by prefsRepo.userPreferencesFlow.collectAsState(initial = UserPreferences())
            val batteryInfo by batteryHelper.batteryInfo.collectAsState()
            val isFlashlightOn by flashlightController.isFlashlightOn.collectAsState()
            val serviceNotifications by LiquidNotificationListenerService.activeNotificationsFlow.collectAsState()
            val isListenerConnected by LiquidNotificationListenerService.isServiceConnected.collectAsState()

            val accentColor = when (preferences.accentColorIndex) {
                1 -> LiquidViolet
                2 -> LiquidBlue
                3 -> LiquidPink
                4 -> LiquidGreen
                else -> LiquidCyan
            }

            val isDarkTheme = when (preferences.themeMode) {
                GlassThemeMode.LIGHT_GLASS -> false
                else -> true
            }

            MyApplicationTheme(
                darkTheme = isDarkTheme,
                glassOpacity = preferences.glassOpacity,
                blurStrength = preferences.blurStrength,
                cornerRadius = preferences.cornerRadius,
                accentColor = accentColor
            ) {
                LiquidGlassMainApp(
                    preferences = preferences,
                    batteryInfo = batteryInfo,
                    isFlashlightOn = isFlashlightOn,
                    serviceNotifications = serviceNotifications,
                    isListenerConnected = isListenerConnected,
                    onToggleFlashlight = {
                        hapticsHelper.playClick(preferences.hapticFeedbackEnabled)
                        flashlightController.toggleFlashlight()
                    },
                    onToggleDarkMode = {
                        hapticsHelper.playClick(preferences.hapticFeedbackEnabled)
                        val newMode = if (isDarkTheme) GlassThemeMode.LIGHT_GLASS else GlassThemeMode.DARK_GLASS
                        lifecycleScope.launch { prefsRepo.updateThemeMode(newMode) }
                    },
                    onLaunchCamera = {
                        hapticsHelper.playHeavyClick(preferences.hapticFeedbackEnabled)
                        settingsHelper.openCameraApp()
                    },
                    onUpdatePreferences = { updated ->
                        lifecycleScope.launch {
                            prefsRepo.updateThemeMode(updated.themeMode)
                            prefsRepo.updateGlassOpacity(updated.glassOpacity)
                            prefsRepo.updateBlurStrength(updated.blurStrength)
                            prefsRepo.updateCornerRadius(updated.cornerRadius)
                            prefsRepo.updateClockStyle(updated.clockStyle)
                            prefsRepo.updateGridColumns(updated.gridColumns)
                            prefsRepo.updateShowAppLabels(updated.showAppLabels)
                            prefsRepo.updateHapticEnabled(updated.hapticFeedbackEnabled)
                            prefsRepo.updateHapticIntensity(updated.hapticIntensity)
                        }
                    },
                    onCompleteOnboarding = {
                        lifecycleScope.launch { prefsRepo.setCompletedOnboarding(true) }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        batteryHelper.unregister()
        flashlightController.setFlashlight(false)
    }
}

@Composable
fun LiquidGlassMainApp(
    preferences: UserPreferences,
    batteryInfo: com.example.utils.BatteryInfo,
    isFlashlightOn: Boolean,
    serviceNotifications: List<NotificationItem>,
    isListenerConnected: Boolean,
    onToggleFlashlight: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onLaunchCamera: () -> Unit,
    onUpdatePreferences: (UserPreferences) -> Unit,
    onCompleteOnboarding: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf("home") }
    var isControlCenterOpen by remember { mutableStateOf(false) }
    var isNotificationCenterOpen by remember { mutableStateOf(false) }

    // Dynamic Island State Machine
    var islandState by remember {
        mutableStateOf(
            IslandState(
                type = IslandType.MUSIC_PLAYBACK,
                title = "Solar Resonance",
                subtitle = "Liquid Studio Audio",
                progress = 0.42f
            )
        )
    }

    // Timer countdown loop when timer is active
    LaunchedEffect(islandState.type) {
        if (islandState.type == IslandType.ACTIVE_TIMER) {
            while (islandState.remainingSeconds > 0) {
                delay(1000)
                islandState = islandState.copy(remainingSeconds = islandState.remainingSeconds - 1)
            }
            if (islandState.remainingSeconds <= 0) {
                islandState = islandState.copy(type = IslandType.NOTIFICATION_ALERT, title = "Timer Finished!")
            }
        }
    }

    // Default notifications list (blends real service notifications + sample interactive cards)
    val notifications = remember {
        mutableStateListOf(
            NotificationItem(
                id = "demo_1",
                appName = "Messages",
                packageName = "com.google.android.apps.messaging",
                title = "Elena Vance",
                content = "The Liquid Glass design refraction looks incredible on Android 15!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 5,
                actionName = "Reply"
            ),
            NotificationItem(
                id = "demo_2",
                appName = "Calendar",
                packageName = "com.google.android.calendar",
                title = "Design Sync in 15 mins",
                content = "Room 4B • Translucent OS Interface Review",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 25,
                actionName = "Join Meet"
            )
        )
    }

    // Combine service notifications
    val allNotifications = remember(serviceNotifications, notifications.toList()) {
        if (isListenerConnected && serviceNotifications.isNotEmpty()) {
            serviceNotifications
        } else {
            notifications.toList()
        }
    }

    // Default curated apps merged with installed device apps
    val apps = remember {
        val builtIn = listOf(
            AppItem("phone", "Phone", "com.google.android.dialer", "Communication", 1),
            AppItem("messages", "Messages", "com.google.android.apps.messaging", "Communication", 3),
            AppItem("browser", "Browser", "com.android.chrome", "Utilities"),
            AppItem("camera", "Camera", "com.google.android.GoogleCamera", "Creativity"),
            AppItem("photos", "Photos", "com.google.android.apps.photos", "Creativity"),
            AppItem("music", "Music", "com.google.android.apps.youtube.music", "Media"),
            AppItem("maps", "Maps", "com.google.android.apps.maps", "Utilities"),
            AppItem("calculator", "Calculator", "com.google.android.calculator", "Utilities"),
            AppItem("calendar", "Calendar", "com.google.android.calendar", "Productivity"),
            AppItem("settings", "Settings", context.packageName, "System"),
            AppItem("lockscreen", "Lock Screen", context.packageName, "System")
        )

        // Query installed applications safely
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val installed = try {
            val list = pm.queryIntentActivities(mainIntent, 0)
            list.filter { it.activityInfo.packageName != context.packageName }
                .take(12)
                .map { ri ->
                    val label = ri.loadLabel(pm).toString()
                    val pkg = ri.activityInfo.packageName
                    AppItem(
                        id = pkg,
                        name = label,
                        packageName = pkg,
                        category = "Apps"
                    )
                }
        } catch (_: Exception) {
            emptyList()
        }

        (builtIn + installed).distinctBy { it.name }
    }

    val folders = remember {
        listOf(
            FolderItem(
                id = "folder_utils",
                name = "Utilities",
                apps = apps.filter { it.category == "Utilities" }
            ),
            FolderItem(
                id = "folder_creativity",
                name = "Creativity",
                apps = apps.filter { it.category == "Creativity" }
            )
        )
    }

    val navItems = listOf(
        GlassNavItem("home", "Home", Icons.Default.Home),
        GlassNavItem("lockscreen", "Lock", Icons.Default.Lock),
        GlassNavItem("controlcenter", "Controls", Icons.Default.Tune),
        GlassNavItem("notifications", "Alerts", Icons.Default.Notifications),
        GlassNavItem("settings", "Settings", Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            if (currentScreen != "lockscreen" && !isControlCenterOpen && !isNotificationCenterOpen) {
                GlassNavigationBar(
                    items = navItems,
                    currentRoute = currentScreen,
                    onItemSelected = { route ->
                        when (route) {
                            "controlcenter" -> isControlCenterOpen = true
                            "notifications" -> isNotificationCenterOpen = true
                            else -> currentScreen = route
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentScreen != "lockscreen") innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    "home" -> {
                        HomeScreen(
                            preferences = preferences,
                            apps = apps,
                            folders = folders,
                            batteryInfo = batteryInfo,
                            islandState = islandState,
                            onExpandIsland = {
                                islandState = islandState.copy(isExpanded = !islandState.isExpanded)
                            },
                            onIslandAction = { action ->
                                when (action) {
                                    "accept_call" -> {
                                        islandState = islandState.copy(
                                            type = IslandType.INCOMING_CALL,
                                            subtitle = "Connected (0:01)",
                                            isExpanded = false
                                        )
                                        Toast.makeText(context, "Call Connected", Toast.LENGTH_SHORT).show()
                                    }
                                    "decline_call" -> {
                                        islandState = islandState.copy(type = IslandType.NONE)
                                    }
                                    "toggle_play" -> {
                                        Toast.makeText(context, "Playback Toggled", Toast.LENGTH_SHORT).show()
                                    }
                                    "stop_timer" -> {
                                        islandState = islandState.copy(type = IslandType.NONE)
                                    }
                                }
                            },
                            onOpenControlCenter = { isControlCenterOpen = true },
                            onOpenNotificationCenter = { isNotificationCenterOpen = true },
                            onOpenSettings = { currentScreen = "settings" },
                            onOpenLockScreen = { currentScreen = "lockscreen" },
                            isFlashlightOn = isFlashlightOn,
                            onToggleFlashlight = onToggleFlashlight,
                            isDarkMode = preferences.themeMode != GlassThemeMode.LIGHT_GLASS,
                            onToggleDarkMode = onToggleDarkMode
                        )
                    }

                    "lockscreen" -> {
                        LockScreenView(
                            clockStyle = preferences.clockStyle,
                            isClockLarge = preferences.clockSizeLarge,
                            batteryInfo = batteryInfo,
                            notifications = allNotifications,
                            isFlashlightOn = isFlashlightOn,
                            onToggleFlashlight = onToggleFlashlight,
                            onLaunchCamera = onLaunchCamera,
                            onUnlock = { currentScreen = "home" },
                            onDismissNotification = { notif ->
                                notifications.removeAll { it.id == notif.id }
                            }
                        )
                    }

                    "settings" -> {
                        SettingsScreen(
                            preferences = preferences,
                            batteryInfo = batteryInfo,
                            onBack = { currentScreen = "home" },
                            onUpdatePreferences = onUpdatePreferences
                        )
                    }
                }
            }

            // Control Center Overlay Panel
            ControlCenterScreen(
                isOpen = isControlCenterOpen,
                onDismiss = { isControlCenterOpen = false },
                isFlashlightOn = isFlashlightOn,
                onToggleFlashlight = onToggleFlashlight,
                isDarkMode = preferences.themeMode != GlassThemeMode.LIGHT_GLASS,
                onToggleDarkMode = onToggleDarkMode,
                onOpenSettings = {
                    isControlCenterOpen = false
                    currentScreen = "settings"
                }
            )

            // Notification Center Overlay Panel
            NotificationCenterScreen(
                isOpen = isNotificationCenterOpen,
                notifications = allNotifications,
                isPermissionGranted = isListenerConnected,
                onDismiss = { isNotificationCenterOpen = false },
                onClearAll = { notifications.clear() },
                onDismissNotification = { notif -> notifications.removeAll { it.id == notif.id } },
                onAddSampleNotification = {
                    val newNotif = NotificationItem(
                        id = "test_${System.currentTimeMillis()}",
                        appName = "Liquid System",
                        packageName = context.packageName,
                        title = "Glass Refraction Live",
                        content = "Incoming liquid animation notification rendered with real-time gradient border!",
                        timestamp = System.currentTimeMillis()
                    )
                    notifications.add(0, newNotif)
                    // Trigger Dynamic Island pop
                    islandState = IslandState(
                        type = IslandType.NOTIFICATION_ALERT,
                        title = "Glass Refraction Live",
                        subtitle = "Liquid System",
                        isExpanded = false
                    )
                }
            )

            // First-launch onboarding dialog
            OnboardingDialog(
                isOpen = !preferences.hasCompletedOnboarding,
                onFinish = onCompleteOnboarding
            )
        }
    }
}
