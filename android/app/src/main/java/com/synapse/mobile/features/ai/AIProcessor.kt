package com.synapse.mobile.features.ai

import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.CommandResult


class AIProcessor(
    private val engine: SynapseEngine,
    private val aiClient: AIClient = GeminiClient()
) {



    suspend fun process(
        userInput: String
    ): CommandResult {

        return try {

            val prompt = PromptBuilder.build(userInput)

            val aiResponse = aiClient.generate(prompt)

            println("========== GEMINI RESPONSE ==========")
            println(aiResponse)
            println("=====================================")

            val command = IntentParser.parse(aiResponse)

            engine.execute(command)

            engine.execute(command)

        } catch (e: Exception) {

            CommandResult(
                success = false,
                message = e.message ?: "Unknown error"
            )

        }

    }

}