package com.synapse.mobile.features.skills.device.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.device.gateway.DeviceGateway

class GetBatteryAction(
    private val gateway: DeviceGateway
) : Action {

    override val name = "get_battery"

    override fun execute(
        command: Command
    ): CommandResult {

        val level =
            gateway.getBatteryLevel()

        return CommandResult(
            true,
            "Battery level is $level%."
        )

    }

}