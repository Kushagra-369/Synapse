package com.synapse.mobile.features.nlp

enum class IntentType {
    OPEN_APP,
    CLOSE_APP,
    OPEN_WEBSITE,
    OPEN_YOUTUBE,
    SEARCH_YOUTUBE,
    PLAY_YOUTUBE,

    MEDIA_SELECTION,

    PLAY_MEDIA,
    PAUSE_MEDIA,
    RESUME_MEDIA,
    STOP_MEDIA,
    NEXT_MEDIA,
    PREVIOUS_MEDIA,
    OPEN_MAPS,
    MAPS_NAVIGATE,
    MAPS_SEARCH,
    GET_CURRENT_LOCATION,
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
            isCurrentLocation(lower) -> IntentType.GET_CURRENT_LOCATION
            isMapsNavigate(lower) -> IntentType.MAPS_NAVIGATE
            isMapsSearch(lower) -> IntentType.MAPS_SEARCH
            isMaps(lower) -> IntentType.OPEN_MAPS
            isYoutubePlay(lower) -> IntentType.PLAY_YOUTUBE
            isYoutubeSearch(lower) -> IntentType.SEARCH_YOUTUBE
            isYoutube(lower) -> IntentType.OPEN_YOUTUBE
            isMediaPause(lower) -> IntentType.PAUSE_MEDIA
            isMediaResume(lower) -> IntentType.RESUME_MEDIA
            isMediaStop(lower) -> IntentType.STOP_MEDIA
            isMediaNext(lower) -> IntentType.NEXT_MEDIA
            isMediaPrevious(lower) -> IntentType.PREVIOUS_MEDIA
            isMediaSelection(lower) -> IntentType.MEDIA_SELECTION
            isMediaPlay(lower) -> IntentType.PLAY_MEDIA
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

    private fun isCurrentLocation(text: String): Boolean {

        return text.contains("current location") ||
                text.contains("my location") ||
                text.contains("meri location") ||
                text.contains("mera location") ||
                text.contains("location batao") ||
                text.contains("location bata") ||
                text.contains("location dikhao") ||
                text.contains("main kahan hu") ||
                text.contains("mai kahan hu") ||
                text.contains("mein kahan hu") ||
                text.contains("where am i") ||
                text.contains("where i am")
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

    private fun isMapsNavigate(text: String): Boolean {

        // Explicit Maps/navigation commands
        if (
            text.contains("rasta") ||
            text.contains("raasta") ||
            text.contains("route") ||
            text.contains("direction") ||
            text.contains("directions") ||
            text.contains("navigate") ||
            text.contains("navigation") ||
            text.contains("le chalo") ||
            text.contains("le jao") ||
            text.contains("pahucha do") ||
            text.contains("pahuncha do") ||
            text.contains("kaise jana") ||
            text.contains("kaise jaaye") ||
            text.contains("kaise jaye")
        ) {
            return true
        }

        // "Delhi se Noida"
        if (Regex("""\b.+\s+se\s+.+\b""").matches(text.trim())) {
            return true
        }

        // "Delhi to Noida"
        if (Regex("""\b.+\s+to\s+.+\b""").matches(text.trim())) {
            return true
        }

        return false
    }

    private fun isMapsSearch(text: String): Boolean {
        return (
                text.contains("nearest") ||
                        text.contains("near me") ||
                        text.contains("nearby") ||
                        text.contains("sabse paas") ||
                        text.contains("aas paas") ||
                        text.contains("pass me") ||
                        text.contains("dikhao") ||
                        text.contains("dhoondo") ||
                        text.contains("dhundo") ||
                        text.contains("search") ||
                        text.contains("find")
                ) && (
                containsAny(text, AliasRepository.mapsAliases) ||
                        text.contains("petrol pump") ||
                        text.contains("petrol station") ||
                        text.contains("hospital") ||
                        text.contains("restaurant") ||
                        text.contains("hotel") ||
                        text.contains("atm") ||
                        text.contains("pharmacy") ||
                        text.contains("cafe") ||
                        text.contains("coffee")
                )
    }

    private fun isYoutubePlay(text: String): Boolean {

        return (
                text.contains("play") ||
                        text.contains("chalao") ||
                        text.contains("bajao") ||
                        text.contains("chala de") ||
                        text.contains("baja do")
                ) && (
                text.contains("youtube") ||
                        text.contains("yt")
                )
    }

    private fun isYoutubeSearch(text: String): Boolean {

        return (
                text.contains("search") ||
                        text.contains("find") ||
                        text.contains("dhoondo") ||
                        text.contains("dhundo") ||
                        text.contains("search karo")
                ) && (
                text.contains("youtube") ||
                        text.contains("yt")
                )
    }

    private fun isYoutube(text: String): Boolean {
        // Specifically detect "youtube" or "yt" – this should come before generic open.
        return containsAny(text, listOf("youtube", "yt"))
    }

    private fun isMediaPlay(text: String): Boolean {
        return text.startsWith("play ") ||
                text == "play" ||
                text.startsWith("chalao ") ||
                text.startsWith("bajao ")
    }

    private fun isMediaPause(text: String): Boolean {
        return text == "pause" ||
                text == "pause media" ||
                text == "media pause"
    }

    private fun isMediaResume(text: String): Boolean {
        return text == "resume" ||
                text == "resume media" ||
                text == "continue"
    }

    private fun isMediaStop(text: String): Boolean {
        return text == "stop" ||
                text == "stop media"
    }

    private fun isMediaNext(text: String): Boolean {
        return text == "next" ||
                text == "next song" ||
                text == "next track"
    }

    private fun isMediaPrevious(text: String): Boolean {
        return text == "previous" ||
                text == "previous song" ||
                text == "previous track"
    }

    private fun isMediaSelection(text: String): Boolean {

        val lower =
            text.lowercase().trim()

        return (
                lower.matches(
                    Regex(
                        """(?:first|1st|second|2nd|third|3rd|number\s*[1-3]|option\s*[1-3]|no\.?\s*[1-3]|pehla|pehli|doosra|dusra|teesra|tisra)(?:\s+(?:one|wala|wali))?"""
                    )
                )
                )
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
}
