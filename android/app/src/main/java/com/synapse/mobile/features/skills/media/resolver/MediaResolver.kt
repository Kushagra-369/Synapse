package com.synapse.mobile.features.skills.media.resolver

import android.net.Uri

class MediaResolver {

    /**
     * Resolves a user's media query into a playable media URI.
     *
     * For now this is a simple catalog-based resolver.
     * Later this can be connected to a backend/media provider.
     */
    fun resolve(query: String): Uri? {

        val normalized = query
            .lowercase()
            .trim()

        return when {

            // Temporary test media
            normalized == "test" ->
                Uri.parse(
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                )

            normalized == "test song" ->
                Uri.parse(
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                )

            else ->
                null
        }
    }
}