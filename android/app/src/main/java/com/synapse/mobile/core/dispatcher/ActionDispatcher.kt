package com.synapse.mobile.core.dispatcher

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class ActionDispatcher(
    private val registry: SkillRegistry
) {

    suspend fun dispatch(command: Command): CommandResult {

        val skill = registry.get(command.skill)

        return skill?.execute(command)
            ?: CommandResult(
                success = false,
                message = "Unknown skill: ${command.skill}"
            )
    }
}