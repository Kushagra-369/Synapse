package com.synapse.mobile.features.skills.whatsapp.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway

class SendMessageAction(
    private val gateway: WhatsAppGateway
) : Action {

    override val name: String = "send_message"

    override fun execute(
        command: Command
    ): CommandResult {

        val phone =
            command.parameters["phone"] as? String
                ?: return CommandResult(
                    false,
                    "Phone number missing."
                )

        val message =
            command.parameters["message"] as? String
                ?: return CommandResult(
                    false,
                    "Message missing."
                )

        val success =
            gateway.sendMessage(
                phone,
                message
            )

        return if (success) {

            CommandResult(
                true,
                "Opening WhatsApp chat..."
            )

        } else {

            CommandResult(
                false,
                "Failed to open WhatsApp."
            )

        }

    }

}