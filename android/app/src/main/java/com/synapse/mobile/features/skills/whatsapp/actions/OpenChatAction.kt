package com.synapse.mobile.features.skills.whatsapp.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway

class OpenChatAction(
    private val gateway: WhatsAppGateway
) : Action {

    override val name: String = "open_chat"

    override fun execute(
        command: Command
    ): CommandResult {

        val phone =
            command.parameters["phone"] as? String
                ?: return CommandResult(
                    false,
                    "Phone number missing."
                )

        val success =
            gateway.openChat(phone)

        return if (success) {

            CommandResult(
                true,
                "Opening chat..."
            )

        } else {

            CommandResult(
                false,
                "Unable to open chat."
            )

        }

    }

}