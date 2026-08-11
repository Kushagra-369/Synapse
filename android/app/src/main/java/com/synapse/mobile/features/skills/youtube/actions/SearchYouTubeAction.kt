package com.synapse.mobile.features.skills.youtube.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.youtube.gateway.YouTubeGateway

class SearchYouTubeAction(
    private val gateway: YouTubeGateway
) : Action {

    override val name: String = "search"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val query =
            command.parameters["query"] as? String
                ?: return CommandResult(
                    success = false,
                    message = "Query missing."
                )

        val success = gateway.search(query)

        return if (success) {

            CommandResult(
                success = true,
                message = "Searching YouTube for \"$query\"."
            )

        } else {

            CommandResult(
                success = false,
                message = "Failed to search YouTube."
            )

        }

    }

}