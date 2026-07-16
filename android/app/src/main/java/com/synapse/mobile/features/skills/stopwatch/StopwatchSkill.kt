package com.synapse.mobile.features.skills.stopwatch

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.stopwatch.actions.StartStopwatchAction
import com.synapse.mobile.features.skills.stopwatch.gateway.StopwatchGateway

class StopwatchSkill(
    gateway: StopwatchGateway
) : Skill {

    override val name = "stopwatch"

    private val actions = mapOf(
        "start_stopwatch" to StartStopwatchAction(gateway)
    )

    override fun execute(command: Command): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )

    }
}