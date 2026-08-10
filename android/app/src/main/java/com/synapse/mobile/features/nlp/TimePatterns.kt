package com.synapse.mobile.features.nlp

object TimePatterns {

    /**
     * Examples:
     * 6 pm
     * 6am
     * 11 PM
     */
    val AM_PM_TIME = Regex(
        """\b(\d{1,2})\s*(am|pm)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Examples:
     * 6:30 pm
     * 11:45 AM
     */
    val AM_PM_TIME_WITH_MINUTES = Regex(
        """\b(\d{1,2}):(\d{2})\s*(am|pm)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Examples:
     * 18:30
     * 07:45
     */
    val TWENTY_FOUR_HOUR_TIME = Regex(
        """\b([01]?\d|2[0-3]):([0-5]\d)\b"""
    )

    /**
     * Examples:
     * 30 min
     * 30 mins
     * 30 minute
     * 30 minutes
     */
    val MINUTES = Regex(
        """\b(\d+)\s*(min|mins|minute|minutes)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Examples:
     * 2 hr
     * 2 hrs
     * 2 hour
     * 2 hours
     */
    val HOURS = Regex(
        """\b(\d+)\s*(hr|hrs|hour|hours)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Examples:
     * 45 sec
     * 45 second
     * 45 seconds
     */
    val SECONDS = Regex(
        """\b(\d+)\s*(sec|secs|second|seconds)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Hindi minutes: 30 minute, 30 minut
     */


    /**
     * Hindi hours: 2 ghanta, 2 ghante
     */
    val HINDI_HOURS = Regex(
        """\b(\d+)\s*(ghanta|ghante)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Hindi seconds: 20 second, 20 seconde
     */

    /**
     * Today
     */
    val TODAY = Regex(
        """\btoday\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Tomorrow / Kal
     */
    val TOMORROW = Regex(
        """\b(tomorrow|kal)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Day after tomorrow / Parso
     */
    val DAY_AFTER_TOMORROW = Regex(
        """\b(day after tomorrow|parso)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Yesterday
     */
    val YESTERDAY = Regex(
        """\b(yesterday)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Weekdays
     */
    val WEEKDAY = Regex(
        """\b(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Next weekday (e.g., "next monday")
     */
    val NEXT_WEEKDAY = Regex(
        """\bnext\s+(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\b""",
        RegexOption.IGNORE_CASE
    )

    // ====== NEW PATTERNS ===================================================

    /**
     * "6 baje" (exact hour, no minutes)
     */
    val BAJE_TIME = Regex(
        """\b(\d{1,2})\s*baje\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * "6:30 baje"
     */
    val BAJE_TIME_WITH_MINUTES = Regex(
        """\b(\d{1,2}):(\d{2})\s*baje\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * "subah 7 baje", "shaam 8 baje", "dopahar 2 baje", "raat 11 baje"
     */
    val TIME_OF_DAY_BAJE = Regex(
        """\b(subah|shaam|dopahar|raat)\s+(\d{1,2})\s*baje\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * "this friday", "this monday"
     */
    val THIS_WEEKDAY = Regex(
        """\bthis\s+(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Hindi clock phrases:
     * - "sawa 6"  → 6:15
     * - "paune 7" → 6:45  (hour-1, minute=45)
     * - "saade 8" → 8:30
     */
    val SAWA_TIME = Regex(
        """\bsawa\s+(\d{1,2})\b""",
        RegexOption.IGNORE_CASE
    )
    val PAUNE_TIME = Regex(
        """\bpaune\s+(\d{1,2})\b""",
        RegexOption.IGNORE_CASE
    )
    val SAADE_TIME = Regex(
        """\bsaade\s+(\d{1,2})\b""",
        RegexOption.IGNORE_CASE
    )

    /**
     * Duration: "aadha ghanta" → 30 minutes
     * Also "aadhe ghante" (same)
     */
    val AADHA_GHANTA = Regex(
        """\baadha\s+ghanta\b|\baadhe\s+ghante\b""",
        RegexOption.IGNORE_CASE
    )
}