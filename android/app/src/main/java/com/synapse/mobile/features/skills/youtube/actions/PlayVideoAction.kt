package com.synapse.mobile.features.skills.youtube.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.youtube.gateway.YouTubeGateway

class PlayYouTubeAction(
    private val gateway: YouTubeGateway
) : Action {

    override val name: String = "play"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val query =
            command.parameters["query"] as? String
                ?: return CommandResult(
                    success = false,
                    message = "Video query missing."
                )

        val success = gateway.play(query)

        return if (success) {

            CommandResult(
                success = true,
                message = "Playing \"$query\" on YouTube."
            )

        } else {

            CommandResult(
                success = false,
                message = "Failed to play video."
            )

        }

    }

}