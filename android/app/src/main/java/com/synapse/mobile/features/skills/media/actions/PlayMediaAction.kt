package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class PlayMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "play"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val query =
            command.parameters["query"] as? String
                ?: return CommandResult(
                    false,
                    "Media query missing."
                )

        val success = gateway.play(query)

        return CommandResult(
            success,
            if (success)
                "Playing \"$query\"."
            else
                "Unable to play \"$query\"."
        )
    }
}