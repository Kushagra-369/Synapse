package com.synapse.mobile.features.skills.stopwatch.gateway

import android.content.Context
import android.util.Log

class AndroidStopwatchGateway(
    private val context: Context
) : StopwatchGateway {

    override fun startStopwatch(): Boolean {

        Log.d("Synapse", "Stopwatch started.")

        // Real implementation later

        return true
    }

}