package com.synapse.mobile.features.skills.flashlight.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.flashlight.gateway.FlashlightGateway

class TurnOffFlashlightAction(
    private val gateway: FlashlightGateway
) : Action {

    override val name = "turn_off"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.turnOff()

        return CommandResult(
            success = success,
            message = if (success)
                "Flashlight turned off."
            else
                "Failed to turn off flashlight."
        )
    }
}