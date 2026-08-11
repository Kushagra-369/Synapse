package com.synapse.mobile.features.skills.phone.actions
import android.content.Context
import com.synapse.mobile.core.resolver.ContactResolver
import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.phone.gateway.PhoneGateway

class MakeCallAction(
    private val gateway: PhoneGateway,
    private val context: Context
): Action {

    override val name: String = "dial_phone"

    override suspend fun execute(command: Command): CommandResult {

        val number = when {

            command.parameters["number"] != null ->
                command.parameters["number"].toString()

            command.parameters["contact"] != null -> {

                val contact =
                    command.parameters["contact"].toString()

                ContactResolver(context)
                    .getPhoneNumber(contact)

            }

            else -> null
        }
            ?: return CommandResult(
                false,
                "Contact not found."
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