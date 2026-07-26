package com.synapse.mobile.features.skills.apps

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.apps.actions.LaunchAppAction
import com.synapse.mobile.features.skills.apps.gateway.AppGateway
import android.content.Context

class AppsSkill(
    gateway: AppGateway,
    context: Context
) : Skill {

    override val name = "apps"

    private val actions = mapOf(
        "open" to LaunchAppAction(gateway, context)
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