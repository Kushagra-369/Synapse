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
    UNKNOWN
}

object IntentDetector {

    fun detect(text: String): IntentType {
        val lower = text.lowercase()

        // Order matters: more specific intents first.
        return when {
            isAlarm(lower) -> IntentType.SET_ALARM
            isTimer(lower) -> IntentType.SET_TIMER
            isStopwatch(lower) -> IntentType.START_STOPWATCH
            isCall(lower) -> IntentType.CALL_CONTACT
            isWhatsapp(lower) -> IntentType.SEND_WHATSAPP
            isMaps(lower) -> IntentType.OPEN_MAPS
            isYoutube(lower) -> IntentType.OPEN_YOUTUBE
            isFlashlightOn(lower) -> IntentType.FLASHLIGHT_ON
            isFlashlightOff(lower) -> IntentType.FLASHLIGHT_OFF
            isBrightness(lower) -> IntentType.SET_BRIGHTNESS
            isVolume(lower) -> IntentType.SET_VOLUME
            isOpen(lower) -> IntentType.OPEN_APP
            isClose(lower) -> IntentType.CLOSE_APP
            else -> IntentType.UNKNOWN
        }
    }

    // --- Helper: check if any alias from a list appears in the text ---

    private fun containsAny(text: String, aliases: List<String>): Boolean {
        return aliases.any { text.contains(it) }
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

    private fun isWhatsapp(text: String): Boolean {
        return containsAny(text, AliasRepository.whatsappAliases)
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