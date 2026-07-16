package com.synapse.mobile.features.skills.timer.gateway

interface TimerGateway {

    fun startTimer(
        seconds: Int
    ): Boolean

}