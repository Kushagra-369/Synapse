package com.synapse.mobile.features.skills.whatsapp.gateway

interface WhatsAppGateway {

    fun open(): Boolean

    fun sendMessage(
        phone: String,
        message: String
    ): Boolean

    fun openChat(
        phone: String
    ): Boolean

    fun voiceCall(
        phone: String
    ): Boolean

    fun videoCall(
        phone: String
    ): Boolean

}