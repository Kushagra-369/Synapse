package com.synapse.mobile.features.nlp

/**
 * Enhanced entity extractor that extracts:
 *
 * - app (from [AliasRepository.appAliases])
 * - contact (after call/dial triggers, with time phrases removed)
 * - message (text content for WhatsApp, SMS, etc.)
 * - website (from local aliases or domain detection)
 * - number (first integer)
 * - time (via [TimeExtractor])
 * - volume (e.g., "volume 5", "set volume to 7")
 * - brightness (e.g., "brightness 50", "set brightness to 80")
 * - percentage (e.g., "50 percent")
 * - phoneNumber (e.g., "+91 9876543210", "987-654-3210")
 * - email (e.g., "john@example.com")
 * - url (e.g., "https://example.com", "www.example.com")
 *
 * All extractions are case‑insensitive and robust against common variations.
 */
@Suppress("UNCHECKED_CAST")
object EntityExtractor {

    // Local website aliases
    private val websiteAliases = mapOf(
        "youtube" to listOf("youtube", "yt"),
        "google" to listOf("google", "gmail", "google drive"),
        "facebook" to listOf("facebook", "fb"),
        "instagram" to listOf("instagram", "ig"),
        "twitter" to listOf("twitter", "x"),
        "github" to listOf("github"),
        "linkedin" to listOf("linkedin"),
        "amazon" to listOf("amazon", "amzn"),
        "flipkart" to listOf("flipkart"),
        "netflix" to listOf("netflix"),
        "reddit" to listOf("reddit"),
        "wikipedia" to listOf("wikipedia", "wiki"),
        "spotify" to listOf("spotify"),
        "whatsapp" to listOf("whatsapp", "wa"),
        "telegram" to listOf("telegram", "tg")
    )

    // ----------------------------------------------------------------------
    // Public API
    // ----------------------------------------------------------------------

    /**
     * Extracts all known entities from the input text.
     *
     * @param text natural language input
     * @return a mutable map with entity keys (String) and values (String, Int, or TimeEntity)
     */
    fun extract(
        text: String,
        intent: IntentType
    ): MutableMap<String, Any>{
        val params = mutableMapOf<String, Any>()

        when (intent) {

            IntentType.OPEN_APP -> {
                extractApp(text)?.let {
                    params["app"] = it
                }
            }

            IntentType.CLOSE_APP -> {
                extractApp(text)?.let {
                    params["app"] = it
                }
            }

            IntentType.OPEN_WEBSITE -> {
                extractWebsite(text)?.let {
                    params["website"] = it
                }
            }

            IntentType.OPEN_YOUTUBE -> {
                extractWebsite(text)?.let {
                    params["website"] = it
                }
            }

            IntentType.OPEN_MAPS -> {
                extractMapsQuery(text)?.let {
                    params["query"] = it
                }
            }

            IntentType.MAPS_NAVIGATE -> {
                extractNavigationDestination(text)?.let {
                    params["destination"] = it
                }

                extractNavigationOrigin(text)?.let {
                    params["origin"] = it
                }
            }

            IntentType.MAPS_SEARCH -> {
                extractMapsQuery(text)?.let {
                    params["place"] = it
                }
            }

            IntentType.GET_CURRENT_LOCATION -> {
                // No parameters required.
            }

            IntentType.CALL_CONTACT -> {

                val contact = extractContact(text)

                contact?.let {
                    params["contact"] = it
                }
            }

            IntentType.SEND_WHATSAPP -> {

                val contact = extractContact(text)

                contact?.let {
                    params["contact"] = it
                }

                extractMessage(text, contact)?.let {
                    params["message"] = it
                }
            }

            IntentType.SET_ALARM -> {

                extractTime(text)?.let {
                    params["time"] = it
                }
            }

            IntentType.SET_TIMER -> {

                extractTime(text)?.let { time ->

                    params["time"] = time

                    if (time.type == TimeType.DURATION) {

                        val duration = time.duration ?: 0

                        val seconds = when (time.durationUnit?.lowercase()) {

                            "seconds",
                            "second",
                            "sec",
                            "secs" -> duration

                            "minutes",
                            "minute",
                            "min",
                            "mins" -> duration * 60

                            "hours",
                            "hour",
                            "hr",
                            "hrs" -> duration * 60 * 60

                            else -> 0
                        }

                        if (seconds > 0) {
                            params["seconds"] = seconds
                        }
                    }
                }
            }

            IntentType.CREATE_CALENDAR_EVENT -> {

                extractDate(text)?.let {
                    params["date"] = it
                }

                extractTime(text)?.let {
                    params["time"] = it
                }

                extractEventTitle(text)?.let {
                    params["title"] = it
                }
            }

            IntentType.SET_VOLUME -> {
                extractVolume(text)?.let {
                    params["volume"] = it
                }
            }

            IntentType.SET_BRIGHTNESS -> {
                extractBrightness(text)?.let {
                    params["brightness"] = it
                }
            }

            else -> {}
        }

        return params
    }

    // ----------------------------------------------------------------------
    // App extraction
    // ----------------------------------------------------------------------

    private fun extractApp(text: String): String? {

        val lower = text
            .lowercase()
            .trim()

        if (lower.isBlank()) {
            return null
        }

        // Remove command words from the BEGINNING.
        var cleaned = lower
            .replaceFirst(
                Regex(
                    """^(open|launch|start|khol|kholo|kholna|open kar|open karo)\s+"""
                ),
                ""
            )
            .trim()

        // Remove command words from the END.
        cleaned = cleaned
            .replaceFirst(
                Regex(
                    """\s+(open|launch|start|khol|kholo|kholna|open kar|open karo)$"""
                ),
                ""
            )
            .trim()

        if (cleaned.isBlank()) {
            return null
        }

        // First use known aliases.
        for ((app, aliases) in AliasRepository.appAliases) {

            if (
                aliases.any {
                    cleaned == it.lowercase()
                }
            ) {
                return app
            }
        }

        // Unknown app:
        // return the user's actual app name.
        // AppResolver will search installed launchable apps.
        return cleaned
    }
    // ----------------------------------------------------------------------
    // Contact extraction (Enhanced with Hinglish support)
    // ----------------------------------------------------------------------

    private fun extractContact(text: String): String? {

        val lower = text.lowercase().trim()

        // remove words which are NOT contact names
        val cleaned = lower
            .replace("whatsapp", "")
            .replace("voice call", "")
            .replace("video call", "")
            .replace("voice", "")
            .replace("video", "")
            .trim()

        // call vinay
        Regex("""(?:call|dial|phone)\s+([a-zA-Z]+)""")
            .find(cleaned)
            ?.groupValues?.get(1)
            ?.let { return it }

        // send hello to vinay
        Regex("""(?:send|message|msg|say)\s+.+?\s+to\s+([a-zA-Z]+)""")
            .find(cleaned)
            ?.groupValues?.get(1)
            ?.let { return it }

        // vinay ko hello bhej
        Regex("""^([a-zA-Z]+)\s+ko\b""")
            .find(cleaned)
            ?.groupValues?.get(1)
            ?.let { return it }

        // hello vinay ko bhej
        Regex(""".+\s+([a-zA-Z]+)\s+ko\s+(?:bhej|bhejo|send|msg|message)""")
            .find(cleaned)
            ?.groupValues?.get(1)
            ?.let { return it }

        return null
    }

    // ----------------------------------------------------------------------
    // Message extraction (Enhanced with Hinglish support)
    // ----------------------------------------------------------------------

    private fun extractMessage(
        text: String,
        contact: String?
    ): String? {

        val lower = text.lowercase().trim()

        // ---------------------------------------------------
        // 1. send hello to vinay
        // ---------------------------------------------------

        Regex("""(?:send|message|msg|say)\s+(.+?)\s+to\s+[a-zA-Z]+""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.trim()
            ?.let { return it }

        // ---------------------------------------------------
        // 2. vinay ko hello bhej
        // ---------------------------------------------------

        Regex("""[a-zA-Z]+\s+ko\s+(.+?)\s+(?:bhej|bhejo|send|message|msg)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.trim()
            ?.let { return it }

        // ---------------------------------------------------
        // 3. hello vinay ko bhej
        // ---------------------------------------------------

        if (contact != null) {

            val escaped = Regex.escape(contact.lowercase())

            Regex("""(.+?)\s+$escaped\s+ko\s+(?:bhej|bhejo|send|message|msg)""")
                .find(lower)
                ?.groupValues?.get(1)
                ?.trim()
                ?.let { return it }
        }

        // ---------------------------------------------------
        // 4. whatsapp vinay hello
        // ---------------------------------------------------

        Regex("""(?:whatsapp|wa)\s+[a-zA-Z]+\s+(.+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.trim()
            ?.let { return it }

        // ---------------------------------------------------
        // 5. message vinay hello
        // ---------------------------------------------------

        Regex("""(?:message|msg|text)\s+[a-zA-Z]+\s+(.+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.trim()
            ?.let { return it }

        return null
    }

    // ----------------------------------------------------------------------
    // Website extraction
    // ----------------------------------------------------------------------

    private fun extractWebsite(text: String): String? {
        val lower = text.lowercase()

        // 1. Check known aliases
        for ((site, aliases) in websiteAliases) {
            if (aliases.any { lower.contains(it.lowercase()) }) {
                return site
            }
        }

        // 2. Detect domain pattern (e.g., "youtube.com")
        val domainRegex = Regex("""\b([\w-]+)\.(com|org|net|co|in|io)\b""")
        val match = domainRegex.find(text)
        if (match != null) {
            return match.groupValues[1]
        }

        return null
    }

    // ----------------------------------------------------------------------
// Maps extraction
// ----------------------------------------------------------------------

    private fun extractMapsQuery(text: String): String? {

        var query = text.lowercase().trim()

        query = query
            .replace("open maps", "")
            .replace("google maps", "")
            .replace("maps", "")
            .replace("map", "")
            .replace("dikhao", "")
            .replace("dikhado", "")
            .replace("dikhaiye", "")
            .replace("search", "")
            .replace("find", "")
            .replace("nearest", "")
            .replace("near me", "")
            .replace("nearby", "")
            .replace("sabse paas", "")
            .replace("aas paas", "")
            .replace("pass me", "")
            .replace("dhoondo", "")
            .replace("dhundo", "")
            .trim()

        return query.takeIf { it.isNotBlank() }
    }

    private fun extractNavigationDestination(text: String): String? {

        val lower = text.lowercase().trim()

        // "Delhi to Noida"
        Regex(
            """^(.+?)\s+to\s+(.+)$"""
        )
            .find(lower)
            ?.groupValues
            ?.get(2)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // "Delhi se Noida"
        Regex(
            """^(.+?)\s+se\s+(.+?)(?:\s+(?:directions?|route|rasta|raasta))?$"""
        )
            .find(lower)
            ?.groupValues
            ?.get(2)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // "India Gate le chalo"
        Regex(
            """(.+?)\s+(?:le chalo|le jao|pahucha do|pahuncha do)$"""
        )
            .find(lower)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // "ghar ka rasta dikhao"
        Regex(
            """(.+?)\s+(?:ka|ki|ke)\s+(?:rasta|raasta|route|directions?)"""
        )
            .find(lower)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // "directions to India Gate"
        Regex(
            """(?:directions?|route|navigate)\s+(?:to|for)\s+(.+)"""
        )
            .find(lower)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        return null
    }

    private fun extractNavigationOrigin(text: String): String? {

        val lower = text.lowercase().trim()

        // "Delhi to Noida"
        Regex(
            """^(.+?)\s+to\s+(.+)$"""
        )
            .find(lower)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // "Delhi se Noida"
        Regex(
            """^(.+?)\s+se\s+(.+?)(?:\s+(?:directions?|route|rasta|raasta))?$"""
        )
            .find(lower)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        return null
    }
    // ----------------------------------------------------------------------
    // Number extraction
    // ----------------------------------------------------------------------

    private fun extractNumber(text: String): Int? {
        val regex = Regex("-?\\d+")
        return regex.find(text)?.value?.toIntOrNull()
    }

    // ----------------------------------------------------------------------
    // Time extraction
    // ----------------------------------------------------------------------

    private fun extractTime(text: String): TimeEntity? {
        val entity = TimeExtractor.extract(text)
        return if (entity.type != TimeType.NONE) entity else null
    }

    private fun extractDate(text: String): DateEntity? {
        return DateExtractor.extract(text)
    }
    // ----------------------------------------------------------------------
    // Volume and Brightness
    // ----------------------------------------------------------------------

    private fun extractVolume(text: String): Int? {
        val regex = Regex("""volume\s*(?:to\s*)?(\d+)""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun extractBrightness(text: String): Int? {
        val regex = Regex("""brightness\s*(?:to\s*)?(\d+)""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    // ----------------------------------------------------------------------
    // Percentage
    // ----------------------------------------------------------------------

    private fun extractPercentage(text: String): Int? {
        val regex = Regex("""(\d+)\s*percent""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    // ----------------------------------------------------------------------
    // Phone number
    // ----------------------------------------------------------------------

    private fun extractPhoneNumber(text: String): String? {
        val regex = Regex("""(?:\+?\d{1,3}[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}""")
        return regex.find(text)?.value?.trim()
    }

    // ----------------------------------------------------------------------
    // Email
    // ----------------------------------------------------------------------

    private fun extractEmail(text: String): String? {
        val regex = Regex("""[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""")
        return regex.find(text)?.value
    }

    private fun extractEventTitle(text: String): String? {

        var title = text.lowercase()

        title = title
            .replace("calendar me add kar do", "")
            .replace("calendar me add karo", "")
            .replace("calendar", "")
            .replace("add event", "")
            .replace("create event", "")
            .replace("meeting", "meeting")
            .replace("remind me", "")
            .replace("reminder", "")
            .replace("kal", "")
            .replace("aaj", "")
            .replace("tomorrow", "")
            .replace("today", "")

        // remove time expressions
        title = title.replace(
            Regex("""\d{1,2}(:\d{2})?\s*(am|pm|baje)?"""),
            ""
        )

        title = title.trim()

        return if (title.isBlank())
            "Event"
        else
            title.replaceFirstChar {
                it.uppercase()
            }
    }

    // ----------------------------------------------------------------------
    // URL
    // ----------------------------------------------------------------------


    private fun extractUrl(text: String): String? {
        val regex = Regex("""https?://[^\s]+|www\.[^\s]+""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.value
    }
}