package com.synapse.mobile.features.skills.media.resolver

data class MediaSource(
    val title: String,
    val uri: String,
    val artist: String? = null,
    val album: String? = null,
    val duration: Long? = null,
    val coverArt: String? = null
)