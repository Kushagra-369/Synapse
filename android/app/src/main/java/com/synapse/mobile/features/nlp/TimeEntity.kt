package com.synapse.mobile.features.nlp

enum class TimeType {
    NONE,

    // Absolute time (e.g., 6:30 PM)
    CLOCK_TIME,

    // Relative duration (e.g., 30 minutes)
    DURATION,

    // Calendar date (e.g., Monday, next Friday)
    DATE,

    // Relative day (today, tomorrow, yesterday)
    RELATIVE_DAY
}

/**
 * Represents a time‑related entity extracted from user input.
 *
 * @property type The kind of time expression.
 * @property hour 24‑hour hour (if CLOCK_TIME or DATE with time).
 * @property minute Minute (if CLOCK_TIME or DATE with time).
 * @property isPm `true` if PM, `false` if AM, `null` if 24‑hour or unknown.
 * @property duration Numeric value for a duration.
 * @property durationUnit Unit of duration: "seconds", "minutes", or "hours".
 * @property dayOffset Offset from today: 0=today, 1=tomorrow, 2=day after tomorrow, -1=yesterday.
 * @property weekDay Name of the weekday (e.g., "Monday") if applicable.
 * @property rawText The original text that was matched.
 */
data class TimeEntity(
    val type: TimeType = TimeType.NONE,
    val hour: Int? = null,
    val minute: Int? = null,
    val isPm: Boolean? = null,
    val duration: Int? = null,
    val durationUnit: String? = null,
    val dayOffset: Int = 0,
    val weekDay: String? = null,
    val rawText: String = ""
) {

    /**
     * Returns `true` if this entity represents any valid time information.
     */
    fun hasTime(): Boolean = type != TimeType.NONE

    /**
     * Returns `true` if this entity represents a clock time (with or without date).
     */
    fun isClockTime(): Boolean = type == TimeType.CLOCK_TIME || (type == TimeType.DATE && hour != null)

    /**
     * Returns `true` if this entity represents a duration.
     */
    fun isDuration(): Boolean = type == TimeType.DURATION

    /**
     * Returns `true` if this entity represents a relative day (today/tomorrow/etc.) or a weekday.
     */
    fun isDate(): Boolean = type == TimeType.RELATIVE_DAY || type == TimeType.DATE

    /**
     * Returns a human‑readable summary (for debugging or logging).
     */
    fun toReadableString(): String {
        return when (type) {
            TimeType.NONE -> "No time"
            TimeType.CLOCK_TIME -> {
                val suffix = if (isPm == true) "PM" else if (isPm == false) "AM" else ""
                val h = hour ?: 0
                val m = minute ?: 0
                "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')} $suffix".trim()
            }
            TimeType.DURATION -> "$duration $durationUnit"
            TimeType.RELATIVE_DAY -> {
                when (dayOffset) {
                    0 -> "today"
                    1 -> "tomorrow"
                    2 -> "day after tomorrow"
                    -1 -> "yesterday"
                    else -> "day offset $dayOffset"
                }
            }
            TimeType.DATE -> {
                val datePart = weekDay?.let { "on $it" } ?: when (dayOffset) {
                    0 -> "today"
                    1 -> "tomorrow"
                    2 -> "day after tomorrow"
                    -1 -> "yesterday"
                    else -> ""
                }
                val timePart = if (hour != null && minute != null) {
                    val suffix = if (isPm == true) "PM" else if (isPm == false) "AM" else ""
                    val h = hour ?: 0
                    val m = minute ?: 0
                    " at ${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')} $suffix".trim()
                } else ""
                (datePart + timePart).trim()
            }
        }
    }
}