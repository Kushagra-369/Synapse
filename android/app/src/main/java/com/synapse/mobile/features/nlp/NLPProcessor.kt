package com.synapse.mobile.features.nlp

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.features.nlp.parser.MultiCommandParser   // ← Add this line

/**
 * Central orchestrator for the NLP pipeline.
 *
 * Process flow:
 * 1. Normalize input (remove filler words, standardise synonyms, clean punctuation)
 * 2. Parse multi-commands (split by "and", "then", etc.)
 * 3. For each command:
 *    a. Detect user intent from the normalized text
 *    b. Extract all relevant entities (app, contact, website, time, volume, brightness, etc.)
 *    c. Build a structured [Command] that can be executed by the system
 * 4. Return the first command (or combined if needed)
 *
 * This class is stateless and thread‑safe.
 */
class NLPProcessor {

    private val multiCommandParser = MultiCommandParser()

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

        // 2. Parse multi-commands (e.g., "open youtube and search cats")
        val commands = multiCommandParser.parse(normalized)

        // For now, process only the first command
        // In the future, you can process all and combine results
        val primaryCommand = commands.firstOrNull() ?: normalized

        // 3. Detect intent.
        val intent = IntentDetector.detect(primaryCommand)

        // 4. Extract entities from the normalized text.
        val parameters = EntityExtractor.extract(
            primaryCommand,
            intent
        )

        // 5. Add intent to parameters for context manager
        parameters["intent"] = intent

        // 6. Build and return the command.
        return CommandBuilder.build(intent, parameters)
    }
}