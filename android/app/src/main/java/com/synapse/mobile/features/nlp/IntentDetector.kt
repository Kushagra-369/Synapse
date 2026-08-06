package com.synapse.mobile.features.nlp

enum class IntentType {
    OPEN_APP,
    CLOSE_APP,
    OPEN_WEBSITE,
    OPEN_YOUTUBE,
    OPEN_MAPS,
    CALL_CONTACT,
    SEND_WHATSAPP,
    SET_ALARM,
    SET_TIMER,
    START_STOPWATCH,
    FLASHLIGHT_ON,
    FLASHLIGHT_OFF,
    SET_BRIGHTNESS,
    SET_VOLUME,
    CREATE_CALENDAR_EVENT,
    UNKNOWN,

    OPEN_WHATSAPP,
    OPEN_WHATSAPP_CHAT,
    WHATSAPP_VOICE_CALL,
    WHATSAPP_VIDEO_CALL,
}

object IntentDetector {

    fun detect(text: String): IntentType {
        val lower = text.lowercase()
        return when {
            isAlarm(lower) -> IntentType.SET_ALARM
            isTimer(lower) -> IntentType.SET_TIMER
            isStopwatch(lower) -> IntentType.START_STOPWATCH
            isWhatsappVideoCall(lower) -> IntentType.WHATSAPP_VIDEO_CALL
            isWhatsappVoiceCall(lower) -> IntentType.WHATSAPP_VOICE_CALL
            isWhatsappChat(lower) -> IntentType.OPEN_WHATSAPP_CHAT
            isWhatsappOpen(lower) -> IntentType.OPEN_WHATSAPP
            isWhatsappSend(lower) -> IntentType.SEND_WHATSAPP
            isCall(lower) -> IntentType.CALL_CONTACT
            isMaps(lower) -> IntentType.OPEN_MAPS
            isYoutube(lower) -> IntentType.OPEN_YOUTUBE
            isFlashlightOn(lower) -> IntentType.FLASHLIGHT_ON
            isFlashlightOff(lower) -> IntentType.FLASHLIGHT_OFF
            isBrightness(lower) -> IntentType.SET_BRIGHTNESS
            isVolume(lower) -> IntentType.SET_VOLUME
            isCalendar(lower) -> IntentType.CREATE_CALENDAR_EVENT
            isWebsite(lower) -> IntentType.OPEN_WEBSITE
            isOpen(lower) -> IntentType.OPEN_APP
            isClose(lower) -> IntentType.CLOSE_APP
            else -> IntentType.UNKNOWN
        }
    }

    // --- Helper: check if any alias from a list appears in the text ---

    private fun containsAny(text: String, aliases: List<String>): Boolean {
        return aliases.any { text.contains(it) }
    }

    private fun isWebsite(text: String): Boolean {
        return text.contains(".com") ||
                text.contains(".in") ||
                text.contains(".org") ||
                text.contains(".net") ||
                text.contains("website") ||
                text.contains("browser")
    }

    // --- Intent checks ---

    private fun isAlarm(text: String): Boolean {
        return containsAny(text, AliasRepository.alarmAliases)
    }

    private fun isTimer(text: String): Boolean {
        return containsAny(text, AliasRepository.timerAliases)
    }

    private fun isStopwatch(text: String): Boolean {
        return containsAny(text, AliasRepository.stopwatchAliases)
    }

    private fun isCall(text: String): Boolean {
        return containsAny(text, AliasRepository.callAliases)
    }

    private fun isWhatsappOpen(text: String): Boolean {
        return containsAny(text, AliasRepository.whatsappAliases) &&
                containsAny(text, AliasRepository.openAliases)
    }

    private fun isWhatsappSend(text: String): Boolean {

        return (
                containsAny(text, AliasRepository.whatsappAliases) ||
                        text.contains("bhej") ||
                        text.contains("bhejo") ||
                        text.contains("msg") ||
                        text.contains("message") ||
                        text.contains("send")
                ) &&
                !isWhatsappVoiceCall(text) &&
                !isWhatsappVideoCall(text) &&
                !isWhatsappChat(text) &&
                !isWhatsappOpen(text)
    }

    private fun isWhatsappChat(text: String): Boolean {
        return containsAny(text, AliasRepository.whatsappAliases) &&
                text.contains("chat")
    }

    private fun isWhatsappVoiceCall(text: String): Boolean {

        return containsAny(text, AliasRepository.whatsappAliases) &&
                (
                        text.contains("voice call") ||
                                text.contains("audio call")
                        )
    }

    private fun isWhatsappVideoCall(text: String): Boolean {

        return containsAny(text, AliasRepository.whatsappAliases) &&
                text.contains("video call")
    }
    private fun isMaps(text: String): Boolean {
        return containsAny(text, AliasRepository.mapsAliases)
    }

    private fun isYoutube(text: String): Boolean {
        // Specifically detect "youtube" or "yt" – this should come before generic open.
        return containsAny(text, listOf("youtube", "yt"))
    }

    private fun isFlashlightOn(text: String): Boolean {
        return containsAny(text, AliasRepository.flashlightAliases) &&
                containsAny(text, AliasRepository.onAliases)
    }

    private fun isFlashlightOff(text: String): Boolean {
        return containsAny(text, AliasRepository.flashlightAliases) &&
                containsAny(text, AliasRepository.offAliases)
    }

    private fun isBrightness(text: String): Boolean {
        return containsAny(text, AliasRepository.brightnessAliases)
    }

    private fun isVolume(text: String): Boolean {
        return containsAny(text, AliasRepository.volumeAliases)
    }

    private fun isOpen(text: String): Boolean {
        return containsAny(text, AliasRepository.openAliases)
    }

    private fun isClose(text: String): Boolean {
        return containsAny(text, AliasRepository.closeAliases)
    }
}
private fun isCalendar(text: String): Boolean {

    return text.contains("calendar") ||
            text.contains("event") ||
            text.contains("meeting") ||
            text.contains("appointment") ||
            text.contains("birthday") ||
            text.contains("schedule") ||
            text.contains("reminder") ||
            text.contains("remind") ||
            text.contains("tomorrow") ||
            text.contains("today") ||
            text.contains("kal") ||
            text.contains("aaj")
}