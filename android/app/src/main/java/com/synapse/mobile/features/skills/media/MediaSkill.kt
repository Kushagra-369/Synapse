package com.synapse.mobile.features.skills.media

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.media.actions.NextMediaAction
import com.synapse.mobile.features.skills.media.actions.PauseMediaAction
import com.synapse.mobile.features.skills.media.actions.PlayMediaAction
import com.synapse.mobile.features.skills.media.actions.PreviousMediaAction
import com.synapse.mobile.features.skills.media.actions.ResumeMediaAction
import com.synapse.mobile.features.skills.media.actions.StopMediaAction
import com.synapse.mobile.features.skills.media.gateway.MediaGateway
import com.synapse.mobile.features.skills.media.resolver.MediaResolver
import com.synapse.mobile.features.skills.media.session.MediaSelectionState

class MediaSkill(
    private val gateway: MediaGateway,
    private val resolver: MediaResolver,
    private val selectionState: MediaSelectionState
) : Skill {

    override val name = "media"

    private val actions = mapOf(
        "play" to PlayMediaAction(gateway),
        "pause" to PauseMediaAction(gateway),
        "resume" to ResumeMediaAction(gateway),
        "stop" to StopMediaAction(gateway),
        "next" to NextMediaAction(gateway),
        "previous" to PreviousMediaAction(gateway)
    )

    override suspend fun execute(
        command: Command
    ): CommandResult {

        // -----------------------------------------
        // PLAY ACTION
        // -----------------------------------------

        val playAction =
            actions["play"]
                ?: return CommandResult(
                    false,
                    "Play action unavailable."
                )

        // -----------------------------------------
        // SELECTION
        // Example:
        // "second one"
        // selectionIndex = 1
        // -----------------------------------------

        val selectionIndex =
            command.parameters["selectionIndex"]
                ?.toString()
                ?.toIntOrNull()

        if (
            selectionIndex != null &&
            selectionState.hasPendingSelection()
        ) {

            val selected =
                selectionState.takeByIndex(
                    selectionIndex
                )

            if (selected == null) {

                return CommandResult(
                    success = false,
                    message = "Invalid media selection."
                )
            }

            return playAction.execute(
                command.copy(
                    action = "play",
                    parameters =
                        command.parameters
                            .toMutableMap()
                            .apply {

                                this["query"] =
                                    selected.title

                                this["resolvedUri"] =
                                    selected.uri

                                this["resolvedTitle"] =
                                    selected.title

                                this["resolvedArtist"] =
                                    selected.artist ?: ""

                                this["resolvedAlbum"] =
                                    selected.album ?: ""
                            }
                )
            )
        }

        // -----------------------------------------
        // NORMAL ACTION
        // -----------------------------------------

        val action =
            actions[command.action]
                ?: return CommandResult(
                    success = false,
                    message =
                        "Unknown media action: ${command.action}"
                )

        // -----------------------------------------
        // NON-PLAY ACTIONS
        // -----------------------------------------

        if (command.action != "play") {
            return action.execute(command)
        }

        // -----------------------------------------
        // PLAY QUERY
        // -----------------------------------------

        val query =
            command.parameters["query"]
                ?.toString()
                ?.trim()

        if (query.isNullOrBlank()) {

            return CommandResult(
                success = false,
                message = "Media query missing."
            )
        }

        // -----------------------------------------
        // RESOLVE MEDIA
        // -----------------------------------------

        val result =
            resolver.resolveDetailed(query)
                ?: return CommandResult(
                    success = false,
                    message = "Media not found."
                )

        val bestMatch =
            result.bestMatch

        val alternatives =
            result.alternatives

        // -----------------------------------------
        // EXACT TITLE MATCH
        // -----------------------------------------

        if (
            bestMatch.title.equals(
                query,
                ignoreCase = true
            )
        ) {

            selectionState.clear()

            return playAction.execute(
                command.copy(
                    action = "play",
                    parameters =
                        command.parameters
                            .toMutableMap()
                            .apply {

                                this["query"] =
                                    bestMatch.title

                                this["resolvedUri"] =
                                    bestMatch.uri

                                this["resolvedTitle"] =
                                    bestMatch.title

                                this["resolvedArtist"] =
                                    bestMatch.artist ?: ""

                                this["resolvedAlbum"] =
                                    bestMatch.album ?: ""
                            }
                )
            )
        }

        // -----------------------------------------
        // AMBIGUOUS RESULT
        // -----------------------------------------

        if (alternatives.isNotEmpty()) {

            val allResults =
                listOf(bestMatch) + alternatives

            selectionState.setResults(
                allResults
            )

            val options =
                buildString {

                    append(
                        "I found multiple matches. "
                    )

                    allResults
                        .take(3)
                        .forEachIndexed { index, media ->

                            if (index > 0) {
                                append(", ")
                            }

                            append(
                                "${index + 1}. "
                            )

                            append(
                                media.title
                            )

                            media.artist?.let {

                                append(
                                    " by $it"
                                )
                            }
                        }

                    append(
                        ". Please specify which one you want."
                    )
                }

            return CommandResult(
                success = false,
                message = options
            )
        }

        // -----------------------------------------
        // SINGLE NON-EXACT MATCH
        // -----------------------------------------

        selectionState.clear()

        return playAction.execute(
            command.copy(
                action = "play",
                parameters =
                    command.parameters
                        .toMutableMap()
                        .apply {

                            this["query"] =
                                bestMatch.title

                            this["resolvedUri"] =
                                bestMatch.uri

                            this["resolvedTitle"] =
                                bestMatch.title

                            this["resolvedArtist"] =
                                bestMatch.artist ?: ""

                            this["resolvedAlbum"] =
                                bestMatch.album ?: ""
                        }
            )
        )
    }
}