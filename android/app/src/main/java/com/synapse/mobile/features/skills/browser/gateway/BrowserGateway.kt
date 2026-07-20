package com.synapse.mobile.features.skills.browser.gateway

interface BrowserGateway {

    fun openUrl(
        url: String
    ): Boolean

}