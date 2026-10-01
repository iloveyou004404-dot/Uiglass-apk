package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "liquid_glass_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val GLASS_OPACITY = floatPreferencesKey("glass_opacity")
        val BLUR_STRENGTH = floatPreferencesKey("blur_strength")
        val CORNER_RADIUS = floatPreferencesKey("corner_radius")
        val ACCENT_COLOR_INDEX = intPreferencesKey("accent_color_index")
        val GRID_COLUMNS = intPreferencesKey("grid_columns")
        val ICON_SIZE = intPreferencesKey("icon_size")
        val SHOW_APP_LABELS = booleanPreferencesKey("show_app_labels")
        val CLOCK_STYLE = stringPreferencesKey("clock_style")
        val CLOCK_SIZE_LARGE = booleanPreferencesKey("clock_size_large")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val HAPTIC_INTENSITY = floatPreferencesKey("haptic_intensity")
        val WALLPAPER_BLUR = booleanPreferencesKey("wallpaper_blur")
        val COMPLETED_ONBOARDING = booleanPreferencesKey("completed_onboarding")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val themeModeStr = prefs[Keys.THEME_MODE] ?: GlassThemeMode.DARK_GLASS.name
        val themeMode = runCatching { GlassThemeMode.valueOf(themeModeStr) }.getOrDefault(GlassThemeMode.DARK_GLASS)
        
        val clockStyleStr = prefs[Keys.CLOCK_STYLE] ?: ClockStyle.FUTURISTIC_GLASS.name
        val clockStyle = runCatching { ClockStyle.valueOf(clockStyleStr) }.getOrDefault(ClockStyle.FUTURISTIC_GLASS)

        UserPreferences(
            themeMode = themeMode,
            glassOpacity = prefs[Keys.GLASS_OPACITY] ?: 0.70f,
            blurStrength = prefs[Keys.BLUR_STRENGTH] ?: 25f,
            cornerRadius = prefs[Keys.CORNER_RADIUS] ?: 24f,
            accentColorIndex = prefs[Keys.ACCENT_COLOR_INDEX] ?: 0,
            gridColumns = prefs[Keys.GRID_COLUMNS] ?: 4,
            iconSizeDp = prefs[Keys.ICON_SIZE] ?: 56,
            showAppLabels = prefs[Keys.SHOW_APP_LABELS] ?: true,
            clockStyle = clockStyle,
            clockSizeLarge = prefs[Keys.CLOCK_SIZE_LARGE] ?: true,
            hapticFeedbackEnabled = prefs[Keys.HAPTIC_ENABLED] ?: true,
            hapticIntensity = prefs[Keys.HAPTIC_INTENSITY] ?: 1.0f,
            wallpaperBlur = prefs[Keys.WALLPAPER_BLUR] ?: true,
            hasCompletedOnboarding = prefs[Keys.COMPLETED_ONBOARDING] ?: false
        )
    }

    suspend fun updateThemeMode(mode: GlassThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun updateGlassOpacity(opacity: Float) {
        context.dataStore.edit { it[Keys.GLASS_OPACITY] = opacity }
    }

    suspend fun updateBlurStrength(blur: Float) {
        context.dataStore.edit { it[Keys.BLUR_STRENGTH] = blur }
    }

    suspend fun updateCornerRadius(radius: Float) {
        context.dataStore.edit { it[Keys.CORNER_RADIUS] = radius }
    }

    suspend fun updateAccentColorIndex(index: Int) {
        context.dataStore.edit { it[Keys.ACCENT_COLOR_INDEX] = index }
    }

    suspend fun updateGridColumns(columns: Int) {
        context.dataStore.edit { it[Keys.GRID_COLUMNS] = columns }
    }

    suspend fun updateIconSize(size: Int) {
        context.dataStore.edit { it[Keys.ICON_SIZE] = size }
    }

    suspend fun updateShowAppLabels(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_APP_LABELS] = show }
    }

    suspend fun updateClockStyle(style: ClockStyle) {
        context.dataStore.edit { it[Keys.CLOCK_STYLE] = style.name }
    }

    suspend fun updateClockSizeLarge(isLarge: Boolean) {
        context.dataStore.edit { it[Keys.CLOCK_SIZE_LARGE] = isLarge }
    }

    suspend fun updateHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.HAPTIC_ENABLED] = enabled }
    }

    suspend fun updateHapticIntensity(intensity: Float) {
        context.dataStore.edit { it[Keys.HAPTIC_INTENSITY] = intensity }
    }

    suspend fun updateWallpaperBlur(blur: Boolean) {
        context.dataStore.edit { it[Keys.WALLPAPER_BLUR] = blur }
    }

    suspend fun setCompletedOnboarding(completed: Boolean) {
        context.dataStore.edit { it[Keys.COMPLETED_ONBOARDING] = completed }
    }
}
