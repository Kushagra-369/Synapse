package com.synapse.mobile.features.skills.clock.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class SetAlarmAction : Action {

    override val name: String = "set_alarm"

    override fun execute(command: Command): CommandResult {

        return CommandResult(
            success = true,
            message = "SetAlarmAction executed successfully."
        )

    }
}