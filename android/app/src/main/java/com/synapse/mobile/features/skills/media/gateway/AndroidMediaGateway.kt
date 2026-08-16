package com.synapse.mobile.features.skills.media.gateway

import android.content.Context
import androidx.media3.common.MediaItem
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

    // Current playback queue
    private val queue =
        mutableListOf<MediaItem>()

    override suspend fun play(
        query: String
    ): Boolean {

        return try {

            val source =
                resolver.resolve(query)
                    ?: return false

            val mediaItem =
                MediaItem.Builder()
                    .setUri(source.uri)
                    .setMediaId(source.title)
                    .build()

            // Avoid duplicate items
            val alreadyExists =
                queue.any {
                    it.mediaId == mediaItem.mediaId
                }

            if (!alreadyExists) {
                queue.add(mediaItem)
            }

            val currentIndex =
                queue.indexOfFirst {
                    it.mediaId == mediaItem.mediaId
                }

            if (currentIndex == -1) {
                return false
            }

            player.setMediaItems(
                queue.toList(),
                currentIndex,
                0L
            )

            player.prepare()
            player.play()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    override fun pause(): Boolean {
        return try {

            player.pause()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    override fun resume(): Boolean {
        return try {

            player.play()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    override fun stop(): Boolean {
        return try {

            player.stop()

            true

        } catch (e: Exception) {

            e.printStackTrace()

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

            e.printStackTrace()

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

            e.printStackTrace()

            false
        }
    }
}