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

        val time =
            command.parameters["time"]
                    as? com.synapse.mobile.features.nlp.TimeEntity
                ?: return CommandResult(
                    success = false,
                    message = "Missing parameter: time"
                )

        val hour =
            time.hour
                ?: return CommandResult(
                    success = false,
                    message = "Invalid time: hour missing."
                )

        val minute =
            time.minute ?: 0

        if (hour !in 0..23) {
            return CommandResult(
                success = false,
                message = "Invalid hour."
            )
        }

        if (minute !in 0..59) {
            return CommandResult(
                success = false,
                message = "Invalid minute."
            )
        }

        android.util.Log.d(
            "Synapse",
            "Setting alarm: %02d:%02d".format(hour, minute)
        )

        val success =
            gateway.setAlarm(hour, minute)

        return CommandResult(
            success = success,
            message = if (success)
                "Alarm scheduled for %02d:%02d".format(hour, minute)
            else
                "Failed to schedule alarm."
        )
    }
}