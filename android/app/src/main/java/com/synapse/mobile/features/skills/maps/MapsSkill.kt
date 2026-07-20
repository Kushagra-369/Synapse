package com.synapse.mobile.features.skills.maps

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.maps.actions.GetCurrentLocationAction
import com.synapse.mobile.features.skills.maps.actions.NavigateAction
import com.synapse.mobile.features.skills.maps.actions.OpenMapsAction
import com.synapse.mobile.features.skills.maps.actions.SearchPlaceAction
import com.synapse.mobile.features.skills.maps.gateway.MapsGateway

class MapsSkill(
    gateway: MapsGateway
) : Skill {

    override val name = "maps"

    private val actions = mapOf(

        "get_current_location"
                to GetCurrentLocationAction(gateway),

        "open_maps"
                to OpenMapsAction(gateway),

        "navigate"
                to NavigateAction(gateway),

        "search_place"
                to SearchPlaceAction(gateway)

    )

    override fun execute(
        command: Command
    ): CommandResult {

        val action =
            actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )

    }

}