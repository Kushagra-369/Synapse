package com.synapse.mobile.features.notification

import android.service.notification.StatusBarNotification

interface NotificationGateway {

    fun onNotificationPosted(
        notification: StatusBarNotification
    )

    fun onNotificationRemoved(
        notification: StatusBarNotification
    )

    fun getNotifications(): List<StatusBarNotification>

    fun clear()

}