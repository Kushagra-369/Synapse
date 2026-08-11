package com.synapse.mobile.features.skills.device.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.device.gateway.DeviceGateway

class SetVolumeAction(
    private val gateway: DeviceGateway
) : Action {

    override val name = "set_volume"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val percentage =
            (command.parameters["percentage"] as? Number)
                ?.toInt()
                ?: return CommandResult(
                    false,
                    "Missing percentage."
                )

        val success =
            gateway.setVolume(percentage)

        return if (success) {

            CommandResult(
                true,
                "Volume set to $percentage%."
            )

        } else {

            CommandResult(
                false,
                "Unable to change volume."
            )

        }

    }

}