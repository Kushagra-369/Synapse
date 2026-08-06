package com.synapse.mobile.features.skills.calendar.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.calendar.gateway.CalendarGateway

class CreateEventAction(
    private val gateway: CalendarGateway
) : Action {

    override val name: String = "create_event"

    override fun execute(command: Command): CommandResult {

        val title =
            command.parameters["title"]?.toString()
                ?: "Event"

        val time =
            command.parameters["time"]
                    as? com.synapse.mobile.features.nlp.TimeEntity
                ?: return CommandResult(
                    false,
                    "Time missing."
                )

        val calendar = java.util.Calendar.getInstance()

        calendar.add(
            java.util.Calendar.DAY_OF_YEAR,
            time.dayOffset
        )

        time.hour?.let {
            calendar.set(
                java.util.Calendar.HOUR_OF_DAY,
                it
            )
        }

        time.minute?.let {
            calendar.set(
                java.util.Calendar.MINUTE,
                it
            )
        }

        calendar.set(
            java.util.Calendar.SECOND,
            0
        )

        val startTime = calendar.timeInMillis

        calendar.add(
            java.util.Calendar.HOUR,
            1
        )

        val endTime = calendar.timeInMillis

        val success =
            gateway.createEvent(
                title,
                startTime,
                endTime
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