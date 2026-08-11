package com.synapse.mobile.features.skills.device.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

class SetNfcAction : Action {

    override val name = "set_nfc"

    override suspend fun execute(command: Command): CommandResult {

        return CommandResult(
            false,
            "NFC control is not implemented yet."
        )

    }

}