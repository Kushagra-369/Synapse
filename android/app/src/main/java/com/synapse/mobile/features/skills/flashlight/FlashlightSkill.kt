package com.synapse.mobile.features.skills.flashlight

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.flashlight.actions.ToggleFlashlightAction
import com.synapse.mobile.features.skills.flashlight.actions.TurnOffFlashlightAction
import com.synapse.mobile.features.skills.flashlight.actions.TurnOnFlashlightAction
import com.synapse.mobile.features.skills.flashlight.gateway.FlashlightGateway

class FlashlightSkill(
    gateway: FlashlightGateway
) : Skill {

    override val name = "flashlight"

    private val actions = mapOf(

        "turn_on" to TurnOnFlashlightAction(gateway),

        "turn_off" to TurnOffFlashlightAction(gateway),

        "toggle" to ToggleFlashlightAction(gateway)

    )

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )
    }
}