package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class PauseMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "pause"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.pause()

        return CommandResult(
            success,
            if (success) "Media paused."
            else "Unable to pause media."
        )
    }
}