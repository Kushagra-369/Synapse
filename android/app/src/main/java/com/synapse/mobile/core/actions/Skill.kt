package com.synapse.mobile.core.actions

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

interface Skill {

    val name: String

    suspend fun execute(
        command: Command
    ): CommandResult
}