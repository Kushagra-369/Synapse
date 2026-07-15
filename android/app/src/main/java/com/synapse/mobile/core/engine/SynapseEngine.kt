package com.synapse.mobile.core.engine

import com.synapse.mobile.core.dispatcher.ActionDispatcher
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.features.skills.clock.ClockSkill

class SynapseEngine {

    private val registry = SkillRegistry()

    private val dispatcher = ActionDispatcher(registry)

    init {

        registry.register(
            ClockSkill()
        )

    }

    fun execute(
        command: Command
    ) = dispatcher.dispatch(command)

}