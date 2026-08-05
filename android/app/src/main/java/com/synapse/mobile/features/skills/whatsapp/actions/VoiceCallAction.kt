package com.synapse.mobile.features.skills.whatsapp.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway
import android.content.Context
import com.synapse.mobile.core.resolver.ContactResolver

class VoiceCallAction(
    private val gateway: WhatsAppGateway,
    private val context: Context
) : Action {

    override val name: String = "voice_call"

    override fun execute(
        command: Command
    ): CommandResult {

        val contact =
            command.parameters["contact"] as? String
                ?: return CommandResult(
                    false,
                    "Contact missing."
                )

        val phone =
            ContactResolver(context)
                .getPhoneNumber(contact)
                ?: return CommandResult(
                    false,
                    "Contact not found."
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