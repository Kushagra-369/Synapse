package com.synapse.mobile.features.skills.maps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.maps.gateway.MapsGateway

class SearchPlaceAction(
    private val gateway: MapsGateway
) : Action {

    override val name = "search_place"

    override fun execute(
        command: Command
    ): CommandResult {

        val place = command.parameters["place"]
            ?.toString()
            ?: return CommandResult(
                false,
                "Missing parameter: place"
            )

        val success = gateway.searchPlace(place)

        return CommandResult(
            success,
            if (success)
                "Search opened in Maps."
            else
                "Failed to search place."
        )
    }
}