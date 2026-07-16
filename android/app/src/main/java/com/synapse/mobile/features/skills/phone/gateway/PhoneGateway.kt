package com.synapse.mobile.features.skills.phone.gateway

interface PhoneGateway {

    fun dialPhone(
        number: String
    ): Boolean

}