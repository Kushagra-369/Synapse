package com.synapse.mobile.features.nlp

import java.time.LocalDate

data class DateEntity(
    val day: Int,
    val month: Int,
    val year: Int
)

object DateExtractor {

    private val months = mapOf(

        "january" to 1,
        "jan" to 1,

        "february" to 2,
        "feb" to 2,

        "march" to 3,
        "mar" to 3,

        "april" to 4,
        "apr" to 4,

        "may" to 5,

        "june" to 6,
        "jun" to 6,

        "july" to 7,
        "jul" to 7,

        "august" to 8,
        "aug" to 8,

        "september" to 9,
        "sep" to 9,

        "october" to 10,
        "oct" to 10,

        "november" to 11,
        "nov" to 11,

        "december" to 12,
        "dec" to 12
    )

    fun extract(text: String): DateEntity? {

        val lower = text.lowercase()

        if (lower.contains("today") || lower.contains("aaj")) {

            val now = LocalDate.now()

            return DateEntity(
                now.dayOfMonth,
                now.monthValue,
                now.year
            )
        }

        if (lower.contains("tomorrow") || lower.contains("kal")) {

            val now = LocalDate.now().plusDays(1)

            return DateEntity(
                now.dayOfMonth,
                now.monthValue,
                now.year
            )
        }

        val regex = Regex(
            """(\d{1,2})\s+(jan|january|feb|february|mar|march|apr|april|may|jun|june|jul|july|aug|august|sep|september|oct|october|nov|november|dec|december)""",
            RegexOption.IGNORE_CASE
        )

        val match = regex.find(lower) ?: return null

        val day = match.groupValues[1].toInt()

        val month =
            months[
                match.groupValues[2].lowercase()
            ] ?: return null

        val year = LocalDate.now().year

        return DateEntity(
            day,
            month,
            year
        )
    }
}