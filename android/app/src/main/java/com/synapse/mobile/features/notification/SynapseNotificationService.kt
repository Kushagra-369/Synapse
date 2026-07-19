package com.synapse.mobile.features.notification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class SynapseNotificationService : NotificationListenerService() {

    companion object {
        val repository = NotificationRepository()
    }

    override fun onNotificationPosted(
        sbn: StatusBarNotification
    ) {
        repository.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification
    ) {
        repository.onNotificationRemoved(sbn)
    }

}