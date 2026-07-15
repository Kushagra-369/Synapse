package com.synapse.mobile.features.skills.clock.gateway

class AndroidClockGateway : ClockGateway {

    override fun setAlarm(
        hour: Int,
        minute: Int
    ): Boolean {

        // Android AlarmManager implementation
        // will come here.

        return true

    }

}