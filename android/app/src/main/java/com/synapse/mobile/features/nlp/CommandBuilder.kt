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

            IntentType.SEARCH_YOUTUBE -> {
                Command(
                    skill = "youtube",
                    action = "search",
                    parameters = parameters
                )
            }

            IntentType.PLAY_YOUTUBE -> {
                Command(
                    skill = "youtube",
                    action = "play",
                    parameters = parameters
                )
            }

            IntentType.PLAY_MEDIA -> {
                Command(
                    skill = "media",
                    action = "play",
                    parameters = parameters
                )
            }

            IntentType.PAUSE_MEDIA -> {
                Command(
                    skill = "media",
                    action = "pause",
                    parameters = parameters
                )
            }

            IntentType.RESUME_MEDIA -> {
                Command(
                    skill = "media",
                    action = "resume",
                    parameters = parameters
                )
            }

            IntentType.STOP_MEDIA -> {
                Command(
                    skill = "media",
                    action = "stop",
                    parameters = parameters
                )
            }

            IntentType.NEXT_MEDIA -> {
                Command(
                    skill = "media",
                    action = "next",
                    parameters = parameters
                )
            }

            IntentType.PREVIOUS_MEDIA -> {
                Command(
                    skill = "media",
                    action = "previous",
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


            IntentType.MAPS_NAVIGATE -> {
                Command(
                    skill = "maps",
                    action = "navigate",
                    parameters = parameters
                )
            }

            IntentType.MAPS_SEARCH -> {
                Command(
                    skill = "maps",
                    action = "search_place",
                    parameters = parameters
                )
            }

            IntentType.GET_CURRENT_LOCATION -> {
                Command(
                    skill = "maps",
                    action = "get_current_location",
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
                    action = "set_alarm",
                    parameters = parameters
                )
            }

            IntentType.SET_TIMER -> {
                Command(
                    skill = "timer",
                    action = "start_timer",
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

            IntentType.CREATE_CALENDAR_EVENT -> {
                Command(
                    skill = "calendar",
                    action = "create_event",
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