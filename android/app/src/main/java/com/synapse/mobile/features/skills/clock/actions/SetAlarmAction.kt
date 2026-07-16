package com.synapse.mobile.features.skills.clock.actions

import com.synapse.mobile.core.actions.Action
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.clock.gateway.ClockGateway

class SetAlarmAction(
    private val gateway: ClockGateway
) : Action {

    override val name: String = "set_alarm"

    override fun execute(command: Command): CommandResult {

        val time = command.parameters["time"]?.toString()
            ?: return CommandResult(
                success = false,
                message = "Missing parameter: time"
            )

        val parts = time.split(":")

        if (parts.size != 2) {
            return CommandResult(
                success = false,
                message = "Invalid time format. Expected HH:mm"
            )
        }

        val hour = parts[0].toIntOrNull()
        val minute = parts[1].toIntOrNull()

        if (hour == null || minute == null) {
            return CommandResult(
                success = false,
                message = "Invalid time value."
            )
        }
        android.util.Log.e("Synapse", "Calling gateway")
        val success = gateway.setAlarm(hour, minute)

        return CommandResult(
            success = success,
            message = if (success)
                "Alarm scheduled for %02d:%02d".format(hour, minute)
            else
                "Failed to schedule alarm."
        )
    }
}