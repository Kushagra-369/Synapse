package com.synapse.mobile.features.skills.media.resolver

data class MediaSearchResult(
    val bestMatch: MediaSource,
    val alternatives: List<MediaSource> = emptyList()
)