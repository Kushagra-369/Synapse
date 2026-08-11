package com.synapse.mobile.core.response

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult

object ResponseGenerator {

    fun generate(
        command: Command,
        executionResult: CommandResult
    ): CommandResult {

        val success = executionResult.success
        val executionMessage = executionResult.message

        val params = command.parameters

        val message = when (command.skill) {

            "maps" -> {

                when (command.action) {

                    "get_current_location" -> {

                        executionMessage

                    }

                    else -> {

                        if (success)
                            ResponseTemplates.OPENING_MAPS
                        else
                            ResponseTemplates.UNKNOWN_COMMAND

                    }
                }
            }

            "phone" -> {

                val contact = params["contact"]?.toString() ?: "contact"

                if (success) {
                    ResponseTemplates.CALLING_CONTACT.format(contact)
                } else {
                    ResponseTemplates.CONTACT_NOT_FOUND.format(contact)
                }

            }

            "whatsapp" -> {

                val contact = params["contact"]?.toString() ?: "contact"

                if (success) {
                    ResponseTemplates.WHATSAPP.format(contact)
                } else {
                    ResponseTemplates.CONTACT_NOT_FOUND.format(contact)
                }

            }

            "browser" -> {

                val website = params["website"]?.toString() ?: "website"

                if (success) {
                    ResponseTemplates.OPENING_WEBSITE.format(website)
                } else {
                    ResponseTemplates.UNKNOWN_COMMAND
                }

            }

            "youtube" -> {
                if (success)
                    ResponseTemplates.OPENING_YOUTUBE
                else
                    ResponseTemplates.UNKNOWN_COMMAND
            }

            "maps" -> {

                when (command.action) {

                    "get_current_location" -> {

                        if (success) {
                            ResponseTemplates.SUCCESS
                        } else {
                            "Unable to get current location."
                        }

                    }

                    else -> {

                        if (success)
                            ResponseTemplates.OPENING_MAPS
                        else
                            ResponseTemplates.UNKNOWN_COMMAND

                    }
                }
            }

            "clock" -> {
                if (success)
                    ResponseTemplates.ALARM_SET
                else
                    ResponseTemplates.UNKNOWN_COMMAND
            }

            "timer" -> {
                if (success)
                    ResponseTemplates.TIMER_STARTED
                else
                    ResponseTemplates.UNKNOWN_COMMAND
            }

            "stopwatch" -> {
                if (success)
                    ResponseTemplates.STOPWATCH_STARTED
                else
                    ResponseTemplates.UNKNOWN_COMMAND
            }

            "calendar" -> {

                val title =
                    params["title"]?.toString() ?: "event"

                if (success)
                    ResponseTemplates.CALENDAR_EVENT_CREATED.format(title)
                else
                    ResponseTemplates.CALENDAR_EVENT_FAILED

            }

            "device" -> {

                if (!success) {
                    ResponseTemplates.UNKNOWN_COMMAND
                } else {

                    when (command.action) {

                        "flashlightOn" ->
                            ResponseTemplates.FLASHLIGHT_ON

                        "flashlightOff" ->
                            ResponseTemplates.FLASHLIGHT_OFF

                        "setBrightness" ->
                            ResponseTemplates.BRIGHTNESS_CHANGED

                        "setVolume" ->
                            ResponseTemplates.VOLUME_CHANGED

                        else ->
                            ResponseTemplates.SUCCESS

                    }

                }

            }

            else -> ResponseTemplates.UNKNOWN_COMMAND

        }

        return CommandResult(
            success = success,
            message = message
        )
    }

}