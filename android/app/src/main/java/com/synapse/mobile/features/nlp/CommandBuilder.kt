package com.synapse.mobile.features.nlp

import com.synapse.mobile.core.models.Command

object CommandBuilder {

    fun build(
        intent: IntentType,
        parameters: MutableMap<String, Any>
    ): Command {

        return when (intent) {

            IntentType.OPEN_APP -> {
                Command(
                    skill = "apps",
                    action = "launch_app",
                    parameters = parameters
                )
            }

            IntentType.CLOSE_APP -> {
                Command(
                    skill = "apps",
                    action = "close_app",
                    parameters = parameters
                )
            }

            IntentType.OPEN_WEBSITE -> {
                Command(
                    skill = "browser",
                    action = "openWebsite",
                    parameters = parameters
                )
            }

            IntentType.OPEN_YOUTUBE -> {
                Command(
                    skill = "youtube",
                    action = "open",
                    parameters = parameters
                )
            }

            IntentType.OPEN_MAPS -> {
                Command(
                    skill = "maps",
                    action = "open",
                    parameters = parameters
                )
            }

            IntentType.CALL_CONTACT -> {
                Command(
                    skill = "phone",
                    action = "call",
                    parameters = parameters
                )
            }

            IntentType.SEND_WHATSAPP -> {
                Command(
                    skill = "whatsapp",
                    action = "send",
                    parameters = parameters
                )
            }

            IntentType.OPEN_WHATSAPP -> {
                Command(
                    skill = "whatsapp",
                    action = "open",
                    parameters = parameters
                )
            }

            IntentType.OPEN_WHATSAPP_CHAT -> {
                Command(
                    skill = "whatsapp",
                    action = "open_chat",
                    parameters = parameters
                )
            }

            IntentType.WHATSAPP_VOICE_CALL -> {
                Command(
                    skill = "whatsapp",
                    action = "voice_call",
                    parameters = parameters
                )
            }

            IntentType.WHATSAPP_VIDEO_CALL -> {
                Command(
                    skill = "whatsapp",
                    action = "video_call",
                    parameters = parameters
                )
            }

            IntentType.SET_ALARM -> {
                Command(
                    skill = "clock",
                    action = "setAlarm",
                    parameters = parameters
                )
            }

            IntentType.SET_TIMER -> {
                Command(
                    skill = "timer",
                    action = "start",
                    parameters = parameters
                )
            }

            IntentType.START_STOPWATCH -> {
                Command(
                    skill = "stopwatch",
                    action = "start",
                    parameters = parameters
                )
            }

            IntentType.FLASHLIGHT_ON -> {
                Command(
                    skill = "device",
                    action = "flashlightOn",
                    parameters = parameters
                )
            }

            IntentType.FLASHLIGHT_OFF -> {
                Command(
                    skill = "device",
                    action = "flashlightOff",
                    parameters = parameters
                )
            }

            IntentType.SET_BRIGHTNESS -> {
                Command(
                    skill = "device",
                    action = "setBrightness",
                    parameters = parameters
                )
            }

            IntentType.SET_VOLUME -> {
                Command(
                    skill = "device",
                    action = "setVolume",
                    parameters = parameters
                )
            }

            IntentType.UNKNOWN -> {
                Command(
                    skill = "",
                    action = "",
                    parameters = mutableMapOf()
                )
            }
        }
    }
}