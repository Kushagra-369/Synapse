package com.synapse.mobile.features.nlp

object TimeUtils {

    fun to24Hour(hour: Int, isPm: Boolean): Int {
        var h = hour
        if (isPm && h < 12) h += 12
        if (!isPm && h == 12) h = 0
        return h
    }

    fun isValidHour(hour: Int): Boolean = hour in 0..23
    fun isValidMinute(minute: Int): Boolean = minute in 0..59

    fun normalizeUnit(unit: String): String {
        return when (unit.lowercase()) {
            "sec", "secs", "second", "seconds" -> "seconds"
            "min", "mins", "minute", "minutes", "minut" -> "minutes"
            "hr", "hrs", "hour", "hours", "ghanta", "ghante" -> "hours"
            else -> unit.lowercase()
        }
    }

    fun normalizeWeekDay(day: String): String =
        day.lowercase().replaceFirstChar { it.uppercase() }

    fun parseRelativeDay(text: String): Int {
        val lower = text.lowercase()
        return when {
            "today" in lower -> 0
            "kal" in lower || "tomorrow" in lower -> 1
            "parso" in lower || "day after tomorrow" in lower -> 2
            "yesterday" in lower -> -1
            else -> 0
        }
    }

    /**
     * Returns `true` for PM, `false` for AM, or `null` if the word is not recognised.
     */
    fun amPmForTimeOfDay(word: String): Boolean? {
        return when (word.lowercase()) {
            "subah" -> false   // morning → AM
            "shaam" -> true    // evening → PM
            "dopahar" -> true  // afternoon → PM
            "raat" -> true     // night → PM
            else -> null
        }
    }

    /**
     * For Hindi clock phrases:
     * - sawa 6 → hour=6, minute=15
     * - paune 7 → hour=6, minute=45
     * - saade 8 → hour=8, minute=30
     * Returns Pair(hour24, minute) or null if invalid.
     */
    fun parseHindiClock(phrase: String, hour: Int): Pair<Int, Int>? {
        if (!isValidHour(hour)) return null
        return when (phrase.lowercase()) {
            "sawa" -> Pair(hour, 15)
            "paune" -> {
                val h = hour - 1
                if (!isValidHour(h)) return null
                Pair(h, 45)
            }
            "saade" -> Pair(hour, 30)
            else -> null
        }
    }
}