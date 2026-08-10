package com.synapse.mobile.features.nlp

/**
 * Production‑ready time extractor.
 *
 * Supports:
 * - Clock time: 6 pm, 6:30 pm, 18:30, 6:30, 6 baje, subah 7 baje, shaam 8 baje,
 *   dopahar 2 baje, raat 11 baje, sawa 6, paune 7, saade 8
 * - Duration: 30 min, 2 hours, 1 hour 30 minutes, 2 ghante 20 minute,
 *   90 min, 30 minute baad, after 2 hours, aadha ghanta
 * - Relative day: today, tomorrow, kal, parso, day after tomorrow, yesterday
 * - Weekday: monday, next monday, this friday
 * - Combined: kal 6 pm, tomorrow 8 am, next monday 6 pm, etc.
 *
 * Validation: rejects invalid hour/minute and negative/zero durations.
 * Performance: single‑scan extraction for durations and clock time where possible.
 */
object TimeExtractor {

    // ----------------------------------------------------------------------
    // Private data classes
    // ----------------------------------------------------------------------

    private data class ClockMatch(
        val hour: Int,
        val minute: Int,
        val isPm: Boolean?,
        val raw: String
    )

    private data class DurationMatch(
        val value: Int,
        val unit: String,      // "seconds", "minutes", "hours"
        val raw: String
    )

    private data class RelativeDayMatch(
        val offset: Int,
        val raw: String
    )

    private data class WeekdayMatch(
        val day: String,
        val raw: String
    )

    // ----------------------------------------------------------------------
    // Public API
    // ----------------------------------------------------------------------

    fun extract(text: String): TimeEntity {
        if (text.isBlank()) return TimeEntity()

        // 1. Find all durations.
        val durationMatches = findAllDurations(text)
        if (durationMatches.isNotEmpty()) {
            // Determine if all are in seconds → keep as seconds, else total minutes.
            val allSeconds = durationMatches.all { it.unit == "seconds" }
            return if (allSeconds) {
                val totalSeconds = durationMatches.sumOf { it.value }
                TimeEntity(
                    type = TimeType.DURATION,
                    duration = totalSeconds,
                    durationUnit = "seconds",
                    rawText = durationMatches.joinToString(" ") { it.raw }
                )
            } else {
                val totalMinutes = durationMatches.sumOf { convertToMinutes(it.value, it.unit) }
                TimeEntity(
                    type = TimeType.DURATION,
                    duration = totalMinutes,
                    durationUnit = "minutes",
                    rawText = durationMatches.joinToString(" ") { it.raw }
                )
            }
        }

        // 2. Find clock time and date components.
        val clock = findClockTime(text)
        val relative = findRelativeDay(text)
        val weekday = findWeekday(text)

        // 3. Combine if both clock and a date component exist.
        if (clock != null && (relative != null || weekday != null)) {
            val offset = relative?.offset ?: 0
            val day = weekday?.day
            return TimeEntity(
                type = TimeType.DATE,
                hour = clock.hour,
                minute = clock.minute,
                isPm = clock.isPm,
                dayOffset = offset,
                weekDay = day,
                rawText = listOfNotNull(clock.raw, relative?.raw, weekday?.raw).joinToString(" ")
            )
        }

        // 4. Clock only.
        if (clock != null) {
            return TimeEntity(
                type = TimeType.CLOCK_TIME,
                hour = clock.hour,
                minute = clock.minute,
                isPm = clock.isPm,
                rawText = clock.raw
            )
        }

        // 5. Relative day only.
        if (relative != null) {
            return TimeEntity(
                type = TimeType.RELATIVE_DAY,
                dayOffset = relative.offset,
                rawText = relative.raw
            )
        }

        // 6. Weekday only.
        if (weekday != null) {
            return TimeEntity(
                type = TimeType.DATE,
                weekDay = weekday.day,
                rawText = weekday.raw
            )
        }

        return TimeEntity()
    }

    // ----------------------------------------------------------------------
    // Duration extraction and combination
    // ----------------------------------------------------------------------

    private val durationPatterns = listOf(
        TimePatterns.MINUTES,
        TimePatterns.HOURS,
        TimePatterns.SECONDS,
        TimePatterns.HINDI_HOURS,
        TimePatterns.AADHA_GHANTA
    )

    private fun findAllDurations(text: String): List<DurationMatch> {
        val matches = mutableListOf<DurationMatch>()

        // First, handle the special "aadha ghanta" phrase.
        TimePatterns.AADHA_GHANTA.findAll(text).forEach { match ->
            // It's always 30 minutes.
            matches.add(DurationMatch(30, "minutes", match.value))
        }

        // Now the numeric patterns.
        for (pattern in durationPatterns) {
            if (pattern == TimePatterns.AADHA_GHANTA) continue // already handled
            pattern.findAll(text).forEach { match ->
                val value = match.groupValues.getOrNull(1)?.toIntOrNull() ?: return@forEach
                val unit = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: return@forEach
                if (value <= 0) return@forEach
                val normalizedUnit = TimeUtils.normalizeUnit(unit)
                matches.add(DurationMatch(value, normalizedUnit, match.value))
            }
        }

        return matches
    }

    private fun convertToMinutes(value: Int, unit: String): Int {
        return when (unit) {
            "seconds" -> (value + 30) / 60   // round to nearest minute
            "minutes" -> value
            "hours" -> value * 60
            else -> value
        }
    }

    // ----------------------------------------------------------------------
    // Clock time extraction
    // ----------------------------------------------------------------------

    // Order matters: more specific first.
    private val clockPatterns = listOf(
        TimePatterns.AM_PM_TIME_WITH_MINUTES,   // 6:30 pm
        TimePatterns.BAJE_TIME_WITH_MINUTES,    // 6:30 baje
        TimePatterns.TIME_OF_DAY_BAJE,          // subah 7 baje
        TimePatterns.SAWA_TIME,                 // sawa 6
        TimePatterns.PAUNE_TIME,                // paune 7
        TimePatterns.SAADE_TIME,                // saade 8
        TimePatterns.TWENTY_FOUR_HOUR_TIME,     // 18:30, 6:30
        TimePatterns.BAJE_TIME,                 // 6 baje
        TimePatterns.AM_PM_TIME                 // 6 pm
    )

    private fun findClockTime(text: String): ClockMatch? {
        for (pattern in clockPatterns) {
            val match = pattern.find(text) ?: continue
            when (pattern) {
                TimePatterns.AM_PM_TIME_WITH_MINUTES -> {
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    val minute = match.groupValues[2].toIntOrNull() ?: continue
                    val ampm = match.groupValues[3].lowercase()
                    if (!TimeUtils.isValidHour(hour) || !TimeUtils.isValidMinute(minute)) continue
                    val isPm = ampm == "pm"
                    val hour24 = TimeUtils.to24Hour(hour, isPm)
                    return ClockMatch(hour24, minute, isPm, match.value)
                }

                TimePatterns.BAJE_TIME_WITH_MINUTES -> {
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    val minute = match.groupValues[2].toIntOrNull() ?: continue
                    if (!TimeUtils.isValidHour(hour) || !TimeUtils.isValidMinute(minute)) continue
                    return ClockMatch(hour, minute, null, match.value)
                }

                TimePatterns.TIME_OF_DAY_BAJE -> {
                    val timeOfDay = match.groupValues[1].lowercase()
                    val hour = match.groupValues[2].toIntOrNull() ?: continue
                    if (!TimeUtils.isValidHour(hour)) continue
                    val isPm = TimeUtils.amPmForTimeOfDay(timeOfDay) ?: continue
                    val hour24 = TimeUtils.to24Hour(hour, isPm)
                    return ClockMatch(hour24, 0, isPm, match.value)
                }

                TimePatterns.SAWA_TIME,
                TimePatterns.PAUNE_TIME,
                TimePatterns.SAADE_TIME -> {
                    val phrase = when (pattern) {
                        TimePatterns.SAWA_TIME -> "sawa"
                        TimePatterns.PAUNE_TIME -> "paune"
                        TimePatterns.SAADE_TIME -> "saade"
                        else -> continue
                    }
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    val (h, m) = TimeUtils.parseHindiClock(phrase, hour) ?: continue
                    return ClockMatch(h, m, null, match.value)
                }

                TimePatterns.TWENTY_FOUR_HOUR_TIME -> {
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    val minute = match.groupValues[2].toIntOrNull() ?: continue
                    if (!TimeUtils.isValidHour(hour) || !TimeUtils.isValidMinute(minute)) continue
                    return ClockMatch(hour, minute, null, match.value)
                }

                TimePatterns.BAJE_TIME -> {
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    if (!TimeUtils.isValidHour(hour)) continue
                    return ClockMatch(hour, 0, null, match.value)
                }

                TimePatterns.AM_PM_TIME -> {
                    val hour = match.groupValues[1].toIntOrNull() ?: continue
                    val ampm = match.groupValues[2].lowercase()
                    if (!TimeUtils.isValidHour(hour)) continue
                    val isPm = ampm == "pm"
                    val hour24 = TimeUtils.to24Hour(hour, isPm)
                    return ClockMatch(hour24, 0, isPm, match.value)
                }

                else -> continue
            }
        }
        return null
    }

    // ----------------------------------------------------------------------
    // Relative day extraction
    // ----------------------------------------------------------------------

    private val relativePatterns = listOf(
        TimePatterns.TODAY,
        TimePatterns.TOMORROW,
        TimePatterns.DAY_AFTER_TOMORROW,
        TimePatterns.YESTERDAY
    )

    private fun findRelativeDay(text: String): RelativeDayMatch? {
        for (pattern in relativePatterns) {
            val match = pattern.find(text) ?: continue
            val offset = TimeUtils.parseRelativeDay(match.value)
            return RelativeDayMatch(offset, match.value)
        }
        return null
    }

    // ----------------------------------------------------------------------
    // Weekday extraction
    // ----------------------------------------------------------------------

    private val weekdayPatterns = listOf(
        TimePatterns.NEXT_WEEKDAY,
        TimePatterns.THIS_WEEKDAY,
        TimePatterns.WEEKDAY
    )

    private fun findWeekday(text: String): WeekdayMatch? {
        for (pattern in weekdayPatterns) {
            val match = pattern.find(text) ?: continue
            val weekday = match.groupValues.getOrNull(1)?.takeIf { it.isNotBlank() } ?: continue
            val normalized = TimeUtils.normalizeWeekDay(weekday)
            return WeekdayMatch(normalized, match.value)
        }
        return null
    }
}