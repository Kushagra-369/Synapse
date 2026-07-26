package com.synapse.mobile.features.skills.browser

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.browser.actions.OpenUrlAction
import com.synapse.mobile.features.skills.browser.gateway.BrowserGateway

class BrowserSkill(
    gateway: BrowserGateway
) : Skill {

    override val name = "browser"

    private val actions = mapOf(
        "openWebsite" to OpenUrlAction(gateway)
    )

    override fun execute(
        command: Command
    ): CommandResult {

        val action =
            actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                false,
                "Unknown action: ${command.action}"
            )

    }

}