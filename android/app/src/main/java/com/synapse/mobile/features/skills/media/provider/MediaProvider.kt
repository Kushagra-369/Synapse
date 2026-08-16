package com.synapse.mobile.features.skills.media.provider

import com.synapse.mobile.features.skills.media.resolver.MediaSource

interface MediaProvider {

    suspend fun search(query: String): MediaSource?
}