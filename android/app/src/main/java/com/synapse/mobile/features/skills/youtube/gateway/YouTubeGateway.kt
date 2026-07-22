package com.synapse.mobile.features.skills.youtube.gateway

interface YouTubeGateway {

    fun open(): Boolean

    fun search(query: String): Boolean

    fun play(query: String): Boolean

}