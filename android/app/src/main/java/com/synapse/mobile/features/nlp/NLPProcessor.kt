package com.synapse.mobile.features.nlp

import com.synapse.mobile.core.models.Command

/**
 * Central orchestrator for the NLP pipeline.
 *
 * Process flow:
 * 1. Normalize input (remove filler words, standardise synonyms, clean punctuation)
 * 2. Detect user intent from the normalized text
 * 3. Extract all relevant entities (app, contact, website, time, volume, brightness, etc.)
 * 4. Build a structured [Command] that can be executed by the system
 *
 * This class is stateless and thread‑safe.
 */
class NLPProcessor {

    /**
     * Processes a raw user utterance and returns a structured [Command].
     *
     * @param rawInput The original text spoken or typed by the user.
     * @return A [Command] containing the skill, action, and extracted parameters.
     *         If no intent is detected, [Command] with empty skill/action is returned.
     */
    fun process(rawInput: String): Command {
        // 1. Normalize – remove noise and standardise terms.
        val normalized = Normalizer.normalize(rawInput)

        // 2. Detect intent.
        val intent = IntentDetector.detect(normalized)

        // 3. Extract entities from the normalized text.
        val parameters = EntityExtractor.extract(normalized)

        // 4. Build and return the command.
        return CommandBuilder.build(intent, parameters)
    }
}