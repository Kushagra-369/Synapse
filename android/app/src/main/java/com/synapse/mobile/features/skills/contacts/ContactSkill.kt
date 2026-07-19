package com.synapse.mobile.features.skills.contacts

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.contacts.actions.FindContactAction
import com.synapse.mobile.features.skills.contacts.gateway.ContactGateway

class ContactSkill(
    gateway: ContactGateway
) : Skill {

    override val name = "contacts"

    private val actions = mapOf(
        "find_contact" to FindContactAction(gateway)
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