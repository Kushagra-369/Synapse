package com.synapse.mobile.core.validation

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class DefaultCommandValidator : CommandValidator {

    override fun validate(command: Command): CommandResult? {

        if (command.skill.isBlank()) {
            return CommandResult(
                success = false,
                message = "Skill cannot be empty."
            )
        }

        if (command.action.isBlank()) {
            return CommandResult(
                success = false,
                message = "Action cannot be empty."
            )
        }

        return null
    }
}