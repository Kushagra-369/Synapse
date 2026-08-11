package com.synapse.mobile.features.skills.device.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.device.gateway.DeviceGateway

class SetBluetoothAction(
    private val gateway: DeviceGateway
) : Action {

    override val name = "set_bluetooth"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val enabled =
            command.parameters["enabled"] as? Boolean
                ?: return CommandResult(
                    false,
                    "Missing enabled."
                )

        val success =
            gateway.setBluetooth(enabled)

        return if (success) {

            CommandResult(
                true,
                if (enabled)
                    "Bluetooth enabled."
                else
                    "Bluetooth disabled."
            )

        } else {

            CommandResult(
                false,
                "Unable to change Bluetooth."
            )

        }

    }

}