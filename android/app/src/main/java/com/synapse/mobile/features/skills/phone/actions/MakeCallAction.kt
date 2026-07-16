package com.synapse.mobile.features.skills.phone.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.phone.gateway.PhoneGateway

class MakeCallAction(
    private val gateway: PhoneGateway
) : Action {

    override val name: String = "dial_phone"

    override fun execute(command: Command): CommandResult {

        val number = command.parameters["number"]
            ?.toString()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: number"
            )

        val success = gateway.dialPhone(number)

        return CommandResult(
            success = success,
            message = if (success)
                "Dialer opened successfully."
            else
                "Failed to open dialer."
        )
    }
}