package com.synapse.mobile.features.notification

import android.service.notification.StatusBarNotification

class NotificationRepository : NotificationGateway {

    private val notifications =
        mutableListOf<StatusBarNotification>()

    override fun onNotificationPosted(
        notification: StatusBarNotification
    ) {

        notifications.removeAll {
            it.key == notification.key
        }

        notifications.add(notification)
    }

    override fun onNotificationRemoved(
        notification: StatusBarNotification
    ) {

        notifications.removeAll {
            it.key == notification.key
        }

    }

    override fun getNotifications(): List<StatusBarNotification> {
        return notifications.toList()
    }

    override fun clear() {
        notifications.clear()
    }

}