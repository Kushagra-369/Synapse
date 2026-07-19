package com.synapse.mobile.features.skills.apps.gateway

import android.content.Context

class AndroidAppGateway(
    private val context: Context
) : AppGateway {

    override fun launchApp(
        packageName: String
    ): Boolean {

        val intent = context.packageManager
            .getLaunchIntentForPackage(packageName)
            ?: return false

        intent.addFlags(
            android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)

        return true

    }

}