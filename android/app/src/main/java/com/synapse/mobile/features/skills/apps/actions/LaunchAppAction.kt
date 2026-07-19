package com.synapse.mobile.features.skills.apps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.apps.gateway.AppGateway

class LaunchAppAction(
    private val gateway: AppGateway
) : Action {

    override val name: String = "launch_app"

    override fun execute(
        command: Command
    ): CommandResult {

        val packageName = command.parameters["package"]
            ?.toString()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: package"
            )

        val success = gateway.launchApp(packageName)

        return CommandResult(
            success = success,
            message = if (success)
                "App launched successfully."
            else
                "Unable to launch app."
        )

    }

}