package com.synapse.mobile.features.skills.media.gateway

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.synapse.mobile.features.skills.media.resolver.MediaResolver

class AndroidMediaGateway(
    context: Context
) : MediaGateway {

    private val resolver =
        MediaResolver()

    private val player: ExoPlayer =
        ExoPlayer.Builder(
            context.applicationContext
        ).build()



    override fun play(query: String): Boolean {
        return try {

            val mediaUri =
                resolver.resolve(query)
                    ?: return false

            val mediaItem =
                MediaItem.fromUri(mediaUri)

            player.setMediaItem(mediaItem)

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

            if (player.hasNextMediaItem()) {

                player.seekToNextMediaItem()
                player.play()

                true

            } else {

                false
            }

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    override fun previous(): Boolean {
        return try {

            if (player.hasPreviousMediaItem()) {

                player.seekToPreviousMediaItem()
                player.play()

                true

            } else {

                false
            }

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }
}