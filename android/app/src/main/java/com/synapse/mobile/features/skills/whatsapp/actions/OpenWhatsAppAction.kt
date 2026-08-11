package com.synapse.mobile.features.skills.whatsapp.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway

class OpenWhatsAppAction(
    private val gateway: WhatsAppGateway
) : Action {

    override val name: String = "open"

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val success = gateway.open()

        return if (success) {

            CommandResult(
                success = true,
                message = "WhatsApp opened."
            )

        } else {

            CommandResult(
                success = false,
                message = "Unable to open WhatsApp."
            )

        }

    }

}