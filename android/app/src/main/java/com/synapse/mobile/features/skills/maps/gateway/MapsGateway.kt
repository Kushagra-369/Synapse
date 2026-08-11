package com.synapse.mobile.features.skills.maps.gateway

interface MapsGateway {

    suspend fun getCurrentLocation(): String?

    fun openMaps(
        query: String
    ): Boolean

    fun navigate(destination: String): Boolean

    fun navigate(origin: String, destination: String): Boolean

    fun searchPlace(
        place: String
    ): Boolean

}