package com.synapse.mobile.features.ai

import com.synapse.mobile.core.models.Command
import org.json.JSONObject

object IntentParser {

    fun parse(response: String): Command {

        if (response.startsWith("ERROR")) {
            throw Exception(response)
        }

        var json = response.trim()

        json = json
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val start = json.indexOf('{')
        val end = json.lastIndexOf('}')

        if (start == -1 || end == -1) {
            throw Exception("Gemini did not return valid JSON.\n\n$response")
        }

        json = json.substring(start, end + 1)

        val obj = JSONObject(json)

        val parameters = mutableMapOf<String, Any>()

        if (obj.has("parameters")) {

            val params = obj.getJSONObject("parameters")

            params.keys().forEach {
                parameters[it] = params.get(it)
            }
        }

        return Command(
            skill = obj.getString("skill"),
            action = obj.getString("action"),
            parameters = parameters
        )
    }
}