package com.synapse.mobile.features.skills.apps

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.apps.actions.LaunchAppAction
import com.synapse.mobile.features.skills.apps.gateway.AppGateway

class AppsSkill(
    gateway: AppGateway
) : Skill {

    override val name = "apps"

    private val actions = mapOf(
        "launch_app" to LaunchAppAction(gateway)
    )

    override fun execute(
        command: Command
    ): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )

    }

}