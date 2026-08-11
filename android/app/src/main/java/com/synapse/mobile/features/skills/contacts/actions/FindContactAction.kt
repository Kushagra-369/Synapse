package com.synapse.mobile.features.skills.contacts.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.contacts.gateway.ContactGateway

class FindContactAction(
    private val gateway: ContactGateway
) : Action {

    override val name: String = "find_contact"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val contactName = command.parameters["name"] as? String
            ?: return CommandResult(
                success = false,
                message = "Contact name missing."
            )

        val number = gateway.findContactNumber(contactName)

        return if (number != null) {

            CommandResult(
                success = true,
                message = number
            )

        } else {

            CommandResult(
                success = false,
                message = "Contact not found."
            )

        }

    }

}