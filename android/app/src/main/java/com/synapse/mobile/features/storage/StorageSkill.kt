package com.synapse.mobile.features.storage

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import java.io.File

class StorageSkill(
    private val gateway: FileGateway
) : Skill {

    override val name = "storage"

    override fun execute(
        command: Command
    ): CommandResult {

        return when (command.action) {

            "read_file" -> {

                val path = command.parameters["path"]?.toString()
                    ?: return CommandResult(
                        false,
                        "Missing path"
                    )

                val text = gateway.readText(File(path))

                CommandResult(
                    success = text != null,
                    message = text ?: "Unable to read file."
                )
            }

            "write_file" -> {

                val path = command.parameters["path"]?.toString()
                    ?: return CommandResult(
                        false,
                        "Missing path"
                    )

                val text = command.parameters["text"]?.toString()
                    ?: ""

                val success = gateway.writeText(
                    File(path),
                    text
                )

                CommandResult(
                    success,
                    if (success)
                        "File saved."
                    else
                        "Failed to save file."
                )
            }

            "list_files" -> {

                val files = gateway.listFiles()

                CommandResult(
                    true,
                    files.joinToString("\n") {
                        it.name
                    }
                )
            }

            else -> CommandResult(
                false,
                "Unknown action: ${command.action}"
            )

        }

    }

}