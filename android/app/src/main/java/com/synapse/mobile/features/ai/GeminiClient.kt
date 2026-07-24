package com.synapse.mobile.features.ai

import com.synapse.mobile.features.ai.network.Content
import com.synapse.mobile.features.ai.network.GeminiRequest
import com.synapse.mobile.features.ai.network.Part
import com.synapse.mobile.features.ai.network.RetrofitClient

class GeminiClient : AIClient {

    override suspend fun generate(
        prompt: String
    ): String {

        return try {

            val request = GeminiRequest(

                contents = listOf(

                    Content(

                        parts = listOf(

                            Part(
                                text = prompt
                            )

                        )

                    )

                )

            )

            val response = RetrofitClient.api.generateContent(

                apiKey = GeminiConfig.API_KEY,

                request = request

            )

            if (!response.isSuccessful) {

                return "ERROR: ${response.code()}"

            }

            response.body()
                ?.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

                ?: "ERROR"

        } catch (e: Exception) {

            "ERROR: ${e.message}"

        }

    }

}