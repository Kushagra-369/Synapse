package com.synapse.mobile.features.accessibility

import android.view.accessibility.AccessibilityNodeInfo

class AccessibilityAction(
    private val gateway: AccessibilityGateway
) {

    fun clickText(
        text: String
    ): Boolean {

        val nodes = gateway.findByText(text)

        if (nodes.isEmpty()) {
            return false
        }

        return gateway.click(nodes.first())
    }

    fun type(
        node: AccessibilityNodeInfo,
        text: String
    ): Boolean {

        return gateway.typeText(node, text)
    }

    fun back() {
        gateway.performGlobalBack()
    }

    fun home() {
        gateway.performHome()
    }

    fun recentApps() {
        gateway.performRecentApps()
    }
}