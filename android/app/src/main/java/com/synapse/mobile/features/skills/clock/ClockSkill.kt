package com.synapse.mobile.features.skills.clock

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class ClockSkill : Skill {

    override val name = "clock"

    override fun execute(command: Command): CommandResult {

        return CommandResult(
            success = true,
            message = "Clock Skill executed successfully."
        )
    }
}