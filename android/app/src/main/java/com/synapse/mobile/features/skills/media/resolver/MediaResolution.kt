package com.synapse.mobile.features.skills.media.resolver

data class MediaResolution(
    val bestMatch: MediaSource,
    val alternatives: List<MediaSource> = emptyList()
)