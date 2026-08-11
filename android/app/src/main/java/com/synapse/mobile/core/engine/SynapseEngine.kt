package com.synapse.mobile.core.engine

import android.content.Context
import com.synapse.mobile.core.dispatcher.ActionDispatcher
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.core.models.BatchResult
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandBatch
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.core.validation.CommandValidator
import com.synapse.mobile.core.validation.DefaultCommandValidator
import com.synapse.mobile.features.skills.apps.AppsSkill
import com.synapse.mobile.features.skills.apps.gateway.AndroidAppGateway
import com.synapse.mobile.features.skills.browser.BrowserSkill
import com.synapse.mobile.features.skills.browser.gateway.AndroidBrowserGateway
import com.synapse.mobile.features.skills.clock.ClockSkill
import com.synapse.mobile.features.skills.clock.gateway.AndroidClockGateway
import com.synapse.mobile.features.skills.device.DeviceSkill
import com.synapse.mobile.features.skills.device.gateway.AndroidDeviceGateway
import com.synapse.mobile.features.skills.maps.MapsSkill
import com.synapse.mobile.features.skills.maps.gateway.AndroidMapsGateway
import com.synapse.mobile.features.skills.phone.PhoneSkill
import com.synapse.mobile.features.skills.phone.gateway.AndroidPhoneGateway
import com.synapse.mobile.features.skills.whatsapp.WhatsAppSkill
import com.synapse.mobile.features.skills.whatsapp.gateway.AndroidWhatsAppGateway
import com.synapse.mobile.features.skills.youtube.YouTubeSkill
import com.synapse.mobile.features.skills.youtube.gateway.AndroidYouTubeGateway

/**
 * Central execution engine for Synapse.
 */
class SynapseEngine(
    private val context: Context,
    registry: SkillRegistry = SkillRegistry(),
    private val validator: CommandValidator = DefaultCommandValidator()
) {

    private val dispatcher = ActionDispatcher(registry)

    // Track last command for debugging
    var lastCommand: Command? = null
        private set

    init {
        // Register ALL skills
        registry.register(AppsSkill(AndroidAppGateway(context), context))
        registry.register(BrowserSkill(AndroidBrowserGateway(context)))
        registry.register(
            PhoneSkill(
                AndroidPhoneGateway(context),
                context
            )
        )
        registry.register(
            WhatsAppSkill(
                AndroidWhatsAppGateway(context),
                context
            )
        )
        registry.register(ClockSkill(AndroidClockGateway(context)))
        registry.register(DeviceSkill(AndroidDeviceGateway(context)))
        registry.register(YouTubeSkill(AndroidYouTubeGateway(context)))
        registry.register(MapsSkill(AndroidMapsGateway(context)))
    }

    suspend fun execute(command: Command): CommandResult {
        lastCommand = command

        validator.validate(command)?.let {
            return it
        }

        return dispatcher.dispatch(command)
    }

    suspend fun executeBatch(batch: CommandBatch): BatchResult {
        if (batch.commands.isEmpty()) {
            return BatchResult(emptyList())
        }

        val results = batch.commands.map { execute(it) }

        return BatchResult(results)
    }
}