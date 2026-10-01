package com.example.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.data.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LiquidNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        _isServiceConnected.value = true
        fetchActiveNotifications()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        _isServiceConnected.value = false
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let { addOrUpdateNotification(it) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn?.let { removeNotification(it) }
    }

    private fun fetchActiveNotifications() {
        try {
            val sbns = activeNotifications ?: return
            val list = sbns.mapNotNull { it.toNotificationItem() }
            _activeNotificationsFlow.value = list
        } catch (_: Exception) {}
    }

    private fun addOrUpdateNotification(sbn: StatusBarNotification) {
        val item = sbn.toNotificationItem() ?: return
        val current = _activeNotificationsFlow.value.toMutableList()
        current.removeAll { it.id == item.id }
        current.add(0, item)
        _activeNotificationsFlow.value = current
    }

    private fun removeNotification(sbn: StatusBarNotification) {
        val current = _activeNotificationsFlow.value.toMutableList()
        current.removeAll { it.id == sbn.key }
        _activeNotificationsFlow.value = current
    }

    private fun StatusBarNotification.toNotificationItem(): NotificationItem? {
        val extras = notification?.extras ?: return null
        val title = extras.getCharSequence(android.app.Notification.EXTRA_TITLE)?.toString()
        val text = extras.getCharSequence(android.app.Notification.EXTRA_TEXT)?.toString()
        if (title.isNullOrBlank() && text.isNullOrBlank()) return null

        val appName = try {
            val pm = packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(ai).toString()
        } catch (_: Exception) {
            packageName.substringAfterLast(".")
        }

        return NotificationItem(
            id = key,
            appName = appName,
            packageName = packageName,
            title = title ?: appName,
            content = text ?: "",
            timestamp = postTime,
            isPriority = (notification?.priority ?: 0) >= android.app.Notification.PRIORITY_HIGH
        )
    }

    companion object {
        private val _isServiceConnected = MutableStateFlow(false)
        val isServiceConnected: StateFlow<Boolean> = _isServiceConnected.asStateFlow()

        private val _activeNotificationsFlow = MutableStateFlow<List<NotificationItem>>(emptyList())
        val activeNotificationsFlow: StateFlow<List<NotificationItem>> = _activeNotificationsFlow.asStateFlow()
    }
}
