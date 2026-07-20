package com.synapse.mobile.features.skills.gallery.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.gallery.gateway.GalleryGateway

class OpenGalleryAction(
    private val gateway: GalleryGateway
) : Action {

    override val name: String = "open_gallery"

    override fun execute(command: Command): CommandResult {

        val success = gateway.openGallery()

        return if (success) {
            CommandResult(
                success = true,
                message = "Gallery opened."
            )
        } else {
            CommandResult(
                success = false,
                message = "Failed to open Gallery."
            )
        }
    }
}