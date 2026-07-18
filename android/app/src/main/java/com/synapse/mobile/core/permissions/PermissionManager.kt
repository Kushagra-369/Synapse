package com.synapse.mobile.core.permissions

interface PermissionManager {

    fun getPermissionStatus(
        permission: PermissionType
    ): PermissionStatus

    fun isGranted(
        permission: PermissionType
    ): Boolean

}