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

        val title = command.parameters["title"]
            ?.toString()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: title"
            )

        val startTime = command.parameters["startTime"]
            ?.toString()
            ?.toLongOrNull()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: startTime"
            )

        val endTime = command.parameters["endTime"]
            ?.toString()
            ?.toLongOrNull()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: endTime"
            )

        val success = gateway.createEvent(
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