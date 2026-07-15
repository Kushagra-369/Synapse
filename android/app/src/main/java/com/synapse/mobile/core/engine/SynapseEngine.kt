package com.synapse.mobile.core.engine

import com.synapse.mobile.core.dispatcher.ActionDispatcher
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.core.validation.DefaultCommandValidator
import com.synapse.mobile.features.skills.clock.ClockSkill

class SynapseEngine {

    private val registry = SkillRegistry()

    private val dispatcher = ActionDispatcher(registry)

    private val validator = DefaultCommandValidator()

    init {
        registry.register(
            ClockSkill()
        )
    }

    fun execute(command: Command): CommandResult {

        val validation = validator.validate(command)

        if (validation != null) {
            return validation
        }

        return dispatcher.dispatch(command)
    }
}