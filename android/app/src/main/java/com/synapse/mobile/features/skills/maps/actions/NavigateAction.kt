package com.synapse.mobile.features.skills.maps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.maps.gateway.MapsGateway

class NavigateAction(
    private val gateway: MapsGateway
) : Action {

    override val name = "navigate"

    override fun execute(
        command: Command
    ): CommandResult {

        val destination = command.parameters["destination"]
            ?.toString()
            ?: return CommandResult(
                false,
                "Missing parameter: destination"
            )

        val success = gateway.navigate(destination)

        return CommandResult(
            success,
            if (success)
                "Navigation started."
            else
                "Failed to start navigation."
        )
    }
}