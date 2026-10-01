package com.example

import android.app.Application

class LiquidGlassApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: LiquidGlassApp
            private set
    }
}
