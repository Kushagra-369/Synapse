package com.synapse.mobile.features.skills.youtube.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.youtube.gateway.YouTubeGateway

class OpenYouTubeAction(
    private val gateway: YouTubeGateway
) : Action {

    override val name: String = "open"

    override fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.open()

        return if (success) {

            CommandResult(
                success = true,
                message = "YouTube opened."
            )

        } else {

            CommandResult(
                success = false,
                message = "Failed to open YouTube."
            )

        }

    }

}