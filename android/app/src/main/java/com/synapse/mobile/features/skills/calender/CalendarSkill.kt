package com.synapse.mobile.features.skills.calendar

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.calendar.actions.CreateEventAction
import com.synapse.mobile.features.skills.calendar.gateway.CalendarGateway

class CalendarSkill(
    gateway: CalendarGateway
) : Skill {

    override val name = "calendar"

    private val actions = mapOf(
        "create_event" to CreateEventAction(gateway)
    )

    override suspend fun execute(command: Command): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )
    }
}