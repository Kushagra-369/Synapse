package com.synapse.mobile.features.skills.media.gateway

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.synapse.mobile.features.skills.media.resolver.MediaResolver

class AndroidMediaGateway(
    context: Context,
    private val resolver: MediaResolver
) : MediaGateway {

    private val player: ExoPlayer =
        ExoPlayer.Builder(
            context.applicationContext
        ).build()

    private val queue =
        mutableListOf<MediaItem>()

    init {

        player.addListener(
            object : Player.Listener {

                override fun onPlayerError(
                    error: androidx.media3.common.PlaybackException
                ) {

                    Log.e(
                        "SYNAPSE_PLAYER",
                        "PLAYBACK ERROR"
                    )

                    Log.e(
                        "SYNAPSE_PLAYER",
                        "ERROR CODE: ${error.errorCode}"
                    )

                    Log.e(
                        "SYNAPSE_PLAYER",
                        "ERROR MESSAGE: ${error.message}"
                    )

                    Log.e(
                        "SYNAPSE_PLAYER",
                        "CAUSE: ${error.cause}"
                    )
                }

                override fun onPlaybackStateChanged(
                    playbackState: Int
                ) {

                    when (playbackState) {

                        Player.STATE_IDLE ->
                            Log.d(
                                "SYNAPSE_PLAYER",
                                "STATE: IDLE"
                            )

                        Player.STATE_BUFFERING ->
                            Log.d(
                                "SYNAPSE_PLAYER",
                                "STATE: BUFFERING"
                            )

                        Player.STATE_READY ->
                            Log.d(
                                "SYNAPSE_PLAYER",
                                "STATE: READY - AUDIO SHOULD PLAY"
                            )

                        Player.STATE_ENDED ->
                            Log.d(
                                "SYNAPSE_PLAYER",
                                "STATE: ENDED"
                            )
                    }
                }
            }
        )
    }

    override suspend fun play(
        query: String
    ): Boolean {

        return try {

            Log.d(
                "SYNAPSE_PLAYER",
                "PLAY REQUEST: $query"
            )

            val source =
                resolver.resolve(query)
                    ?: run {

                        Log.e(
                            "SYNAPSE_PLAYER",
                            "NO MEDIA SOURCE FOUND"
                        )

                        return false
                    }

            Log.d(
                "SYNAPSE_PLAYER",
                "TITLE: ${source.title}"
            )

            Log.d(
                "SYNAPSE_PLAYER",
                "ARTIST: ${source.artist}"
            )

            Log.d(
                "SYNAPSE_PLAYER",
                "URI: ${source.uri}"
            )

            val mediaItem =
                MediaItem.Builder()
                    .setUri(source.uri)
                    .setMediaId(source.uri)
                    .setMimeType("audio/mpeg")
                    .build()

            queue.clear()
            queue.add(mediaItem)

            player.stop()

            player.setMediaItems(
                queue.toList(),
                0,
                0L
            )

            Log.d(
                "SYNAPSE_PLAYER",
                "PREPARING PLAYER"
            )

            player.prepare()

            Log.d(
                "SYNAPSE_PLAYER",
                "STARTING PLAYBACK"
            )

            player.play()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "PLAY EXCEPTION",
                e
            )

            false
        }
    }

    override fun pause(): Boolean {

        return try {

            player.pause()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "PAUSE ERROR",
                e
            )

            false
        }
    }

    override fun resume(): Boolean {

        return try {

            player.play()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "RESUME ERROR",
                e
            )

            false
        }
    }

    override fun stop(): Boolean {

        return try {

            player.stop()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "STOP ERROR",
                e
            )

            false
        }
    }

    override fun next(): Boolean {

        return try {

            if (!player.hasNextMediaItem()) {
                return false
            }

            player.seekToNextMediaItem()
            player.play()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "NEXT ERROR",
                e
            )

            false
        }
    }

    override fun previous(): Boolean {

        return try {

            if (!player.hasPreviousMediaItem()) {
                return false
            }

            player.seekToPreviousMediaItem()
            player.play()

            true

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_PLAYER",
                "PREVIOUS ERROR",
                e
            )

            false
        }
    }
}