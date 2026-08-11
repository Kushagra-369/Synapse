package com.synapse.mobile.features.skills.stopwatch.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.stopwatch.gateway.StopwatchGateway

class StartStopwatchAction(
    private val gateway: StopwatchGateway
) : Action {

    override val name: String = "start_stopwatch"

    override suspend fun execute(command: Command): CommandResult {

        val success = gateway.startStopwatch()

        return CommandResult(
            success = success,
            message = if (success) {
                "Stopwatch started successfully."
            } else {
                "Failed to start stopwatch."
            }
        )
    }
}