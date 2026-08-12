package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class StopMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "stop"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.stop()

        return CommandResult(
            success,
            if (success) "Media stopped."
            else "Unable to stop media."
        )
    }
}