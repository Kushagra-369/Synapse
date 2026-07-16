package com.synapse.mobile.features.skills.clock.gateway

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock

class AndroidClockGateway(
    private val context: Context
) : ClockGateway {

    override fun setAlarm(
        hour: Int,
        minute: Int
    ): Boolean {

        android.util.Log.e("Synapse", "Gateway called")

        return try {

            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, hour)
                putExtra(AlarmClock.EXTRA_MINUTES, minute)
                putExtra(AlarmClock.EXTRA_MESSAGE, "Created by Synapse")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            android.util.Log.e("Synapse", "Before startActivity")

            context.startActivity(intent)

            android.util.Log.e("Synapse", "After startActivity")

            true

        } catch (e: Exception) {

            android.util.Log.e("Synapse", "Gateway exception", e)

            false
        }
    }

}