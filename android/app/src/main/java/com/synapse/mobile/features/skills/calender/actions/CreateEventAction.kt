package com.synapse.mobile.features.skills.calendar.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.calendar.gateway.CalendarGateway

class CreateEventAction(
    private val gateway: CalendarGateway
) : Action {

    override val name: String = "create_event"

    override suspend fun execute(command: Command): CommandResult {

        val title =
            command.parameters["title"]?.toString()
                ?: "Event"

        val time =
            command.parameters["time"]
                    as? com.synapse.mobile.features.nlp.TimeEntity

        val date =
            command.parameters["date"]
                    as? com.synapse.mobile.features.nlp.DateEntity

        val calendar = java.util.Calendar.getInstance()

        if (time != null) {

            calendar.add(
                java.util.Calendar.DAY_OF_YEAR,
                time.dayOffset
            )

            calendar.set(
                java.util.Calendar.HOUR_OF_DAY,
                time.hour ?: 9
            )

            calendar.set(
                java.util.Calendar.MINUTE,
                time.minute ?: 0
            )

        } else {

            if (date != null) {

                calendar.set(
                    java.util.Calendar.YEAR,
                    date.year
                )

                calendar.set(
                    java.util.Calendar.MONTH,
                    date.month - 1
                )

                calendar.set(
                    java.util.Calendar.DAY_OF_MONTH,
                    date.day
                )
            }

            calendar.set(
                java.util.Calendar.HOUR_OF_DAY,
                0
            )

            calendar.set(
                java.util.Calendar.MINUTE,
                0
            )
        }

        calendar.set(
            java.util.Calendar.SECOND,
            0
        )

        val startTime = calendar.timeInMillis

        if (time != null) {

            calendar.add(
                java.util.Calendar.HOUR,
                1
            )

        } else {

            calendar.add(
                java.util.Calendar.DAY_OF_YEAR,
                1
            )

        }

        val endTime = calendar.timeInMillis

        val success =
            gateway.createEvent(
                title,
                startTime,
                endTime,
                allDay = (time == null)
            )

        return CommandResult(
            success = success,
            message = if (success)
                "Calendar event created successfully."
            else
                "Failed to create calendar event."
        )
    }
}