package com.synapse.mobile.features.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SynapseAccessibilityService :
    AccessibilityService(),
    AccessibilityGateway {

    companion object {

        var instance: SynapseAccessibilityService? = null
            private set

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
        // Future automation logic
    }

    override fun onInterrupt() {}

    override fun getRoot(): AccessibilityNodeInfo? {
        return rootInActiveWindow
    }

    override fun click(
        node: AccessibilityNodeInfo
    ): Boolean {

        return node.performAction(
            AccessibilityNodeInfo.ACTION_CLICK
        )
    }

    override fun typeText(
        node: AccessibilityNodeInfo,
        text: String
    ): Boolean {

        val bundle = Bundle()

        bundle.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            text
        )

        return node.performAction(
            AccessibilityNodeInfo.ACTION_SET_TEXT,
            bundle
        )
    }

    override fun findByText(
        text: String
    ): List<AccessibilityNodeInfo> {

        return rootInActiveWindow
            ?.findAccessibilityNodeInfosByText(text)
            ?: emptyList()
    }

    override fun performGlobalBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun performHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    override fun performRecentApps() {
        performGlobalAction(GLOBAL_ACTION_RECENTS)
    }
}