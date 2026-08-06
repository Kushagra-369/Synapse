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
import android.content.Context
class WhatsAppSkill(
    gateway: WhatsAppGateway,
    private val context: Context
) : Skill {

    override val name: String = "whatsapp"

    private val actions = mapOf(
        "open" to OpenWhatsAppAction(gateway),

        "send" to SendMessageAction(
            gateway,
            context
        ),

        "open_chat" to OpenChatAction(
            gateway,
            context
        ),

        "voice_call" to VoiceCallAction(
            gateway,
            context
        ),

        "video_call" to VideoCallAction(
            gateway,
            context
        )
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