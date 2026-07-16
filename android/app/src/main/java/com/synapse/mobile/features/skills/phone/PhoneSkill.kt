package com.synapse.mobile.features.skills.phone

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.phone.actions.MakeCallAction
import com.synapse.mobile.features.skills.phone.gateway.PhoneGateway

class PhoneSkill(
    gateway: PhoneGateway
) : Skill {

    override val name = "phone"

    private val actions = mapOf(
        "dial_phone" to MakeCallAction(gateway)
    )

    override fun execute(command: Command): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown action: ${command.action}"
            )
    }
}