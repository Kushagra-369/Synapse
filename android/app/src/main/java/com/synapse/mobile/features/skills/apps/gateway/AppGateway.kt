package com.synapse.mobile.features.skills.apps.gateway

interface AppGateway {

    fun launchApp(
        packageName: String
    ): Boolean

}