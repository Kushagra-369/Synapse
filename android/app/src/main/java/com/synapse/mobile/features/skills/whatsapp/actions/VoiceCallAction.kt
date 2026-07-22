package com.synapse.mobile.features.skills.whatsapp.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway

class VoiceCallAction(
    private val gateway: WhatsAppGateway
) : Action {

    override val name: String = "voice_call"

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
            gateway.voiceCall(phone)

        return if (success) {

            CommandResult(
                true,
                "Opening WhatsApp voice call..."
            )

        } else {

            CommandResult(
                false,
                "Unable to start WhatsApp voice call."
            )

        }

    }

}