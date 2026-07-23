package com.synapse.mobile.features.skills.device.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class SetHotspotAction : Action {

    override val name = "set_hotspot"

    override fun execute(command: Command): CommandResult {

        return CommandResult(
            false,
            "Hotspot control is not implemented yet."
        )

    }

}