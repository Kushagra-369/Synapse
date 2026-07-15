package com.synapse.mobile.features.skills.clock.gateway

interface ClockGateway {

    fun setAlarm(
        hour: Int,
        minute: Int
    ): Boolean

}