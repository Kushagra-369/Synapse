package com.synapse.mobile.features.skills.browser.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.browser.gateway.BrowserGateway

class OpenUrlAction(
    private val gateway: BrowserGateway
) : Action {

    override val name = "open_url"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val url =
            command.parameters["url"]
                ?.toString()
                ?: return CommandResult(
                    false,
                    "Missing parameter: url"
                )

        val success =
            gateway.openUrl(url)

        return CommandResult(
            success,
            if (success)
                "Browser opened successfully."
            else
                "Failed to open browser."
        )

    }

}