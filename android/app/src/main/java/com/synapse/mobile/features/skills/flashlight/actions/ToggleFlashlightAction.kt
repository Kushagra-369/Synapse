package com.synapse.mobile.features.skills.flashlight.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.flashlight.gateway.FlashlightGateway

class ToggleFlashlightAction(
    private val gateway: FlashlightGateway
) : Action {

    override val name = "toggle"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.toggle()

        return CommandResult(
            success = success,
            message = if (success)
                "Flashlight toggled."
            else
                "Failed to toggle flashlight."
        )
    }
}