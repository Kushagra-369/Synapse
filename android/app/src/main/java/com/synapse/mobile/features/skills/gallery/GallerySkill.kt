package com.synapse.mobile.features.skills.gallery

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.gallery.actions.OpenGalleryAction
import com.synapse.mobile.features.skills.gallery.actions.OpenPhotosAction
import com.synapse.mobile.features.skills.gallery.actions.OpenVideosAction
import com.synapse.mobile.features.skills.gallery.gateway.GalleryGateway

class GallerySkill(
    gateway: GalleryGateway
) : Skill {

    override val name = "gallery"

    private val actions = mapOf(
        "open_gallery" to OpenGalleryAction(gateway),
        "open_photos" to OpenPhotosAction(gateway),
        "open_videos" to OpenVideosAction(gateway)
    )

    override fun execute(
        command: Command
    ): CommandResult {

        val action = actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                false,
                "Unknown action: ${command.action}"
            )
    }
}