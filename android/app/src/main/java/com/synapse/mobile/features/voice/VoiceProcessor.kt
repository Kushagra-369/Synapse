package com.synapse.mobile.features.voice

import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.core.response.ResponseGenerator
import com.synapse.mobile.features.nlp.NLPProcessor

class VoiceProcessor(
    private val nlpProcessor: NLPProcessor,
    private val engine: SynapseEngine,
    private val textToSpeechManager: TextToSpeechManager
) {

    /**
     * Processes recognized speech.
     */
    suspend fun process(
        spokenText: String
    ): CommandResult {

        if (spokenText.isBlank()) {

            val result = CommandResult(
                success = false,
                message = "I didn't hear anything."
            )

            textToSpeechManager.speak(result.message)

            return result
        }

        val command = nlpProcessor.process(spokenText)

        val executionResult = engine.execute(command)

        val response = ResponseGenerator.generate(
            command = command,
            success = executionResult.success
        )

        textToSpeechManager.speak(response.message)

        return response
    }
}