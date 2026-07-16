package com.synapse.mobile.features.skills.clock.gateway

interface ClockGateway {

    /**
     * Creates an alarm inside the user's Clock application.
     *
     * @return true if the request was accepted.
     */
    fun setAlarm(
        hour: Int,
        minute: Int
    ): Boolean

}