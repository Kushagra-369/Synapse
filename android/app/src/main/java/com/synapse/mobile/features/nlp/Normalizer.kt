package com.synapse.mobile.features.nlp

/**
 * Normalizes raw user input for reliable intent detection and entity extraction.
 *
 * Key improvements:
 * - Expanded list of common fillers (English & Hindi) to remove noise.
 * - Mapped "set" → "" to avoid interference (e.g., "set alarm" → "alarm").
 * - Added synonyms for "open", "close", "flashlight", "volume", "brightness".
 * - Preserves digits, colon, and letters – essential for time extraction.
 * - Leaves time‑related keywords (am, pm, baje, subah, etc.) untouched.
 */
object Normalizer {

    // Ordered replacements – longer phrases first to avoid partial matches.
    private val replacements = mapOf(

        // ----- Polite / filler words (English) -----
        "please" to "",
        "plz" to "",
        "kindly" to "",
        "can you" to "",
        "could you" to "",
        "would you" to "",
        "actually" to "",
        "basically" to "",
        "literally" to "",
        "you know" to "",
        "i want" to "",
        "i need" to "",
        "i would like" to "",
        "could i" to "",

        // ----- Hindi fillers -----
        "bhai" to "",
        "yaar" to "",
        "yar" to "",
        "zara" to "",
        "jara" to "",
        "matlab" to "",
        "mtlb" to "",
        "yaani" to "",
        "kar do" to "",
        "kar dena" to "",
        "karo" to "",
        "mere liye" to "",
        "thik hai" to "",

        // ----- Open / launch / start -----
        "khol do" to "open",
        "kholde" to "open",
        "kholdo" to "open",
        "khol" to "open",
        "kholo" to "open",
        "launch" to "open",
        "start" to "open",
        "open up" to "open",

        // ----- Close / exit -----
        "band kar do" to "close",
        "band kardo" to "close",
        "band karo" to "close",
        "close it" to "close",
        "exit" to "close",

        // ----- Flashlight -----
        "torch" to "flashlight",
        "light" to "flashlight",
        "flash" to "flashlight",

        // ----- Volume -----
        "sound" to "volume",
        "voice" to "volume",
        "audio" to "volume",

        // ----- Brightness -----
        "screen brightness" to "brightness",
        "brightness ko" to "brightness",
        "bright" to "brightness",

        // ----- Alarm / timer helpers -----
        "set alarm" to "alarm",
        "set timer" to "timer",
        "set stopwatch" to "stopwatch",

        // ----- General clean‑up -----
        "set" to "",      // removes "set" but leaves the rest (e.g., "volume" remains)
        "to" to "",       // careful: might affect "go to" but we have "go to" as open trigger separately
        "please" to "",   // already present but safe
        "kindly" to ""    // already present
    )

    /**
     * Normalizes the input string:
     * - Converts to lowercase.
     * - Applies replacement map (longest‑first).
     * - Removes all characters except letters, digits, colon, and space.
     * - Collapses multiple spaces and trims.
     *
     * @param input Raw user utterance.
     * @return Clean, normalized string ready for intent detection and entity extraction.
     */
    fun normalize(input: String): String {
        var text = input.lowercase()

        // Apply replacements (longer phrases are handled first because map iteration order is predictable,
        // but we also sort keys by length descending to avoid partial replacements).
        val sortedKeys = replacements.keys.sortedByDescending { it.length }
        for (key in sortedKeys) {
            val value = replacements[key] ?: continue
            text = text.replace(key, value)
        }

        // Keep only letters, digits, colon, and spaces.
        text = text
            .replace(Regex("[^a-z0-9: ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return text
    }
}