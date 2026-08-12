package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class PreviousMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "previous"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.previous()

        return CommandResult(
            success,
            if (success) "Playing previous media."
            else "Unable to play previous media."
        )
    }
}