package com.synapse.mobile.features.skills.browser.gateway

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidBrowserGateway(
    private val context: Context
) : BrowserGateway {

    override fun openUrl(
        url: String
    ): Boolean {

        return try {

            val finalUrl =
                if (
                    url.startsWith("http://") ||
                    url.startsWith("https://")
                ) {
                    url
                } else {
                    "https://$url"
                }

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(finalUrl)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}