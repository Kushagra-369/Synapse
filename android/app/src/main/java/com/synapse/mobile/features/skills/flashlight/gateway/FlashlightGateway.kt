package com.synapse.mobile.features.skills.flashlight.gateway

interface FlashlightGateway {

    fun turnOn(): Boolean

    fun turnOff(): Boolean

    fun toggle(): Boolean
}