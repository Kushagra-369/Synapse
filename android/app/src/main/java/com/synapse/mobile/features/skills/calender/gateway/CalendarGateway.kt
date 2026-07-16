package com.synapse.mobile.features.skills.calendar.gateway

interface CalendarGateway {

    fun createEvent(
        title: String,
        startTime: Long,
        endTime: Long
    ): Boolean

}