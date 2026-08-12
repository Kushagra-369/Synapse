package com.synapse.mobile.features.skills.media

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.actions.NextMediaAction
import com.synapse.mobile.features.skills.media.actions.PauseMediaAction
import com.synapse.mobile.features.skills.media.actions.PlayMediaAction
import com.synapse.mobile.features.skills.media.actions.PreviousMediaAction
import com.synapse.mobile.features.skills.media.actions.ResumeMediaAction
import com.synapse.mobile.features.skills.media.actions.StopMediaAction
import com.synapse.mobile.features.skills.media.gateway.MediaGateway

class MediaSkill(
    gateway: MediaGateway
) : Skill {

    override val name = "media"

    private val actions = mapOf(

        "play" to PlayMediaAction(gateway),

        "pause" to PauseMediaAction(gateway),

        "resume" to ResumeMediaAction(gateway),

        "stop" to StopMediaAction(gateway),

        "next" to NextMediaAction(gateway),

        "previous" to PreviousMediaAction(gateway)
    )

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                false,
                "Unknown media action: ${command.action}"
            )
    }
}