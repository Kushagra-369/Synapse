package com.synapse.mobile.features.skills.phone

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.phone.actions.MakeCallAction
import com.synapse.mobile.features.skills.phone.gateway.PhoneGateway

import android.content.Context

class PhoneSkill(
    private val gateway: PhoneGateway,
    private val context: Context
) : Skill {

    override val name = "phone"

    private val actions = mapOf(
        "call" to MakeCallAction(
            gateway,
            context
        )
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