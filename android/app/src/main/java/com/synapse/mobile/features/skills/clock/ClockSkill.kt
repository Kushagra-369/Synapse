package com.synapse.mobile.features.skills.clock

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.clock.actions.SetAlarmAction

class ClockSkill : Skill {

    override val name = "clock"

    private val actions = mapOf(
        "set_alarm" to SetAlarmAction()
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