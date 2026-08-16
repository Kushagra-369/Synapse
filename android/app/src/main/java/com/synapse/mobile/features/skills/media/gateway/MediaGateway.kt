package com.synapse.mobile.features.skills.media.gateway

interface MediaGateway {

    suspend fun play(query: String): Boolean

    fun pause(): Boolean

    fun resume(): Boolean

    fun stop(): Boolean

    fun next(): Boolean

    fun previous(): Boolean
}