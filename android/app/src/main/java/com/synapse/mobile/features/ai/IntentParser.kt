package com.synapse.mobile.features.ai
import org.json.JSONObject
import com.synapse.mobile.core.models.Command

object IntentParser {

    fun parse(
        json: String
    ): Command {

        val obj = JSONObject(json)

        val parameters = mutableMapOf<String, Any>()

        val params =
            obj.getJSONObject("parameters")

        params.keys().forEach {

            parameters[it] = params.get(it)

        }

        return Command(

            skill = obj.getString("skill"),

            action = obj.getString("action"),

            parameters = parameters

        )

    }

}