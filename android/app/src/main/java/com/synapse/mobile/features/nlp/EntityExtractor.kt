package com.synapse.mobile.features.nlp

/**
 * Enhanced entity extractor that extracts:
 *
 * - app (from [AliasRepository.appAliases])
 * - contact (after call/dial triggers, with time phrases removed)
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
 *
 * Note: If you want to centralise website aliases, move the [websiteAliases] map
 * to [AliasRepository] and replace the local usage with it.
 */
@Suppress("UNCHECKED_CAST")
object EntityExtractor {

    // Local website aliases (move to AliasRepository.websiteAliases if needed)
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

    /**
     * Extracts all known entities from the input text.
     *
     * @param text natural language input
     * @return a mutable map with entity keys (String) and values (String, Int, or TimeEntity)
     */
    fun extract(text: String): MutableMap<String, Any> {
        val params = mutableMapOf<String, Any>()

        // Core entities
        extractApp(text)?.let { params["app"] = it }
        extractContact(text)?.let { params["contact"] = it }
        extractWebsite(text)?.let { params["website"] = it }
        extractNumber(text)?.let { params["number"] = it }
        extractTime(text)?.let { params["time"] = it }

        // New entities
        extractVolume(text)?.let { params["volume"] = it }
        extractBrightness(text)?.let { params["brightness"] = it }
        extractPercentage(text)?.let { params["percentage"] = it }
        extractPhoneNumber(text)?.let { params["phoneNumber"] = it }
        extractEmail(text)?.let { params["email"] = it }
        extractUrl(text)?.let { params["url"] = it }

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
    // Contact extraction (with time removal)
    // ----------------------------------------------------------------------

    private fun extractContact(text: String): String? {
        val trigger = AliasRepository.callAliases.firstOrNull {
            text.contains(it, ignoreCase = true)
        } ?: return null

        val afterTrigger = text.substringAfter(trigger, "").trim()
        if (afterTrigger.isEmpty()) return null

        // Remove any time expression from the substring to avoid contamination.
        val timeEntity = TimeExtractor.extract(afterTrigger)
        val cleaned = if (timeEntity.type != TimeType.NONE) {
            // Remove the raw matched time text from the string
            afterTrigger.replace(timeEntity.rawText, "").trim()
        } else {
            afterTrigger
        }

        // Now extract a name from the cleaned text.
        // Name pattern: letters, spaces, hyphens, apostrophes (both curly and straight), dots.
        val nameRegex = Regex("^[\\p{L}\\s'’.-]+")
        val name = nameRegex.find(cleaned)?.value?.trim()
        return name?.takeIf { it.isNotEmpty() }
    }

    // ----------------------------------------------------------------------
    // Website extraction (using local aliases + domain detection)
    // ----------------------------------------------------------------------

    private fun extractWebsite(text: String): String? {
        val lower = text.lowercase()

        // 1. Check known aliases from local map.
        for ((site, aliases) in websiteAliases) {
            if (aliases.any { lower.contains(it.lowercase()) }) {
                return site
            }
        }

        // 2. Detect "open" / "go to" followed by a word.
        val openTriggers = listOf("open", "go to", "launch", "visit")
        for (trigger in openTriggers) {
            if (lower.contains(trigger)) {
                val after = text.substringAfter(trigger, "").trim()
                val wordRegex = Regex("^[\\w.-]+")
                val domain = wordRegex.find(after)?.value
                if (!domain.isNullOrBlank()) {
                    return domain.replace(Regex("\\.(com|org|net|co|in|io)$"), "")
                }
            }
        }

        // 3. Detect domain‑like pattern (e.g., "youtube.com").
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
        // Patterns: "volume 5", "set volume to 70", "volume up" (ignored)
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
    // Phone number (simple)
    // ----------------------------------------------------------------------

    private fun extractPhoneNumber(text: String): String? {
        // Supports: +91 9876543210, 987-654-3210, 9876543210
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