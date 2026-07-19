package com.synapse.mobile.features.skills.contacts.gateway

interface ContactGateway {

    fun findContactNumber(
        name: String
    ): String?

}