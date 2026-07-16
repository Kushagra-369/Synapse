package com.synapse.mobile.features.skills.phone.gateway

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

class AndroidPhoneGateway(
    private val context: Context
) : PhoneGateway {

    override fun dialPhone(
        number: String
    ): Boolean {

        return try {

            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED

            val intent = if (hasPermission) {

                Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$number")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            } else {

                Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$number")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}