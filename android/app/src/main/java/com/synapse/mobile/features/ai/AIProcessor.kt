package com.synapse.mobile.features.ai

import android.content.Context
import android.util.Log
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.nlp.NLPProcessor
import com.synapse.mobile.features.nlp.parser.MultiCommandParser

class AIProcessor(
    context: Context
) {

    private val engine = SynapseEngine(context)  // ← Only context needed, rest use defaults
    private val multiCommandParser = MultiCommandParser()
    private val nlpProcessor = NLPProcessor()

    suspend fun process(
        userInput: String
    ): CommandResult {

        return try {

            Log.d("Synapse-AI", "Input: $userInput")

            val commands = multiCommandParser.parse(userInput)

            if (commands.isEmpty()) {

                return CommandResult(
                    success = false,
                    message = "No command detected."
                )

            }

            val results = mutableListOf<CommandResult>()

            for (text in commands) {

                Log.d("Synapse-AI", "Processing: $text")

                val command = nlpProcessor.process(text)

                Log.d("Synapse-AI", "Command: ${command.skill}/${command.action}, params: ${command.parameters}")

                val result = engine.execute(command)

                Log.d("Synapse-AI", "Result: ${result.success} - ${result.message}")

                results.add(result)

            }

            val success = results.all { it.success }

            val message = buildString {

                results.forEachIndexed { index, result ->

                    append("${index + 1}. ${result.message}")

                    if (index != results.lastIndex) {
                        append("\n")
                    }

                }

            }

            CommandResult(
                success = success,
                message = message
            )

        } catch (e: Exception) {

            Log.e(
                "Synapse-AI",
                "Offline Processing Error",
                e
            )

            CommandResult(
                success = false,
                message = e.message ?: "Unknown error"
            )

        }

    }

    /**
     * Get last command for debugging
     */
    fun getLastCommand() = engine.lastCommand
}