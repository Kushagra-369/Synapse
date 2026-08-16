package com.synapse.mobile.features.skills.media.resolver

import com.synapse.mobile.features.skills.media.provider.MediaProvider

class MediaResolver(
    private val providers: List<MediaProvider>
) {

    suspend fun resolve(
        query: String
    ): MediaSource? {

        val normalized =
            query.trim()

        if (normalized.isBlank()) {
            return null
        }

        for (provider in providers) {

            val result =
                provider.search(normalized)

            if (result != null) {
                return result
            }
        }

        return null
    }
}