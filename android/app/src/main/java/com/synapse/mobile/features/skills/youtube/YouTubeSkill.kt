package com.synapse.mobile.features.skills.youtube

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.youtube.actions.OpenYouTubeAction
import com.synapse.mobile.features.skills.youtube.actions.PlayYouTubeAction
import com.synapse.mobile.features.skills.youtube.actions.SearchYouTubeAction
import com.synapse.mobile.features.skills.youtube.gateway.YouTubeGateway

class YouTubeSkill(
    gateway: YouTubeGateway
) : Skill {

    override val name = "youtube"

    private val actions = mapOf(

        "open" to OpenYouTubeAction(gateway),

        "search" to SearchYouTubeAction(gateway),

        "play" to PlayYouTubeAction(gateway)

    )

    override fun execute(
        command: Command
    ): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                false,
                "Unknown action: ${command.action}"
            )

    }

}