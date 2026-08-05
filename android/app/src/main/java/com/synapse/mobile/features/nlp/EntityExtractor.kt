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
                extractWebsite(text)?.let {
                    params["location"] = it
                }
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

            IntentType.SET_ALARM,
            IntentType.SET_TIMER -> {

                extractTime(text)?.let {
                    params["time"] = it
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
        val lower = text.lowercase()
        for ((app, aliases) in AliasRepository.appAliases) {
            if (aliases.any { lower.contains(it.lowercase()) }) {
                return app
            }
        }
        return null
    }

    // ----------------------------------------------------------------------
    // Contact extraction (Enhanced with Hinglish support)
    // ----------------------------------------------------------------------

    private fun extractContact(text: String): String? {

        val lower = text.lowercase().trim()

        // -------------------------------------------------
        // 1. Call Rahul
        // -------------------------------------------------

        Regex("""(?:call|dial|phone)\s+([a-zA-Z]+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.let { return it }

        // -------------------------------------------------
        // 2. Voice call Rahul
        // -------------------------------------------------

        Regex("""(?:voice|video)\s+call\s+([a-zA-Z]+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.let { return it }

        // -------------------------------------------------
        // 3. WhatsApp Rahul
        // -------------------------------------------------

        Regex("""(?:whatsapp|wa)\s+([a-zA-Z]+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.let { return it }

        // -------------------------------------------------
        // 4. send hello to Rahul
        // -------------------------------------------------

        Regex("""(?:send|message|msg|say)\s+.+?\s+(?:to)\s+([a-zA-Z]+)""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.let { return it }

        // -------------------------------------------------
        // 5. Rahul ko hello bhej
        // -------------------------------------------------

        Regex("""^([a-zA-Z]+)\s+ko\b""")
            .find(lower)
            ?.groupValues?.get(1)
            ?.let { return it }

        // -------------------------------------------------
        // 6. hello Rahul ko bhej
        // -------------------------------------------------

        Regex(""".+\s+([a-zA-Z]+)\s+ko\s+(?:bhej|bhejo|send|msg|message)""")
            .find(lower)
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

    // ----------------------------------------------------------------------
    // URL
    // ----------------------------------------------------------------------

    private fun extractUrl(text: String): String? {
        val regex = Regex("""https?://[^\s]+|www\.[^\s]+""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.value
    }
}