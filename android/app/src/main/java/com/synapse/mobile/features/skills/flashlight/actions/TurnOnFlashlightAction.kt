package com.synapse.mobile.features.skills.flashlight.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.flashlight.gateway.FlashlightGateway

class TurnOnFlashlightAction(
    private val gateway: FlashlightGateway
) : Action {

    override val name = "turn_on"

    override fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.turnOn()

        return CommandResult(
            success = success,
            message = if (success)
                "Flashlight turned on."
            else
                "Failed to turn on flashlight."
        )
    }
}