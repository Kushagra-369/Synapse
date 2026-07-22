package com.synapse.mobile.features.skills.whatsapp.gateway

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidWhatsAppGateway(
    private val context: Context
) : WhatsAppGateway {

    override fun open(): Boolean {

        return try {

            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            try {

                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(browserIntent)

                true

            } catch (e: Exception) {

                false

            }

        }

    }

    override fun sendMessage(
        phone: String,
        message: String
    ): Boolean {

        return try {

            val url =
                "https://wa.me/$phone?text=${Uri.encode(message)}"

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun openChat(
        phone: String
    ): Boolean {

        return try {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/$phone")
            )

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }


    override fun voiceCall(phone: String): Boolean {
        return false
    }

    override fun videoCall(phone: String): Boolean {
        return false
    }

}