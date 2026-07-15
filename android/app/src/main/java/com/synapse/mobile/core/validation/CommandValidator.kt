package com.synapse.mobile.core.validation

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

interface CommandValidator {

    fun validate(command: Command): CommandResult?

}