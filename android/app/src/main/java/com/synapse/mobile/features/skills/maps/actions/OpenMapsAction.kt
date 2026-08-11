package com.synapse.mobile.features.skills.maps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.maps.gateway.MapsGateway

class OpenMapsAction(
    private val gateway: MapsGateway
) : Action {

    override val name = "open_maps"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val query = command.parameters["query"]
            ?.toString()
            ?: return CommandResult(
                false,
                "Missing parameter: query"
            )

        val success = gateway.openMaps(query)

        return CommandResult(
            success,
            if (success)
                "Maps opened successfully."
            else
                "Failed to open maps."
        )
    }
}