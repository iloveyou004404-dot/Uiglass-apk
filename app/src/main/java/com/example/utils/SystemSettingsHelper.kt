package com.example.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast

class SystemSettingsHelper(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    fun openWifiSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            launchIntent(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
        } else {
            launchIntent(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    }

    fun openBluetoothSettings() {
        launchIntent(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
    }

    fun openMobileDataSettings() {
        launchIntent(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
    }

    fun openAirplaneModeSettings() {
        launchIntent(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
    }

    fun openHotspotSettings() {
        val intent = Intent().apply {
            action = "android.settings.TETHER_SETTINGS"
        }
        if (!launchIntent(intent)) {
            launchIntent(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

    fun openNotificationListenerSettings() {
        launchIntent(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    fun openDisplaySettings() {
        launchIntent(Intent(Settings.ACTION_DISPLAY_SETTINGS))
    }

    fun openSoundSettings() {
        launchIntent(Intent(Settings.ACTION_SOUND_SETTINGS))
    }

    fun openBatterySaverSettings() {
        launchIntent(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS))
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        launchIntent(intent)
    }

    fun openCameraApp() {
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (!launchIntent(intent)) {
            val fallback = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            launchIntent(fallback)
        }
    }

    fun openCalculatorApp() {
        val calculatorPackages = listOf(
            "com.google.android.calculator",
            "com.android.calculator2",
            "com.sec.android.app.popupcalculator",
            "com.miui.calculator"
        )
        for (pkg in calculatorPackages) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                return
            }
        }
        val genericIntent = Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_APP_CALCULATOR)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (!launchIntent(genericIntent)) {
            Toast.makeText(context, "Calculator app not found on device", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMediaVolume(): Float {
        val current = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 5
        val max = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
        return (current.toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)
    }

    fun setMediaVolume(volumeRatio: Float) {
        val max = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
        val target = (volumeRatio * max).toInt().coerceIn(0, max)
        try {
            audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, target, 0)
        } catch (_: Exception) {}
    }

    fun setInAppBrightness(activity: Activity?, brightnessRatio: Float) {
        activity?.window?.let { window ->
            val layoutParams = window.attributes
            layoutParams.screenBrightness = brightnessRatio.coerceIn(0.01f, 1.0f)
            window.attributes = layoutParams
        }
    }

    private fun launchIntent(intent: Intent): Boolean {
        return try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}
