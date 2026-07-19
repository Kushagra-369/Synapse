package com.synapse.mobile.features.accessibility

import android.view.accessibility.AccessibilityNodeInfo

interface AccessibilityGateway {

    fun getRoot(): AccessibilityNodeInfo?

    fun click(
        node: AccessibilityNodeInfo
    ): Boolean

    fun typeText(
        node: AccessibilityNodeInfo,
        text: String
    ): Boolean

    fun findByText(
        text: String
    ): List<AccessibilityNodeInfo>

    fun performGlobalBack()

    fun performHome()

    fun performRecentApps()
}