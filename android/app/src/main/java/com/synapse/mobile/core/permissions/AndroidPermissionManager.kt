package com.synapse.mobile.core.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class AndroidPermissionManager(
    private val context: Context
) : PermissionManager {

    override fun getPermissionStatus(
        permission: PermissionType
    ): PermissionStatus {

        return if (isGranted(permission)) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }

    }

    override fun isGranted(
        permission: PermissionType
    ): Boolean {

        val androidPermission = when (permission) {

            PermissionType.CONTACTS ->
                Manifest.permission.READ_CONTACTS

            PermissionType.PHONE ->
                Manifest.permission.CALL_PHONE

            PermissionType.SMS ->
                Manifest.permission.SEND_SMS

            PermissionType.CALENDAR ->
                Manifest.permission.WRITE_CALENDAR

            PermissionType.MICROPHONE ->
                Manifest.permission.RECORD_AUDIO

            PermissionType.CAMERA ->
                Manifest.permission.CAMERA

            PermissionType.STORAGE ->
                Manifest.permission.READ_EXTERNAL_STORAGE

            PermissionType.NOTIFICATIONS ->
                Manifest.permission.POST_NOTIFICATIONS

            else -> return false

        }

        return ContextCompat.checkSelfPermission(
            context,
            androidPermission
        ) == PackageManager.PERMISSION_GRANTED

    }

}