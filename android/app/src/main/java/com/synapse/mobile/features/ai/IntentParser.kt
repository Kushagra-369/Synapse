package com.synapse.mobile.features.ai

import com.synapse.mobile.core.models.Command
import org.json.JSONObject

object IntentParser {

    fun parse(json: String): Command {

        val obj = JSONObject(json)

        val parameters = mutableMapOf<String, Any>()

        if (obj.has("parameters")) {
            val params = obj.getJSONObject("parameters")

            params.keys().forEach { key ->
                parameters[key] = params.get(key)
            }
        }

        return Command(
            skill = obj.getString("skill"),
            action = obj.getString("action"),
            parameters = parameters
        )
    }
}