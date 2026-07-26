package com.synapse.mobile.features.ai

import android.util.Log
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class AIProcessor(

    private val engine: SynapseEngine,
    private val aiClient: AIClient = GeminiClient()

) {

    suspend fun process(
        userInput: String
    ): CommandResult {

        return try {

            Log.d("Synapse-AI", "User: $userInput")

            val prompt = PromptBuilder.build(userInput)

            Log.d("Synapse-AI", "Prompt:\n$prompt")

            val aiResponse = aiClient.generate(prompt)

            Log.d("Synapse-AI", "Gemini:\n$aiResponse")

            val command: Command = IntentParser.parse(aiResponse)

            Log.d("Synapse-AI", "Command: $command")

            val result = engine.execute(command)

            Log.d("Synapse-AI", "Result: $result")

            result

        } catch (e: Exception) {

            Log.e(
                "Synapse-AI",
                "AI Error",
                e
            )

            CommandResult(
                success = false,
                message = e.message ?: "Unknown AI Error"
            )

        }

    }

}