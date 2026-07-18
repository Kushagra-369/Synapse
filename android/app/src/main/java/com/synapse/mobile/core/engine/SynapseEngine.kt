package com.synapse.mobile.core.engine

import com.synapse.mobile.core.dispatcher.ActionDispatcher
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.core.models.BatchResult
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandBatch
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.core.validation.DefaultCommandValidator

class SynapseEngine(
    registry: SkillRegistry
) {

    private val dispatcher = ActionDispatcher(registry)

    private val validator = DefaultCommandValidator()

    fun execute(command: Command): CommandResult {

        val validation = validator.validate(command)

        if (validation != null) {
            return validation
        }

        return dispatcher.dispatch(command)
    }

    fun executeBatch(batch: CommandBatch): BatchResult {

        val results = mutableListOf<CommandResult>()

        for (command in batch.commands) {

            val result = execute(command)

            results.add(result)
        }

        return BatchResult(results)
    }
}