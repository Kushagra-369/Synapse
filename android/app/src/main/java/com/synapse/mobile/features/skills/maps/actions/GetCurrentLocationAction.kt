package com.synapse.mobile.features.skills.maps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.maps.gateway.MapsGateway

class GetCurrentLocationAction(
    private val gateway: MapsGateway
) : Action {

    override val name = "get_current_location"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val location = gateway.getCurrentLocation()

        val failed =
            location == null ||
                    location == "Unable to get current location." ||
                    location == "Unable to fetch current location." ||
                    location == "Location permission not granted."

        return CommandResult(
            success = !failed,
            message = location ?: "Unable to get current location."
        )
    }
}