package com.synapse.mobile.features.ai

interface AIClient {

    suspend fun generate(
        prompt: String
    ): String

}