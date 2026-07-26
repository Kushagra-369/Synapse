package com.synapse.mobile.core.resolver

import android.graphics.drawable.Drawable

data class ResolvedApp(
    val packageName: String,
    val appName: String,
    val icon: Drawable? = null,
    val isSystemApp: Boolean = false
)