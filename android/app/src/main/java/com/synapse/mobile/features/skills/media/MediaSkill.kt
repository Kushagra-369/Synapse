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

        val playAction =
            actions["play"]
                ?: return CommandResult(
                    false,
                    "Play action unavailable."
                )

        // =====================================================
        // MANUAL SELECTION
        // Example:
        // "second one"
        // selectionIndex = 1
        //
        // This is kept only for compatibility.
        // Normal music requests will NOT enter this path.
        // =====================================================

        val selectionIndex =
            command.parameters["selectionIndex"]
                ?.toString()
                ?.toIntOrNull()

        if (
            selectionIndex != null &&
            selectionState.hasPendingSelection()
        ) {

            val selected =
                selectionState.takeByIndex(selectionIndex)

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

        // =====================================================
        // NORMAL ACTION
        // =====================================================

        val action =
            actions[command.action]
                ?: return CommandResult(
                    success = false,
                    message =
                        "Unknown media action: ${command.action}"
                )

        // =====================================================
        // NON-PLAY ACTIONS
        // =====================================================

        if (command.action != "play") {
            return action.execute(command)
        }

        // =====================================================
        // PLAY QUERY
        // =====================================================

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

        // =====================================================
        // RESOLVE MEDIA
        //
        // Resolver returns the BEST MATCH.
        //
        // We intentionally ignore alternatives here.
        // Synapse should automatically play the best result.
        // =====================================================

        val result =
            resolver.resolveDetailed(query)
                ?: return CommandResult(
                    success = false,
                    message = "Media not found."
                )

        val bestMatch =
            result.bestMatch

        // =====================================================
        // CLEAR OLD SELECTION
        // =====================================================

        selectionState.clear()

        // =====================================================
        // DIRECT PLAY
        //
        // Whatever the resolver considers the best match,
        // play it immediately.
        // =====================================================

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