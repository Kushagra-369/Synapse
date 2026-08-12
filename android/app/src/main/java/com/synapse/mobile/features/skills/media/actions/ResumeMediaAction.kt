package com.synapse.mobile.features.skills.media.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class ResumeMediaAction(
    private val gateway: MediaGateway
) : Action {

    override val name = "resume"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.resume()

        return CommandResult(
            success,
            if (success) "Media resumed."
            else "Unable to resume media."
        )
    }
}