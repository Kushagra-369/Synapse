package com.synapse.mobile.features.skills.gallery.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.gallery.gateway.GalleryGateway

class OpenVideosAction(
    private val gateway: GalleryGateway
) : Action {

    override val name: String = "open_videos"

    override fun execute(command: Command): CommandResult {

        val success = gateway.openVideos()

        return if (success) {
            CommandResult(
                success = true,
                message = "Videos opened."
            )
        } else {
            CommandResult(
                success = false,
                message = "Failed to open Videos."
            )
        }
    }
}