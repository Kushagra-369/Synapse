package com.synapse.mobile.features.skills.timer.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.timer.gateway.TimerGateway

class StartTimerAction(
    private val gateway: TimerGateway
) : Action {

    override val name: String = "start_timer"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val seconds = command.parameters["seconds"]
            ?.toString()
            ?.toIntOrNull()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: seconds"
            )

        val success = gateway.startTimer(seconds)

        return CommandResult(
            success = success,
            message = if (success)
                "Timer started successfully."
            else
                "Failed to start timer."
        )
    }
}