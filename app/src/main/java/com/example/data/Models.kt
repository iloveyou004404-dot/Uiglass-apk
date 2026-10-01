package com.example.data

enum class WidgetType {
    WEATHER,
    BATTERY,
    CALENDAR,
    MUSIC,
    QUICK_CONTROLS,
    SYSTEM_INFO
}

enum class ControlType {
    WIFI,
    BLUETOOTH,
    MOBILE_DATA,
    AIRPLANE,
    FLASHLIGHT,
    ROTATION_LOCK,
    BATTERY_SAVER,
    DARK_MODE,
    LOCATION,
    HOTSPOT,
    DND,
    CALCULATOR,
    CAMERA,
    SCREEN_RECORD,
    SETTINGS
}

enum class ClockStyle(val displayName: String) {
    FUTURISTIC_GLASS("Futuristic Glass"),
    MINIMAL_SERIF("Minimal Serif"),
    DIGITAL_BOLD("Digital Bold"),
    NEON_GLOW("Neon Glow"),
    LIQUID_CURVED("Liquid Curved")
}

enum class GlassThemeMode(val displayName: String) {
    DARK_GLASS("Dark Glass"),
    LIGHT_GLASS("Light Glass"),
    FROSTED_GLASS("Frosted Glass"),
    CLEAR_GLASS("Clear Glass"),
    TINTED_GLASS("Tinted Glass")
}

data class AppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val category: String = "Apps",
    val badgeCount: Int = 0,
    val isHidden: Boolean = false
)

data class FolderItem(
    val id: String,
    val name: String,
    val apps: List<AppItem>
)

data class WidgetItem(
    val id: String,
    val type: WidgetType,
    val title: String,
    val span: Int = 2 // 1 = small square, 2 = medium wide card
)

data class NotificationItem(
    val id: String,
    val appName: String,
    val packageName: String,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPriority: Boolean = false,
    val actionName: String? = null
)

data class ControlTileItem(
    val id: String,
    val type: ControlType,
    val label: String,
    val subLabel: String = "",
    val isActive: Boolean = false,
    val isAvailable: Boolean = true
)

enum class IslandType {
    NONE,
    INCOMING_CALL,
    MUSIC_PLAYBACK,
    ACTIVE_TIMER,
    BATTERY_CHARGING,
    BLUETOOTH_CONNECTED,
    NOTIFICATION_ALERT
}

data class IslandState(
    val type: IslandType = IslandType.NONE,
    val title: String = "",
    val subtitle: String = "",
    val progress: Float = 0f,
    val isExpanded: Boolean = false,
    val actionText: String? = null,
    val remainingSeconds: Int = 0
)

data class UserPreferences(
    val themeMode: GlassThemeMode = GlassThemeMode.DARK_GLASS,
    val glassOpacity: Float = 0.70f,
    val blurStrength: Float = 25f,
    val cornerRadius: Float = 24f,
    val accentColorIndex: Int = 0, // 0: Cyan, 1: Violet, 2: Blue, 3: Pink, 4: Green
    val gridColumns: Int = 4,
    val iconSizeDp: Int = 56,
    val showAppLabels: Boolean = true,
    val clockStyle: ClockStyle = ClockStyle.FUTURISTIC_GLASS,
    val clockSizeLarge: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val hapticIntensity: Float = 1.0f,
    val wallpaperBlur: Boolean = true,
    val hasCompletedOnboarding: Boolean = false
)
