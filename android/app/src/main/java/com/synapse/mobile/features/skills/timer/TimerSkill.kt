package com.synapse.mobile.features.skills.timer

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.timer.actions.StartTimerAction
import com.synapse.mobile.features.skills.timer.gateway.TimerGateway

class TimerSkill(
    private val gateway: TimerGateway
) : Skill {

    override val name = "timer"

    private val actions = mapOf(
        "start_timer" to StartTimerAction(gateway)
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