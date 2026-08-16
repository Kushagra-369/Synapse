package com.synapse.mobile.features.skills.media.provider

import com.synapse.mobile.features.skills.media.resolver.MediaSource

class CatalogMediaProvider : MediaProvider {

    private val catalog = listOf(

        MediaSource(
            title = "test",
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),

        MediaSource(
            title = "test 2",
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        ),

        MediaSource(
            title = "test 3",
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        )
    )

    override suspend fun search(query: String): MediaSource? {

        val normalized =
            query.trim().lowercase()

        // Exact match
        catalog.firstOrNull {
            it.title.lowercase() == normalized
        }?.let {
            return it
        }

        // Partial match
        return catalog.firstOrNull {
            it.title.lowercase().contains(normalized)
        }
    }
}