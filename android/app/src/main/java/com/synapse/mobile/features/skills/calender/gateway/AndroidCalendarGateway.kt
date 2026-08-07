package com.synapse.mobile.features.skills.calendar.gateway

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract

class AndroidCalendarGateway(
    private val context: Context
) : CalendarGateway {

    override fun createEvent(
        title: String,
        startTime: Long,
        endTime: Long,
        allDay: Boolean
    ): Boolean {

        return try {

            val projection = arrayOf(
                CalendarContract.Calendars._ID
            )

            val cursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                null
            )

            if (cursor == null) {
                return false
            }

            var calendarId: Long? = null

            cursor.use {

                if (it.moveToFirst()) {

                    calendarId = it.getLong(
                        it.getColumnIndexOrThrow(
                            CalendarContract.Calendars._ID
                        )
                    )

                }

            }

            if (calendarId == null) {
                return false
            }

            val values = ContentValues().apply {

                put(
                    CalendarContract.Events.CALENDAR_ID,
                    calendarId
                )

                put(
                    CalendarContract.Events.TITLE,
                    title
                )

                put(
                    CalendarContract.Events.DTSTART,
                    startTime
                )

                put(
                    CalendarContract.Events.DTEND,
                    endTime
                )

                put(
                    CalendarContract.Events.EVENT_TIMEZONE,
                    java.util.TimeZone.getDefault().id
                )

                put(
                    CalendarContract.Events.ALL_DAY,
                    allDay
                )

                if (allDay) {

                    put(
                        CalendarContract.Events.RRULE,
                        "FREQ=YEARLY"
                    )

                }

            }

            val uri = context.contentResolver.insert(
                CalendarContract.Events.CONTENT_URI,
                values
            )

            uri != null

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}