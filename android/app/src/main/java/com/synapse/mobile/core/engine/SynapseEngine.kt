package com.synapse.mobile.core.engine

import com.synapse.mobile.core.dispatcher.ActionDispatcher
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.core.models.BatchResult
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandBatch
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.core.validation.CommandValidator
import com.synapse.mobile.core.validation.DefaultCommandValidator

/**
 * Central execution engine for Synapse.
 *
 * Responsibilities:
 * - Validate commands
 * - Dispatch commands to the correct skill
 * - Execute command batches
 *
 * This class is stateless and thread-safe.
 */
class SynapseEngine(
    registry: SkillRegistry,
    private val validator: CommandValidator = DefaultCommandValidator()
) {

    private val dispatcher = ActionDispatcher(registry)

    /**
     * Executes a single command.
     */
    fun execute(command: Command): CommandResult {

        validator.validate(command)?.let {
            return it
        }

        return dispatcher.dispatch(command)
    }

    /**
     * Executes multiple commands sequentially.
     */
    fun executeBatch(batch: CommandBatch): BatchResult {

        if (batch.commands.isEmpty()) {
            return BatchResult(emptyList())
        }

        val results = batch.commands.map(::execute)

        return BatchResult(results)
    }
}