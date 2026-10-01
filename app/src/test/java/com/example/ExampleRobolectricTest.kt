package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ClockStyle
import com.example.data.GlassThemeMode
import com.example.data.UserPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Liquid Glass", appName)
  }

  @Test
  fun `verify default user preferences`() {
    val prefs = UserPreferences()
    assertEquals(GlassThemeMode.DARK_GLASS, prefs.themeMode)
    assertEquals(ClockStyle.FUTURISTIC_GLASS, prefs.clockStyle)
    assertEquals(4, prefs.gridColumns)
    assertEquals(56, prefs.iconSizeDp)
  }
}
