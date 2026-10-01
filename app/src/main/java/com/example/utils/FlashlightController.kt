package com.example.utils

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlashlightController(private val context: Context) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    private var cameraId: String? = null

    private val _isFlashlightOn = MutableStateFlow(false)
    val isFlashlightOn: StateFlow<Boolean> = _isFlashlightOn.asStateFlow()

    init {
        try {
            val cameraIds = cameraManager?.cameraIdList ?: emptyArray()
            for (id in cameraIds) {
                val characteristics = cameraManager?.getCameraCharacteristics(id)
                val hasFlash = characteristics?.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                val facing = characteristics?.get(CameraCharacteristics.LENS_FACING)
                if (hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id
                    break
                }
            }
            if (cameraId == null && cameraIds.isNotEmpty()) {
                cameraId = cameraIds.firstOrNull()
            }
        } catch (_: Exception) {}
    }

    fun toggleFlashlight(): Boolean {
        val target = !_isFlashlightOn.value
        return setFlashlight(target)
    }

    fun setFlashlight(enabled: Boolean): Boolean {
        val id = cameraId ?: return false
        return try {
            cameraManager?.setTorchMode(id, enabled)
            _isFlashlightOn.value = enabled
            true
        } catch (_: Exception) {
            _isFlashlightOn.value = false
            false
        }
    }
}
