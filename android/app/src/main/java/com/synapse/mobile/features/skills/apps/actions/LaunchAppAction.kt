package com.synapse.mobile.features.skills.apps.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.apps.gateway.AppGateway
import android.content.Context
import com.synapse.mobile.core.resolver.AppResolver
class LaunchAppAction(
    private val gateway: AppGateway,
    private val context: Context
) : Action {

    override val name: String = "launch_app"

    override fun execute(
        command: Command
    ): CommandResult {

        val appName = command.parameters["app"]
            ?.toString()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: app"
            )

        val packageName = AppResolver(context)
            .getPackageName(appName)
            ?: return CommandResult(
                success = false,
                message = "App '$appName' not found."
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