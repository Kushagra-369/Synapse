package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class NextMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "next"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.next()

        return CommandResult(
            success,
            if (success) "Playing next media."
            else "Unable to play next media."
        )
    }
}