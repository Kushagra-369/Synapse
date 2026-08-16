package com.synapse.mobile.features.skills.media.provider

import com.synapse.mobile.features.skills.media.resolver.MediaSearchResult

interface MediaProvider {

    suspend fun search(
        query: String
    ): MediaSearchResult?
}