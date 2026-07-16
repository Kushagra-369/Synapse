package com.synapse.mobile.features.skills.timer.gateway

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock

class AndroidTimerGateway(
    private val context: Context
) : TimerGateway {

    override fun startTimer(
        seconds: Int
    ): Boolean {

        return try {

            val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {

                putExtra(
                    AlarmClock.EXTRA_LENGTH,
                    seconds
                )

                putExtra(
                    AlarmClock.EXTRA_MESSAGE,
                    "Created by Synapse"
                )

                putExtra(
                    AlarmClock.EXTRA_SKIP_UI,
                    false
                )

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}