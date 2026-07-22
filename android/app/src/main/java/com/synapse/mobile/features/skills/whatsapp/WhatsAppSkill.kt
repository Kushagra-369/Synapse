package com.synapse.mobile.features.skills.whatsapp

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.whatsapp.actions.OpenChatAction
import com.synapse.mobile.features.skills.whatsapp.actions.OpenWhatsAppAction
import com.synapse.mobile.features.skills.whatsapp.actions.SendMessageAction
import com.synapse.mobile.features.skills.whatsapp.actions.VideoCallAction
import com.synapse.mobile.features.skills.whatsapp.actions.VoiceCallAction
import com.synapse.mobile.features.skills.whatsapp.gateway.WhatsAppGateway

class WhatsAppSkill(
    gateway: WhatsAppGateway
) : Skill {

    override val name: String = "whatsapp"

    private val actions: Map<String, com.synapse.mobile.core.actions.Action> = mapOf(

        "open" to OpenWhatsAppAction(gateway),

        "send_message" to SendMessageAction(gateway),

        "open_chat" to OpenChatAction(gateway),

        "voice_call" to VoiceCallAction(gateway),

        "video_call" to VideoCallAction(gateway)

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