package com.synapse.mobile.features.skills.calendar.gateway

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract

class AndroidCalendarGateway(
    private val context: Context
) : CalendarGateway {

    override fun createEvent(
        title: String,
        startTime: Long,
        endTime: Long
    ): Boolean {

        return try {

            val intent = Intent(Intent.ACTION_INSERT).apply {

                data = CalendarContract.Events.CONTENT_URI

                putExtra(
                    CalendarContract.Events.TITLE,
                    title
                )

                putExtra(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    startTime
                )

                putExtra(
                    CalendarContract.EXTRA_EVENT_END_TIME,
                    endTime
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